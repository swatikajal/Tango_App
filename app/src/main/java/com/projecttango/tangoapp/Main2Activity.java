package com.projecttango.tangoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import android.support.v4.app.FragmentActivity;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class Main2Activity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {



    private  static final String TAG = "Main2Activity";
    JavaCameraView javaCameraView;
    Mat mRgba ,imgGray, imgCanny,imgSobel, imgthresh;
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                }
            }
        }
    };

static {

        }
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

        }
@Override
protected void onPause(){
        super.onPause();
        if(javaCameraView!=null)
        javaCameraView.disableView();
        }

@Override
protected void onResume(){
        super.onResume();
        if(!OpenCVLoader.initDebug()){
        Log.d(TAG, "OpenCV  loaded successfully");
        mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
        Log.d(TAG, "OpenCV loaded");
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0,this,mLoaderCallBack);
        }

        }


public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        imgGray = new Mat(height, width, CvType.CV_8UC1);
        imgCanny = new Mat(height, width, CvType.CV_8UC1);
        imgSobel= new Mat(height,width,CvType.CV_8UC1);
        imgthresh= new Mat(height,width,CvType.CV_8UC1);
        }


public void onCameraViewStopped() {
        mRgba.release();

        }
public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {


        mRgba=inputFrame.rgba();
        Imgproc.cvtColor(mRgba,imgGray, Imgproc.COLOR_RGB2GRAY); //grayscale
        //Imgproc.Canny(imgGray,imgCanny,50,150); //edges (more clear)

        Imgproc.Sobel(imgGray, imgSobel, -1,0,1,3,1,0); //edges

        Imgproc.threshold(imgGray,imgthresh, 127, 255, Imgproc.THRESH_BINARY); //threshold

        return imgSobel;
        }
        }