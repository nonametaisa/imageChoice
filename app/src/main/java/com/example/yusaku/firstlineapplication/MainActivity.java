package com.example.yusaku.firstlineapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.io.InputStream;

//問題点　比較される画像より検出したい画像が大きい（縦or横）の場合アプリが落ちる


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_GALLERY = 0;
    private ImageView imgView;
    private Button mButton;
    private Button mTM1Button;
    private Button mTM2Button;
    private Button mResultButton;
    private Bitmap mImageHough;
    private Bitmap mImageTM1;
    private Bitmap mImageTM2;

    private int mFlagButton; //どのボタンが押されたか判定する
    private Context mWakewakame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView)findViewById(R.id.imageView);
        mButton = (Button) findViewById(R.id.button);
        mTM1Button =(Button) findViewById(R.id.TM1button);
        mTM2Button = (Button)findViewById(R.id.TM2button);
        mResultButton = (Button)findViewById(R.id.resultbutton);

        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ギャラリー呼び出し
                mFlagButton = 0;

                startActivityForResult(intent, REQUEST_GALLERY);


            }
        });
        mTM1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ギャラリー呼び出し
                // 一回以上結果を出したときに通る
                if (mFlagButton ==2 || mFlagButton ==3){
                    mFlagButton =3;

                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_GALLERY);
                //はじめに通る分岐点
                }else {
                    mFlagButton = 1;

                    startActivityForResult(intent, REQUEST_GALLERY);
                }

            }
        });
        mWakewakame = this;

        mTM2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ギャラリー呼び出し
                if(mFlagButton ==1 || mFlagButton ==2 || mFlagButton ==3) {
                    mFlagButton = 2;


                    startActivityForResult(intent, REQUEST_GALLERY);
                }else {
                    Toast.makeText(mWakewakame,"先にTM1を選択してください",Toast.LENGTH_SHORT).show();

                }


            }
        });


        mResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ギャラリー呼び出し
                if(mFlagButton ==2 || mFlagButton ==3) {
                    templateMattching();
                }else {
                    Toast.makeText(mWakewakame,"先に画像、比較画像を選択してください",Toast.LENGTH_SHORT).show();

                }


            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OK", "OpenCV loaded successfully");
                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }};

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                if (mFlagButton ==0){

                    Mat src = new Mat();
                    Utils.bitmapToMat(img, src);
                    Mat bined = new OpenCVUse().serchLine(src);
                    Bitmap dst = Bitmap.createBitmap(bined.width(), bined.height(), Bitmap.Config.ARGB_8888); // 1ピクセル4バイト（、、各8ビット）
                    Utils.matToBitmap(bined, dst);
                    // 選択した画像を表示
                    imgView.setImageBitmap(dst);

                }else if(mFlagButton ==1 || mFlagButton ==3){
                    mImageTM1 = img;

                }else if (mFlagButton ==2){
                    mImageTM2 =img;

                }


                } catch (Exception e) {

                }
            }
        }

    private void templateMattching(){

        Mat conparedImage = new Mat();
        Mat conpareImage = new Mat();

        Utils.bitmapToMat(mImageTM1,conparedImage);
        Utils.bitmapToMat(mImageTM2 , conpareImage);
        Mat result = new TmatchClass().matchFile(conparedImage ,conpareImage);
        Bitmap dst = Bitmap.createBitmap(result.width(),result.height(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result,dst);

        imgView.setImageBitmap(dst);



    }
}
