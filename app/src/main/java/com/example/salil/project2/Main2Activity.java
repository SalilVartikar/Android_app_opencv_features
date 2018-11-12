package com.example.salil.project2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class Main2Activity extends AppCompatActivity implements OnTouchListener, CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;

    public Mat mRGBA;
    TextView touch_coordinates;
    TextView touch_color;
    double x = -1;
    double y = -1;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(Main2Activity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        touch_coordinates = (TextView) findViewById(R.id.touch_coordinates);
        touch_color = (TextView) findViewById(R.id.touch_color);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.cam1);

        mOpenCvCameraView.setCameraIndex(1);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        Button normal = (Button) findViewById(R.id.Normal);

        normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Mat m = new Mat();

                // Start NewActivity.class
                Intent myIntent = new Intent(Main2Activity.this,
                        Main3Activity.class);

                Imgproc.cvtColor(mRGBA, m, Imgproc.COLOR_RGBA2BGR, 3);

                File path = new File(Environment.getExternalStorageDirectory() + "/Images/");
                path.mkdirs();
                File file = new File(path, "image.jpg");

                String filename = file.toString();
                Boolean bool = Imgcodecs.imwrite(filename, m);

                if (!bool)
                    Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_LONG).show();

                long addr = mRGBA.getNativeObjAddr();
                myIntent.putExtra("Address", addr);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
        }
        else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int cols = mRGBA.cols();
        int rows = mRGBA.rows();

        double yLow = (double) mOpenCvCameraView.getHeight()*0.2401961;
        double yHigh = (double) mOpenCvCameraView.getHeight()*0.7696078;
        double xScale = (double)rows/(double)mOpenCvCameraView.getWidth();
        double yScale = (double)rows/(yHigh - yLow);

        x = event.getX();
        y = event.getY();

        y = y - yLow;
        x = x * xScale;
        y = y * yScale;

        if((x < 0) || (y < 0) || (x > cols) || (y > rows))
            return false;

        touch_coordinates.setText("X: " + Double.valueOf(x) + ", Y: " + Double.valueOf(y));

        Rect touchedRect = new Rect();

        touchedRect.x = (int)x;
        touchedRect.y = (int)y;

        touchedRect.width = 8;
        touchedRect.height = 8;

        Mat touchedRegionRgba = mRGBA.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);
        //Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width * touchedRect.height;
        for(int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;
        mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv);
        touch_color.setText("Color: #" + String.format("%02X", (int)mBlobColorRgba.val[0]) + String.format("%02X", (int)mBlobColorRgba.val[1]) + String.format("%02X", (int)mBlobColorRgba.val[2]));

        touch_color.setTextColor(Color.rgb((int)mBlobColorRgba.val[0],(int)mBlobColorRgba.val[1],
                (int)mBlobColorRgba.val[2]));

        touch_coordinates.setTextColor(Color.rgb((int)mBlobColorRgba.val[0],(int)mBlobColorRgba.val[1],
                (int)mBlobColorRgba.val[2]));

        return false;
    }

    private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        int choice = getIntent().getIntExtra("Choice", 0);
        switch(choice) {
            case 1:
                return mRGBA;

            case 2:
                try {
                    mRGBA.convertTo(mRGBA, -1, 1, 50);
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;

            case 3:
                Imgproc.cvtColor(mRGBA, mRGBA, Imgproc.COLOR_RGBA2RGB);
                MatOfKeyPoint keypoints = new MatOfKeyPoint();
                FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
                detector.detect(mRGBA, keypoints);
                Features2d.drawKeypoints(mRGBA, keypoints, mRGBA);
                break;

            case 4:
                Mat mGrey = new Mat(mRGBA.size(), CvType.CV_8UC1);
                Imgproc.cvtColor(mRGBA, mGrey, Imgproc.COLOR_RGBA2GRAY);
                Imgproc.Canny(mGrey, mRGBA, 50, 150);
                break;
        }
        return mRGBA;
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
