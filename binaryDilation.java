package pagenumber;
import org.opencv.imgproc.Imgproc;

import org.opencv.utils.Converters;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
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
import java.util.Random;
import java.util.Vector;
import java.awt.image.BufferedImage;





import javax.imageio.ImageIO;




public class binaryDilation {

int smallestContourLoc=0;
Mat binaryMat;


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
		
		// Mat threshold_output; use binaryMat
		  
		final List<MatOfPoint> contours = new ArrayList<>();
		//List<MatOfPoint2f> contours2f=new ArrayList<>();
		//List<MatOfPoint2f> contoursPoly=new ArrayList<>(contours.size());
		//List<MatOfPoint> contoursPolyPoint=new ArrayList<>(contours.size());
		//List<Rect> boundRect=new ArrayList<>(contours.size());
		//List<Point>center=new ArrayList<>(contours.size());
		
		
		
		//List<Float>radius=new ArrayList<>(contours.size());
		float[] radius=new float[contours.size()];
		
		
	    final Mat hierarchy = new Mat();
	    Imgproc.findContours(binaryMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
	    //double smallestcontourArea=Double.MAX_VALUE;
	    //int smallestContourLoc=0;
       Mat blueMat=mat.clone();
        //boolean oriented=false;
       
       MatOfPoint2f approxCurve=new MatOfPoint2f();
        for(int i=0;i<contours.size();i++){
        	Imgproc.drawContours(blueMat, contours, i, new Scalar(0,255,0),1);
        	/*
        	MatOfPoint2f contour2f =new MatOfPoint2f(contours.get(i).toArray());
        	double approxDistance =Imgproc.arcLength(contour2f,true)*0.02;3
        	Imgproc.approxPolyDP(contour2f, approxCurve,approxDistance, true);
        	MatOfPoint points=new MatOfPoint(approxCurve.toArray());
        	Rect rect=Imgproc.boundingRect(points);
        	Core.rectangle(blueMat, new Point(rect.x, rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255,0,0),3);
        	*/
        	
        	//if( Imgproc.contourArea(contours.get(i),oriented)<smallestcontourArea && Imgproc.contourArea(contours.get(i),oriented)!=0.0 ){
        		//smallestcontourArea=Imgproc.contourArea(contours.get(i));
        		//smallestContourLoc=i;
        		
        		
        	//}
        }
        
      
       
        Mat grayscaleMat2=new Mat();
       
   
        
        
        Imgproc.cvtColor(blueMat,grayscaleMat2,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(grayscaleMat2, binaryMat, 35, 255, Imgproc.THRESH_BINARY);
   	    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6,6
   	    		));
   	    Imgproc.dilate(binaryMat, binaryMat, dilateElement);
   	    
//System.out.println("contour list size:"+contours.size());
		
		//Imgproc.drawContours(blueMat, contours, 530, new Scalar(0, 0,255 ), -1); 
    	
         
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
