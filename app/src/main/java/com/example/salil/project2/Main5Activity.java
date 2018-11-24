package com.example.salil.project2;

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

        Button nFast = (Button) findViewById(R.id.normalFast);
        Button nOrb = (Button) findViewById(R.id.normalOrb);
        Button kp = (Button) findViewById(R.id.fastOrb);

        nFast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 1;
                displayImages();
            }
        });

        nOrb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 2;
                displayImages();
            }
        });

        kp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                compare = 3;
                displayImages();
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

            Mat mFast = fastKp(m);
            Bitmap bmFast = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mFast, bmFast);
            iv2.setImageBitmap(bmFast);
            tv2.setText("Fast");
        }

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
