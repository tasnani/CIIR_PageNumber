package pagenumber;
import java.nio.*;

import java.nio.charset.Charset;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Random;

public class predictedAccuracy {
	public void predictedAccuracy(){
	Charset charset = Charset.forName("US-ASCII");
	Path file=Paths.get("/Users/tanasn/Desktop/CIIR_Example2NumberedData.txt");
	Path file4=Paths.get("/Users/tanasn/Desktop/CIIR_actualNumberedData.txt");
	Path file5=Paths.get("/Users/tanasn/Desktop/CIIR_RandomSample2.txt");
	ArrayList<String> truthData=new ArrayList<String>();
	ArrayList<Integer> number=new ArrayList<Integer>();
	ArrayList<Integer> randomIndexes=new ArrayList<Integer>();
	ArrayList<Integer> randomIndexesTest=new ArrayList<Integer>();
	ArrayList<String> linesrandomIndexes= new ArrayList<String>();
	Random rand=new Random();
		for(int i=0;i<200;i++){
			randomIndexesTest.add(i);
			
			//System.out.println(randomIndexes.get(i));
		}
		Collections.shuffle(randomIndexesTest);
		for(int i=0;i<randomIndexesTest.size()/2;i++){
			randomIndexes.add(randomIndexesTest.get(i));
		}
		
			
		
	   Collections.sort(randomIndexes);
		

		
		
		for(int i=0;i<randomIndexes.size();i++){
			String line=null;
			
	try(BufferedReader reader =Files.newBufferedReader(file4,charset)){
		 while((line=reader.readLine())!= null)	{
	        //System.out.println("I entered");
			if(randomIndexes.get(i)<10){
			if(line.substring(0,1).trim().equals(randomIndexes.get(i).toString())){
				System.out.println(randomIndexes.get(i)+" "+ line);
				linesrandomIndexes.add("0"+line.substring(1,line.length()));
				break;
			}
			}else if(randomIndexes.get(i)>=10 && randomIndexes.get(i)<100){
				if(line.substring(0,2).trim().equals(randomIndexes.get(i).toString())){
					System.out.println(randomIndexes.get(i)+" "+line);	
				linesrandomIndexes.add("0"+line.substring(2,line.length()));
				break;
				}
			}else if(randomIndexes.get(i)>=100){
				if(line.substring(0,3).trim().equals(randomIndexes.get(i).toString())){
					System.out.println(randomIndexes.get(i)+" "+line);
				linesrandomIndexes.add("0"+line.substring(3,line.length()));
				break;
				}
			}
			}
		 
			
	}catch(IOException y){
		System.err.format("IOException: %s%n",y);
	}
	}

	try(BufferedWriter writer=Files.newBufferedWriter(file5,charset)){
		for(int i=0;i<linesrandomIndexes.size();i++){
		writer.write(linesrandomIndexes.get(i).toString(),0,linesrandomIndexes.get(i).toString().length());
		writer.write('\n');
		}
		
	}catch (IOException m) {
	    System.err.format("IOException: %s%n", m);
	}
	
	try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
	    String line = null;
	    String s="";
	  
	    while ((line = reader.readLine()) != null) {
	        for(int i=0;i<line.length();i++){
	        	if(line.charAt(i)==':'){
	        		s=line.substring(0,i-2);
	        		s.trim();
	        		number.add(Integer.parseInt(s));
	        		break;
	        	}
	        }
	    }
	} catch (IOException x) {
	    System.err.format("IOException: %s%n", x);
	}
	
	
	
   Path file2=Paths.get("/Users/tanasn/Desktop/CIIR_TruthData.txt");
    try(BufferedReader reader2=Files.newBufferedReader(file2, charset)){
    	String line=null;
    	String s="";
    	String prev="";
    	int c=0;
    	int lineCounter=0;
    	
    	
    	while((line=reader2.readLine())!=null){
    	 
    		    if(c<randomIndexes.size()){
    			if(lineCounter==randomIndexes.get(c)){
    				       
    		                s=line.substring(0,3);
    						s.trim();
    						truthData.add(s);
    						System.out.println(s);
    						System.out.println(randomIndexes.get(c));
    						 c++;
    						
    		
    		    }
    			lineCounter++;
    			
    			
    		    }
    			
    			
    			
    	}	
    }catch(IOException e){
    	 System.err.format("IOException: %s%n", e);
    }
   
  
   
    int c2=0;
    int right=0;
    String line=null;
    
    Path file3=Paths.get("/Users/tanasn/Desktop/CIIR_RandomSample2Output.txt");
    try(BufferedReader reader3 = Files.newBufferedReader(file3,charset)){
    	while((line=reader3.readLine())!=null && c2<truthData.size()){
    		//System.out.println(truthData.get(c2));
    		//System.out.println(line);
    	if(truthData.get(c2).charAt(0)=='-' && line.charAt(0)=='-'  || truthData.get(c2).charAt(0)=='+' && Character.isDigit(line.charAt(0))==true){
    		right++;
    		//System.out.println("right");
    	}else {
    		//System.out.println("wrong");
    		}
    	c2++;
    	}
    }catch(IOException i){
    	System.err.format("IOException: %s%n", i);
    }
   
    double pctgsright=(double)right/truthData.size();
    System.out.println("Accuracy:"+(Math.ceil(pctgsright*100))+ " %");
    
	
	}
	public static void main(String[] args){
		predictedAccuracy pA=new predictedAccuracy();
		pA.predictedAccuracy();
	}

}
