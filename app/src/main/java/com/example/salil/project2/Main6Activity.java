package com.example.salil.project2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main6Activity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        tv = (TextView) findViewById(R.id.text);

        int from = getIntent().getIntExtra("fromActivity", 0);

        if(from == 4)
            tv.setText("Canny Edge Detector\n" +
                    "Canny Edge Detection is a popular edge detection algorithm. It is a image processing method used to detect edges in an image while suppressing noise. It takes as input a gray scale image, and produces as output an image showing the positions of tracked intensity discontinuities.\n" +
                    "\n" +
                    "For more information, visit :\n" +
                    "https://docs.opencv.org/3.1.0/da/d22/tutorial_py_canny.html\n" +
                    "\n" +
                    "\n" +
                    "Sobel Edge Detector\n" +
                    "The Sobel operator performs a 2-D spatial gradient measurement on an image and so emphasizes regions of high spatial frequency that correspond to edges. It is a gradient based method.\n" +
                    "\n" +
                    "For more information, visit :\n" +
                    "https://docs.opencv.org/2.4/doc/tutorials/imgproc/imgtrans/sobel_derivatives/sobel_derivatives.html");
        else if(from == 5)
            tv.setText("FAST(Features from Accelerated Segment Test)\n" +
                    "FAST can be computed very quickly. It is a corner detection method, which could be used to extract feature points and later used to track and map objects in many computer vision tasks. Great for real-time visual tracking.\n" +
                    "\n" +
                    "For more information, visit :\n" +
                    "https://docs.opencv.org/3.0-beta/doc/py_tutorials/py_feature2d/py_fast/py_fast.html\n" +
                    "\n" +
                    "\n" +
                    "ORB(Oriented FAST and rotated BRIEF)\n" +
                    "ORB is a fusion of FAST(Features from Accelerated Segment Test) key point detetctor and BRIEF(Binary Robust Independent Elementary Features) descriptor with some modifications. Moreover, FAST doesn't compute the orientation and is rotation variant. In ORB, a rotation matrix is computed using orientation of patch and then BRIEF descriptors are steered according to the orientation.\n" +
                    "\n" +
                    "For more information, visit :\n" +
                    "https://docs.opencv.org/3.0-beta/doc/py_tutorials/py_feature2d/py_orb/py_orb.html");
    }
}
