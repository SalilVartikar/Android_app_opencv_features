package com.example.salil.project2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Main2Activity extends AppCompatActivity implements OnTouchListener, CvCameraViewListener2 {
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRGBA;
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
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_tutorial_activity_surface_view);

        mOpenCvCameraView.setCameraIndex(1);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
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
        if(!OpenCVLoader.initDebug())
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0,this, mLoaderCallback);
        else
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
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

//        Imgproc.cvtColor(mRGBA, mRGBA, Imgproc.COLOR_RGBA2RGB);
//        MatOfKeyPoint keypoints = new MatOfKeyPoint();
//        FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
//        detector.detect(mRGBA, keypoints);
//        Features2d.drawKeypoints(mRGBA, keypoints, mRGBA);
//
        int choice = getIntent().getIntExtra("Choice", 0);
//
        switch(choice) {
            case 1: return mRGBA;

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
        }
        return mRGBA;
    }
}
