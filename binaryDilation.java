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
		Mat binaryMat=grayscaleMat.clone();
		Imgproc.threshold(grayscaleMat, binaryMat, 50, 255, Imgproc.THRESH_BINARY);
		
		final List<MatOfPoint> contours = new ArrayList<>();
	    final Mat hierarchy = new Mat();
	    Imgproc.findContours(binaryMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat blueMat=mat.clone();
       
        for(int i=0;i<contours.size();i++){
        	Imgproc.drawContours(blueMat, contours, i, new Scalar(0, 255,0 ), -1);
        	
        }
        Mat grayscaleMat2=new Mat();
        
        
        
        
        Imgproc.cvtColor(blueMat,grayscaleMat2,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(grayscaleMat2, binaryMat, 35, 255, Imgproc.THRESH_BINARY);
   	    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
   	    Imgproc.dilate(binaryMat, binaryMat, dilateElement);
		
		
         
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".PNG", binaryMat, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage outputImage = ImageIO.read(in);
	    return outputImage;
	   

		
		
	}

}
