package pagenumber;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class connectedComponent {
	int[][]  imageArray;
	int[] imageArrayN;
	int trueImageWidth=0;
	int trueImageHeight=0;
	boolean b=true;
	public int[] loadImage(BufferedImage im){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println(im.getType());
		ArrayList<Integer> temp=new ArrayList<>();
		trueImageWidth=im.getWidth();
		trueImageHeight=im.getHeight();
		System.out.println("trueImageWidth:"+trueImageWidth);
		System.out.println("trueImageHeight:"+trueImageHeight);
		
		imageArray=new int[im.getWidth()][im.getHeight()];
		for(int x=0;x<im.getWidth();x++){
			for(int y=0;y<im.getHeight();y++ ){
				imageArray[x][y]=im.getRGB(x, y);
				temp.add(imageArray[x][y]);
			}
		}
		imageArrayN=new int[temp.size()];
		for(int i=0;i<temp.size();i++){
			imageArrayN[i]=temp.get(i);
			
		}
		
	  Dimension d=new Dimension(im.getWidth(),im.getHeight());
	 
		return	 cLabeling(imageArrayN,d,b);
		
	}
		
		int maxLabels=80000;
		int nextLabel=1;
		int[] label;
		//background value should be 0, because the background is black and the components are in white, so it should be true
		
		public int[] cLabeling(int[] ia, Dimension d ,boolean background){
			label=labeling(ia,d,background);
			int stat[]=new int[nextLabel+1];
			for (int i=0;i<imageArrayN.length;i++) {
				
	            if (label[i]-1>nextLabel)
	                System.err.println("bigger label than next_label found!");
	            stat[label[i]]++;
	        }
			
			stat[0]=0;
			int j=1;
			 for (int i=1; i<stat.length; i++) {
		            if (stat[i]!=0) stat[i]=j++;
		        }
			 System.out.println("From "+nextLabel+"to"+(j-1)+"regions");
			  
		        nextLabel= j-1;
		        for (int i=0;i<imageArrayN.length;i++) label[i]= stat[label[i]];
		        return label;
		    }
		
		public int maxLabel(){
			return nextLabel;
		}
		
		int[] labels;
		
		public int[] labeling(int[] ia,Dimension d, boolean background ){
			int w=(int)d.getWidth();
			int h=(int)d.getHeight();
			int[] rst=new int[w*h];
			labels=new int[maxLabels];
			int[] parent=new int[maxLabels];
			int nextRegion=1;
			for(int k=0;k<h;++k){
				for(int j=0;j<w;++j){
					if(imageArrayN[k*w+j]==0 && background) 
						continue;
					
					int m=0;
					boolean connected=false;
					if(j>0 && imageArrayN[k*w+j-1]==imageArrayN[k*w+j]){
						m=rst[k*w+j-1];
						connected=true;
					}
					
					if(k>0 && imageArrayN[(k-1)*w+j]==imageArrayN[k*w+j]&& (connected =false || imageArrayN[ (k-1)*w+j]< m)){
						m=rst[(k-1)*w+j];
						connected=true;
					}
					if(!connected){
						m=nextRegion;
						nextRegion++;
					}
					if(m>=maxLabels){
						System.err.println("maximum number of labels reached");
						System.exit(1);
					}
					rst[j*w+k]=m;
					if(j>0 && imageArrayN[k*w+j-1]==imageArrayN[k*w+j]&& rst[k*w+j-1]!=m)
						uf_union(m,rst[k*w+j-1],parent);
						if(k>0 && imageArrayN[(k-1)*w+j]==imageArrayN[k*w+j]&& rst[(k-1)*w+j]!=m)
							uf_union(m,rst[(k-1)*w+j],parent);
					}
				}
				nextLabel=1;
				for(int i=0;i<w*h;i++){
					if(imageArrayN[i]!=0 || !background){
						rst[i]=uf_find(rst[i],parent,labels);
						if(!background)rst[i]--;
					}
				}
				nextLabel--;
				if(!background)nextLabel--;
						System.out.println(nextLabel+"regions");
					
			
			
			
			return rst;	
			
			
		}
			public void uf_union(int x, int y, int[] parent){
				while(parent[x]>0)
					x=parent[x];
				while(parent[y]>0)
					y=parent[y];
				if(x!=y){
					if(x<y)
						parent[x]=y;
					else parent[y] =x;
				}
			}
			 
			
		public int	uf_find(int x,int[] parent, int[] label){
				while(parent[x]>0)
					x=parent[x];
					if(label[x]==0)
						label[x]=nextLabel++;
					return label[x];
				
				
					
					
				}
		//convert rst[] back to BufferedImage
			public BufferedImage converttoBufferedImage(int[] outputArray){
				for(int i=0;i<outputArray.length;i++){
					System.out.print(outputArray[i]);
				}
				//int[] newOutputArray=Arrays.copyOf(outputArray,outputArray.length);
			BufferedImage output = new BufferedImage(trueImageWidth,trueImageHeight,BufferedImage.TYPE_BYTE_GRAY);
			
				WritableRaster raster = (WritableRaster) output.getData();
				raster.setPixels(0,0,trueImageWidth,trueImageHeight,outputArray);
				output.setData(raster);
				return output;
				
			}
		}
		

	
	
	


