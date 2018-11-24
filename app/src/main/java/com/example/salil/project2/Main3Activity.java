package com.example.salil.project2;

import android.content.Intent;
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

public class Main3Activity extends Main2Activity {
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;
    public int compare;


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
                Intent myInt = new Intent(Main3Activity.this,
                        Main4Activity.class);
                startActivity(myInt);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
            Mat mFAST = fastKp(m);

            Bitmap bmFAST = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mFAST, bmFAST);
            iv1.setImageBitmap(bmFAST);
            tv1.setText("FAST");


            Mat mORB = orbKp(m);
            Bitmap bmORB = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mORB, bmORB);
            iv2.setImageBitmap(bmORB);
            tv2.setText("ORB");
        }

        else if(compare == 3) {
//            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(m, bm);
//            iv1.setImageBitmap(bm);
//            tv1.setText("Normal");

            Mat sobel = sobel(m);
            Bitmap bm = Bitmap.createBitmap(sobel.cols(), sobel.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(sobel, bm);
            iv1.setImageBitmap(bm);
            tv1.setText("Normal");

            Mat mCanny = canny(m);
            Bitmap bmCanny = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mCanny, bmCanny);
            iv2.setImageBitmap(bmCanny);
            tv2.setText("Canny");
        }
    }
}
