package com.example.salil.project2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;

public class Main3Activity extends AppCompatActivity {
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;
    public int compare;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
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
        setContentView(R.layout.activity_main3);
        iv1 = (ImageView) findViewById(R.id.img1);
        iv2 = (ImageView) findViewById(R.id.img2);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);

        Button keypoints = (Button) findViewById(R.id.Keypoints);
        Button brightness = (Button) findViewById(R.id.Brightness);
        Button edge = (Button) findViewById(R.id.Edge);

        brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 1;
                displayImages();
            }
        });

        keypoints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 2;
                displayImages();
            }
        });

        edge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 3;
                displayImages();
            }
        });
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

    public void displayImages() {
        Mat m = Imgcodecs.imread("/storage/emulated/0/Images/image.jpg");
        Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2RGB);


        if(compare == 1) {
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);

            iv1.setImageBitmap(bm);
            tv1.setText("Normal");

            Mat mBright = new Mat();
            try {
                m.convertTo(mBright, -1, 1, 50);
            } catch (Exception e) {
                System.out.println(e);
            }
            Bitmap bmBright = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mBright, bmBright);

            iv2.setImageBitmap(bmBright);

            tv2.setText("Bright");
        }

        else if(compare == 2) {
            Mat mFAST = new Mat();
            Imgproc.cvtColor(m, mFAST, Imgproc.COLOR_RGBA2RGB);
            MatOfKeyPoint keypoints = new MatOfKeyPoint();
            FeatureDetector detector = FeatureDetector.create(FeatureDetector.FAST);
            detector.detect(mFAST, keypoints);
            Features2d.drawKeypoints(mFAST, keypoints, mFAST);

            Bitmap bmFAST = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mFAST, bmFAST);
            iv1.setImageBitmap(bmFAST);
            tv1.setText("FAST");


            Mat mORB = new Mat();
            Imgproc.cvtColor(m, mORB, Imgproc.COLOR_RGBA2RGB);
            MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
            FeatureDetector detector1 = FeatureDetector.create(FeatureDetector.ORB);
            detector1.detect(mORB, keypoints1);
            Features2d.drawKeypoints(mORB, keypoints1, mORB);
            Bitmap bmORB = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mORB, bmORB);
            iv2.setImageBitmap(bmORB);
            tv2.setText("ORB");
        }

        else if(compare == 3) {
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);
            iv1.setImageBitmap(bm);
            tv1.setText("Normal");

            Mat mCanny = new Mat(m.size(), CvType.CV_8UC1);
            Imgproc.cvtColor(m, mCanny, Imgproc.COLOR_RGBA2GRAY);
            Imgproc.Canny(mCanny, mCanny, 50, 150);
            Bitmap bmCanny = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mCanny, bmCanny);
            iv2.setImageBitmap(bmCanny);
            tv2.setText("Canny");
        }
    }
}
