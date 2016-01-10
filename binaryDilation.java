package pagenumber;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;




import javax.imageio.ImageIO;




public class binaryDilation {

//int smallestContourLoc=0;
Mat binaryMat;
Mat blueMat;
final List<MatOfPoint> contours = new ArrayList<>();
	public BufferedImage dilate(BufferedImage bi) throws IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BufferedImage convertImage=new BufferedImage(bi.getWidth(),bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2 = convertImage.createGraphics();
        g2.drawRenderedImage(bi, null);
        g2.dispose();
        byte[] data = ((DataBufferByte) convertImage.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(convertImage.getHeight(), convertImage.getWidth(), CvType.CV_8UC3);
		mat.put(0,0 , data);
		Mat grayscaleMat=mat.clone();
		Imgproc.cvtColor(mat, grayscaleMat, Imgproc.COLOR_BGR2GRAY);
		 binaryMat=grayscaleMat.clone();
		Imgproc.threshold(grayscaleMat, binaryMat, 70, 255, Imgproc.THRESH_BINARY);
		
		
	    final Mat hierarchy = new Mat();
	    Imgproc.findContours(binaryMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
	    //double smallestcontourArea=Double.MAX_VALUE;
	    //int smallestContourLoc=0;
        blueMat=mat.clone();
        boolean oriented=false;
       // System.out.println("double max value"+smallestcontourArea);
       
        for(int i=0;i<contours.size();i++){
        	Imgproc.drawContours(blueMat, contours, i, new Scalar(0, 255,0 ), -1);
        	
        	//if( Imgproc.contourArea(contours.get(i),oriented)<smallestcontourArea && Imgproc.contourArea(contours.get(i),oriented)!=0.0 ){
        		//smallestcontourArea=Imgproc.contourArea(contours.get(i));
        		//smallestContourLoc=i;
        		
        		
        	//}
        }
        
        
        //System.out.println("smallest contour area:"+smallestcontourArea);
       // System.out.println("smallestContourLoc:"+smallestContourLoc);
       
        Mat grayscaleMat2=new Mat();
       
   
        
        
        Imgproc.cvtColor(blueMat,grayscaleMat2,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(grayscaleMat2, binaryMat, 35, 255, Imgproc.THRESH_BINARY);
   	    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5
   	    		));
   	    Imgproc.dilate(binaryMat, binaryMat, dilateElement);
   	    
		
		
		//Imgproc.drawContours(blueMat, contours, smallestContourLoc, new Scalar(0, 0,255 ), -1);
    	
         
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".PNG", binaryMat, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage outputImage = ImageIO.read(in);
	    return outputImage;
	   

		
		
	}
	/*
	public Mat gotofindsmallBlob(){
		findsmallBlob fsB=new findsmallBlob();
		blueMat=fsB.blobCheck(blueMat, contours, smallestContourLoc);
		return blueMat;
		
	}
	*/
	

}
