package com.example.salil.project2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class Main2Activity extends AppCompatActivity implements OnTouchListener, CvCameraViewListener2 {

    /*Declaring variables*/
    private CameraBridgeViewBase mOpenCvCameraView;
    public Mat mRGBA;
    public Mat mNormal;
    public int choice = 1;
    TextView touch_coordinates;
    TextView touch_color;
    double x = -1;
    double y = -1;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;

    /*Loader function to load openCV*/
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

        /*Requesting read and write permission*/
        //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
            }
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},9);
            }
        //}
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        touch_coordinates = (TextView) findViewById(R.id.touch_coordinates);
        touch_color = (TextView) findViewById(R.id.touch_color);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.cam1);

        /*Obtain and set camera choice*/
        int camChoice = getIntent().getIntExtra("camChoice", 0);
        mOpenCvCameraView.setCameraIndex(camChoice);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        /*Declare buttons*/
        Button normal = (Button) findViewById(R.id.Normal);
        Button brightness = (Button) findViewById(R.id.Brightness);
        Button keypoints = (Button) findViewById(R.id.Keypoints);
        Button canny = (Button) findViewById(R.id.Canny);
        Button capture = (Button) findViewById(R.id.Capture);
        Button mirror = (Button) findViewById(R.id.Mirror);

        /*Capture and store frame as .jpg image*/
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Mat m1 = new Mat();

                /*Go to new activity*/
                Intent myIntent = new Intent(Main2Activity.this,
                        Main3Activity.class);

                if(choice != 1) {
                    Toast.makeText(getApplicationContext(), "Capture only in Normal mode", Toast.LENGTH_SHORT).show();
                }
                else {
                    Imgproc.cvtColor(mNormal, m1, Imgproc.COLOR_RGBA2BGR, 3);
                    File path = new File(Environment.getExternalStorageDirectory() + "/Images/");
                    path.mkdirs();
                    File file = new File(path, "image.jpg");

                    String filename = file.toString();
                    Boolean bool = Imgcodecs.imwrite(filename, m1);

                    startActivity(myIntent);
                }
            }
        });

        /*Display image without any changes*/
        normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                choice = 1;
            }
        });

        /*Select brightness enhancement*/
        brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                choice = 2;
            }
        });

        /*Select detection and drawing keypoints*/
        keypoints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                choice = 3;
            }
        });

        /*Select edge detection*/
        canny.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                choice = 4;
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        /*Calculations for displaying color of the location touched*/
        if(choice == 1) {
            int cols = mRGBA.cols();
            int rows = mRGBA.rows();

            double yLow = (double) mOpenCvCameraView.getHeight() * 0.2401961;
            double yHigh = (double) mOpenCvCameraView.getHeight() * 0.7696078;
            double xScale = (double) rows / (double) mOpenCvCameraView.getWidth();
            double yScale = (double) rows / (yHigh - yLow);

            x = event.getX();
            y = event.getY();

            y = y - yLow;
            x = x * xScale;
            y = y * yScale;

            if ((x < 0) || (y < 0) || (x > cols) || (y > rows))
                return false;

            touch_coordinates.setText("X: " + x + ", Y: " + y);

            Rect touchedRect = new Rect();

            touchedRect.x = (int) x;
            touchedRect.y = (int) y;

            touchedRect.width = 8;
            touchedRect.height = 8;

            Mat touchedRegionRgba = mRGBA.submat(touchedRect);

            Mat touchedRegionHsv = new Mat();
            Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

            mBlobColorHsv = Core.sumElems(touchedRegionHsv);
            int pointCount = touchedRect.width * touchedRect.height;
            for (int i = 0; i < mBlobColorHsv.val.length; i++)
                mBlobColorHsv.val[i] /= pointCount;
            mBlobColorRgba = convertScalarHsv2Rgba(mBlobColorHsv);
            touch_color.setText("Color: #" + String.format("%02X", (int) mBlobColorRgba.val[0]) + String.format("%02X", (int) mBlobColorRgba.val[1]) + String.format("%02X", (int) mBlobColorRgba.val[2]));

            touch_color.setTextColor(Color.rgb((int) mBlobColorRgba.val[0], (int) mBlobColorRgba.val[1],
                    (int) mBlobColorRgba.val[2]));

            touch_coordinates.setTextColor(Color.rgb((int) mBlobColorRgba.val[0], (int) mBlobColorRgba.val[1],
                    (int) mBlobColorRgba.val[2]));
        }
        else {
            touch_color.setText("");
            touch_coordinates.setText("");
        }
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

        /*Get camera frame*/
        mRGBA = inputFrame.rgba();
        mNormal = inputFrame.rgba();

        /*Switch case to select between different features*/
        switch(choice) {
            /*Normal mode*/
            case 1:
                return mRGBA;

            /*Brighness enhancement*/
            case 2:
                try {
                    mRGBA.convertTo(mRGBA, -1, 1, 75);
                } catch (Exception e) {
                    System.out.println("ERROR");
                }
                break;

            /*Fast keypoint detection*/
            case 3:
                mRGBA = fastKp(mRGBA);
                break;

            /*Canny edge detection*/
            case 4:
                mRGBA = canny(mRGBA);
                break;

        }
        return mRGBA;
    }

    /*Calculating sobel edge detection*/
    public Mat sobel(Mat m) {
        Mat gray = new Mat();
        Mat sobel = new Mat();
        Mat x = new Mat();
        Mat y = new Mat();
        Mat absX = new Mat();
        Mat absY = new Mat();

        Imgproc.cvtColor(m, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.Sobel(gray, x, CvType.CV_16S, 1, 0, 3, 1, 0);
        Imgproc.Sobel(gray, y, CvType.CV_16S, 0, 1, 3, 1, 0);

        Core.convertScaleAbs(x, absX);
        Core.convertScaleAbs(y, absY);

        Core.addWeighted(absX, 0.5, absY, 0.5, 1, sobel);

        return sobel;
    }

    /*Calculating canny edge detection*/
    public Mat canny(Mat m) {
        Mat canny = new Mat();
        Mat mGrey = new Mat(m.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(m, mGrey, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.Canny(mGrey, canny, 50, 150);

        return canny;
    }

    /*FAST keypoint detection*/
    public Mat fastKp(Mat m) {
        Mat fast = new Mat();
        Imgproc.cvtColor(m, m, Imgproc.COLOR_RGBA2RGB);
        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
        detector.detect(m, keypoints);
        Features2d.drawKeypoints(m, keypoints, fast);

        return fast;
    }

    /*ORB keypoint detection*/
    public Mat orbKp(Mat m) {
        Mat mORB = new Mat();
        Imgproc.cvtColor(m, mORB, Imgproc.COLOR_RGBA2RGB);
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        FeatureDetector detector1 = FeatureDetector.create(FeatureDetector.ORB);
        detector1.detect(mORB, keypoints1);
        Features2d.drawKeypoints(mORB, keypoints1, mORB);

        return mORB;
    }
}
