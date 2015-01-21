package com.example.yusaku.firstlineapplication;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Yusaku on 2015/01/14.
 */
public class OpenCVUse {

    private int logGet = 0;

    public Mat serchLine(Mat src ){
        Mat edge   = new Mat();
        Mat lines = new Mat();

        double[] data;
        double rho, theta;
        Point pt1 = new Point();
        Point pt2 = new Point();
        double x , y1  = 0 , y2 = 999;
        double a,b;

        Imgproc.cvtColor(src , edge, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(edge,edge,new Size(5,3),8,6);
        Imgproc.Canny(edge, edge, 80, 100);
        Imgproc.HoughLinesP(edge, lines, 1, Math.PI / 180 , 100, 100 ,10);

        for (int i = 0; i < lines.cols(); i++){
            data = lines.get(0, i);
/*            pt1.x = data[0];
            pt1.y = data[1];
            pt2.x = data[2];
            pt2.y = data[3]; */

            a = (data[1] - data[3]) / (data[0] - data[2]);
            b = data[1] - (a * data[0]);

            pt1.y = y1;
            pt2.y = y2;

            pt1.x = (y1 - b) / a;
            pt2.x = (y2 - b) / a;







            Core.line(src, pt1, pt2, new Scalar(255, 0, 0), 1);
        }



/*        Imgproc.HoughLines(edge, lines, 1, Math.PI/180,150 );//最後の引数以上に表が入ったもののみ出力される
        for (int i = 0; i < lines.cols(); i++){
            data = lines.get(0, i);
            rho = data[0];
            theta = data[1];


   /*             if(theta  > 0.6 && theta < 1.5) {
                    double cosTheta = Math.cos(theta);
                    double sinTheta = Math.sin(theta);
                    double x = cosTheta * rho;
                    double y = sinTheta * rho;
                    Point pta = new Point(x + 10000 * (-sinTheta), y + 10000 * cosTheta);
                    Point ptb = new Point(x - 10000 * (-sinTheta), y - 10000 * cosTheta);
                    String ptast = String.valueOf(pta);
                    String ptbst = String.valueOf(ptb);

                    Log.d("wawawawa = ", ptast);

                    Core.line(src, pta, ptb, new Scalar(255, 0, 0), 1);
                }
   */
                /*else if(theta > 1.5 && theta < 2.4){

                    double cosTheta = Math.cos(theta);
                    double sinTheta = Math.sin(theta);
                    double x = cosTheta * rho;
                    double y = sinTheta * rho;
                    Point pta = new Point(x + 10000 * (-sinTheta), y + 10000 * cosTheta);
                    Point ptb = new Point(x - 10000 * (-sinTheta), y - 10000 * cosTheta);
                    String ptast = String.valueOf(theta);
                    String ptbst = String.valueOf(ptb);

                    Log.d("ptptptasttheta = ", ptast);

                    Core.line(src, pta, ptb, new Scalar(255, 0, 0), 1);

            }  */

//        }

/*
        for(int i = 0 ; i < lines.cols() ; i ++){

            data = lines.get(0,i);
            rho  = data[0];
            theta = data[1];
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);

            double x = rho * cosTheta;
            double y = rho * sinTheta;

            pt1.x = cvRound(x + 1000 * (-sinTheta));
            pt1.y = cvRound(y + 1000 * (cosTheta));
            pt2.x = cvRound(x - 1000 * (-sinTheta));
            pt2.y = cvRound(y - 1000 * (cosTheta));
            logGet ++;

            Core.line(src, pt1, pt2, new Scalar(0,0,255), 1);
        }
*/





        return src;



    }

    int cvRound(double x){
        int y;
        if(x >= (int)x+0.5)
        y = (int)x++;
        else
        y = (int)x;
        return y;
    }
}
