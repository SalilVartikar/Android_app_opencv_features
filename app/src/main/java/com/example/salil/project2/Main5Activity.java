package com.example.salil.project2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main5Activity extends Main2Activity {

    /*Declaring variables*/
    ImageView iv1;
    ImageView iv2;
    TextView tv1;
    TextView tv2;
    public int compare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        iv1 = (ImageView) findViewById(R.id.img1);
        iv2 = (ImageView) findViewById(R.id.img2);
        tv1 = (TextView) findViewById(R.id.text1);
        tv2 = (TextView) findViewById(R.id.text2);

        /*Declaring buttons*/
        Button nFast = (Button) findViewById(R.id.normalFast);
        Button nOrb = (Button) findViewById(R.id.normalOrb);
        Button kp = (Button) findViewById(R.id.fastOrb);
        Button help = (Button) findViewById(R.id.help);

        /*Select comparison of Normal image and FAST keypoint detection*/
        nFast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 1;
                displayImages();
            }
        });

        /*Select comparison of Normal image and ORB keypoint detection*/
        nOrb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 2;
                displayImages();
            }
        });

        /*Select comparison of FAST vs ORB*/
        kp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 3;
                displayImages();
            }
        });

        /*Display help for keypoint detection*/
        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(Main5Activity.this,
                        Main6Activity.class);
                i.putExtra("fromActivity", 5);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayImages() {

        /*Read image from memory*/
        Mat m = Imgcodecs.imread("/storage/emulated/0/Images/image.jpg");
        Imgproc.cvtColor(m, m, Imgproc.COLOR_BGR2RGB);

        /*Display normal vs FAST*/
        if(compare == 1) {
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);

            iv1.setImageBitmap(bm);
            tv1.setText("Normal");

            Mat mFast = fastKp(m);
            Bitmap bmFast = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mFast, bmFast);
            iv2.setImageBitmap(bmFast);
            tv2.setText("Fast");
        }

        /*Display normal vs ORB*/
        else if(compare == 2) {
            Bitmap bm = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(m, bm);

            iv1.setImageBitmap(bm);
            tv1.setText("Normal");


            Mat mOrb = orbKp(m);
            Bitmap bmOrb = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mOrb, bmOrb);
            iv2.setImageBitmap(bmOrb);
            tv2.setText("Orb");
        }

        /*Display FAST vs ORB*/
        else if(compare == 3) {
            Mat mFast = fastKp(m);
            Bitmap bmFast = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mFast, bmFast);
            iv1.setImageBitmap(bmFast);
            tv1.setText("Fast");

            Mat mOrb = orbKp(m);
            Bitmap bmOrb = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mOrb, bmOrb);
            iv2.setImageBitmap(bmOrb);
            tv2.setText("Orb");
        }
    }
}
