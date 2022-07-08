package com.nie.image2code.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import static com.nie.image2code.Misc.Constants.*;

import java.io.File;

public class Imag2GreyScale {
    public boolean convertToGreyScale(String path)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //input file
        Mat src = Imgcodecs.imread(path);

        Mat brightImg = new Mat();
        Mat greyImg = new Mat();
        Mat thresholdImg = new Mat();

        //adjust brightness
        //src.convertTo(brightImg,-1,1,-30);

        //convert to greyscale
        Imgproc.cvtColor(src,greyImg,Imgproc.COLOR_BGR2GRAY);

        //adjust threshold
        //Imgproc.threshold(greyImg,thresholdImg,70,255,Imgproc.THRESH_BINARY);

        Imgcodecs imgcodecs = new Imgcodecs();

        //save modified image
        imgcodecs.imwrite(CV_DESTINATION,greyImg);

        //check if file exist
        File file = new File(CV_DESTINATION);
        if(file.exists())
            return true;
        else
            return false;
    }
}
