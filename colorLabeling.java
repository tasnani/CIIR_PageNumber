package pagenumber;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class colorLabeling {
	
	public BufferedImage addcolorLabels(BufferedImage im,int[][] imageArrayN, int[] outputArray, int width, int height, int maxLabels){
	
	   
	    ArrayList<Integer> mostLocation=new ArrayList<>();
	    //System.out.println("length of output array:"+outputArray.length);
	    int largestLabel=0;
	    
	    for(int g=0;g<outputArray.length;g++){
	    	
	    	if(outputArray[g]>largestLabel){
	    		largestLabel=outputArray[g];
	    	}
	    }
	   
	    int[] labelCount=new int[largestLabel+1];
	    for(int k=0;k<outputArray.length;k++){
	    	labelCount[outputArray[k]]++;
	    }
	    int mostPixels=0;
	    int mostLabel=0;
	    for(int k=1;k<labelCount.length;k++){
	    	//System.out.println("For label "+k+" : "+labelCount[k]+"pixels.");
	    	if(labelCount[k]>mostPixels){
	    		
	    		mostPixels=labelCount[k];
	    		mostLabel=k;
	    		
	    	}
	    	
	    }
	    //System.out.println("size of label count:"+labelCount.length);
	    //System.out.println("most label:"+mostLabel);
	    //System.out.println("number of pixels in the most occuring label:"+mostPixels);
	    
	    for(int h=0;h<outputArray.length;h++){
	    	if(outputArray[h]==mostLabel ){
	    		mostLocation.add(h);
	    		
	    			//System.out.println("Location:"+h);	
	    	
	    		
	    	}
	    }
	    /*
	    
for(int y=0;y<mostLocation.size();y++){
	int row = (int) Math.floor((y+width-1)/height);
	//System.out.println("X coordiate of pixel in the image:"+row);
	int column=(int)y - height*row;
	//System.out.println("Y coordinate of pixel in the image:"+column);
}
*/

BufferedImage newIM=new BufferedImage(im.getWidth(),im.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
Graphics2D g2 = newIM.createGraphics();
g2.drawRenderedImage(im, null);
g2.dispose();


 
 int singleArrayCounter=0;
 
	    for(int k=0;k<height;k++){
	    	for(int j=0;j<width;j++){
	    		if(singleArrayCounter<width*height) {
	    		for(int listCounter=0;listCounter<mostLocation.size();listCounter++){
	    		if(mostLocation.get(listCounter)==singleArrayCounter ){
	    			//System.out.println("X coordinate of pixel:"+k);
	    			//System.out.println("Y coordinate of pixel"+j);
	    			newIM.setRGB(j, k, new Color(0, 255, 0).getRGB());
	    			
	    		}
	    		
	    		
	    		}
	    		}
	    		singleArrayCounter++;
	    		
	    		
	    	}
	    }
	    
return newIM;
		
	}

}