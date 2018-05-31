package com.projecttango.tangoapp;

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

import static org.opencv.core.Core.BORDER_DEFAULT;


public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private final int VIDEO_REQUEST_CODE=100;
    public Button but1;
    ImageView imageView;

    public void init() {
        but1 = (Button) findViewById(R.id.button6);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toy= new Intent(MainActivity.this,Main2Activity.class);
                startActivity(toy);
            }
        });
    }
    public void init2() {
        but1 = (Button) findViewById(R.id.button7);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toy= new Intent(MainActivity.this,Main3Activity.class);
                startActivity(toy);
            }
        });
    }

    private  static final String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRgba ,imgGray, imgCanny,imgSobel, imgthresh,imgblob;
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
        init();
        init2();

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



    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        imgGray = new Mat(height, width, CvType.CV_8UC1);
        imgCanny = new Mat(height, width, CvType.CV_8UC1);
        imgSobel= new Mat(height,width,CvType.CV_8UC1);
        imgthresh= new Mat(height,width,CvType.CV_8UC1);
        imgblob= new Mat(height,width,CvType.CV_8UC1);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();

    }

    public void captureVideo(View view)
  {

        Imgproc.cvtColor(mRgba,imgGray, Imgproc.COLOR_RGB2GRAY);
        Intent camera_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File video_file= getFilepath();
        Uri video_uri = Uri.fromFile(video_file);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,video_uri);
        camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(camera_intent,VIDEO_REQUEST_CODE);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "video successfully recorded", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "video capture false", Toast.LENGTH_SHORT).show();
            }
        }
    }
public File getFilepath() {
        Imgproc.cvtColor(mRgba,imgGray, Imgproc.COLOR_RGB2GRAY);
    File folder = new File("sdcard/video_app");
    if (!folder.exists()) {
        folder.mkdir();
    }
    File video_file = new File(folder, "sample_video.mp4");
    return video_file;





}






        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba=inputFrame.rgba();
        Imgproc.cvtColor(mRgba,imgGray, Imgproc.COLOR_RGB2GRAY);
        Button button1= (Button) findViewById(R.id.button1);
       /* Button button2= (Button) findViewById(R.id.button2);
        Button button3= (Button) findViewById(R.id.button3);
        Button button4=(Button) findViewById(R.id.button4);
       // Button button5=(Button) findViewById(R.id.button5); */



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return imgGray;



        /*
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Imgproc.Sobel(imgGray, imgSobel, -1,0,1,3,1,0,BORDER_DEFAULT);
               // return imgSobel;
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imgproc.threshold(imgGray,imgthresh, 127, 255, Imgproc.THRESH_BINARY); //threshold
                //return imgthresh;
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Imgproc.blob(imgGray,imgblob, 127, 255, Imgproc.COLOR_RGB2GRAY); //blob
                //return blob;
            }
        });  */

        //Imgproc.Sobel(imgGray, imgSobel, -1,0,1,3,1,0,BORDER_DEFAULT);

        //if (button1.isClickable()){
      /*  if (button1.isPressed()){
            return imgGray;
        }
        else if (button2.isPressed()){
            return imgSobel;
        }
        else if (button3.isPressed()) {
            return imgthresh;
        }
        else if (button4.isPressed()) {
            return imgthresh;
        }

        else {
            return mRgba;
        }*/
    }
}