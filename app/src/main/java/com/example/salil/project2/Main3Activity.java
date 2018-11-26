package com.example.salil.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

    /*Declaring variables*/
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        iv1 = (ImageView) findViewById(R.id.img1);
        iv2 = (ImageView) findViewById(R.id.img2);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);

        /*Declare buttons*/
        Button keypoints = (Button) findViewById(R.id.Keypoints);
        Button brightness = (Button) findViewById(R.id.Brightness);
        Button edge = (Button) findViewById(R.id.Edge);
        Button mirror = (Button) findViewById(R.id.Mirror);

        /*Brighness enhancement*/
        brightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                displayImages(1);
            }
        });

        /*Flip image horizontally*/
        mirror.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                displayImages(2);
            }
        });

        /*Go to keypoint detection activity*/
        keypoints.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myInt = new Intent(Main3Activity.this,
                        Main5Activity.class);
                startActivity(myInt);
            }
        });

        /*Go to edge detection activity*/
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

    /*Function to display images in the ImageViews*/
    public void displayImages(int x) {

        /*Reading image from storage*/
        Mat m = Imgcodecs.imread("/storage/emulated/0/Images/image.jpg");
        Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2RGB);

        Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(m, bm);

        iv1.setImageBitmap(bm);
        tv1.setText("Normal");

        /*Brightness Enhancement*/
        if(x == 1) {
            Mat mBright = new Mat();
            try {
                m.convertTo(mBright, -1, 1, 75);
            } catch (Exception e) {
                System.out.println(e);
            }

            Bitmap bmBright = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mBright, bmBright);

            iv2.setImageBitmap(bmBright);
            tv2.setText("Bright");
        }

        /*Horizontally flipping the image*/
        else if(x == 2){
            Bitmap bmBright = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bmBright);

            Bitmap bOutput;
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            bOutput = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

            iv2.setImageBitmap(bOutput);
            tv2.setText("Mirror image");
        }
    }
}
