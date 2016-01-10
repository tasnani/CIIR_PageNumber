package pagenumber;

import java.awt.*;






import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.color.*;


public class imageEdgeDetection extends Component{
	 BufferedImage img;
	 static BufferedImage inputSourceImage;
	 JFrame f;
	 JFrame f2;
	 BufferedImage outputSourceImage;
	 BufferedImage i;
	 binaryDilation bD=new binaryDilation(); 
	
	 
	 
	
	
 
	public void showInputImage() throws IOException {
	
		f = new JFrame("Loaded image");
		f.setSize(1000, 1000);
		 f.setBackground(Color.BLACK);
		inputSourceImage=ImageIO.read(new File("/Users/tanasn/Desktop/text1.png"));
      
		f.add(new JLabel(new ImageIcon(inputSourceImage)));
		
	    f.setVisible(true);
			
			
		

	}
	//take buffered image into matrix n then invert it 
	public BufferedImage inversion(BufferedImage image) throws IOException{
		/*
		BufferedImage dumpImage=ImageIO.read(new File("/Users/tanasn/Desktop/dumptext10.png"));
		BufferedImage convertforInverseDumpImage=new BufferedImage(dumpImage.getWidth(), dumpImage.getHeight(),BufferedImage.TYPE_3BYTE_BGR );
		Graphics2D g2 = convertforInverseDumpImage.createGraphics();
        g2.drawRenderedImage(dumpImage, null);
        g2.dispose();
        */
		 //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		BufferedImage convertforInverseImage=new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g1 = convertforInverseImage.createGraphics();
        g1.drawRenderedImage(image, null);
        g1.dispose();
;       
       
		short[] invert = new short[256];
		for (int i = 0; i < 256; i++)
		invert[i] = (short)(255 - i);
		BufferedImageOp invertOp = new LookupOp(
        new ShortLookupTable(0, invert), null);
		BufferedImage invertedImage=invertOp.filter(convertforInverseImage,convertforInverseImage);
		ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		BufferedImage im = op.filter(invertedImage, null);
		
		
		
				 return im;
		
	}
	
	public void writetoOutputFile(BufferedImage im) throws IOException{
		 File outputFile=new File("/Users/tanasn/Desktop/text1result.png");
         ImageIO.write(im,"PNG",outputFile );
          
		
	}
	
	public void displayOutputFile() throws IOException{
		
		
		
		outputSourceImage=ImageIO.read(new File("/Users/tanasn/Desktop/text1result.png"));
	    f2=new JFrame("edited image");
	    f2.setSize(1000,1000);
	    f2.setBackground(Color.BLACK);
		f2.add(new JLabel(new ImageIcon(outputSourceImage)));
		f2.setVisible(true);
	}
	
	public BufferedImage doBinaryDilation(BufferedImage b) throws IOException{
		
		BufferedImage binaryDilatedImage=bD.dilate(b);
		
		return binaryDilatedImage;
		
		
	}
	public static void main(String args[]) throws IOException {
       imageEdgeDetection loadObject=new imageEdgeDetection();
       loadObject.showInputImage();
       loadObject.writetoOutputFile( loadObject.doBinaryDilation(loadObject.inversion(inputSourceImage)));
       loadObject.displayOutputFile();

}
}