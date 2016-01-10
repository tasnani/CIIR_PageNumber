package pagenumber;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class findsmallBlob {
	
	public Mat blobCheck(Mat m,List<MatOfPoint> contours,int smallContourIndex) {
		Imgproc.drawContours(m , contours, smallContourIndex, new Scalar(0,0 ,255 ), -1);
		System.out.println(smallContourIndex);
	return m;
	}

}