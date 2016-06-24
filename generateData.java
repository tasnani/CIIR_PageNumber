package pagenumber;
import pagenumber.ExtractFeaturesBook;
import pagenumber.ExtractFeaturesBook.PageNumberCandidate;

import java.awt.font.LineBreakMeasurer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class generateData {
String line="";
int c=0;

String[] temp=new String[2];
HashMap<String,String> truthDataMap=new HashMap<String,String>();
ArrayList<String> QIDS=new ArrayList<String>();


String token;
int positiveExampleNumber=0;
int negativeExampleNumber=0;
int numberOfCandidates=0;
int QID=1;
int counter=0;

ArrayList<String> truthArray=new ArrayList<String>();
	public void generateTrainingData(String truthData,List<List<PageNumberCandidate>> book) throws IOException{
		
		
		BufferedReader br = new BufferedReader(new FileReader(truthData));
		
				line=br.readLine();     
			while(line!=null)
			{
				StringTokenizer st = new StringTokenizer(line);
				while(st.hasMoreTokens()){
					temp[c]=st.nextToken();
					c++;
				}	
				c=0;
				truthDataMap.put(temp[1].trim(),temp[0].trim());
				line=br.readLine();
			}
		   br.close();
			boolean found=false;
		for(List<PageNumberCandidate> b: book){
			
			for( PageNumberCandidate PNC : b){
				
					
					BufferedReader br2 = new BufferedReader(new FileReader(truthData));
					
					line=br2.readLine();     
					found=false;
				while(line!=null)
				{   
					StringTokenizer st = new StringTokenizer(line);
					while(st.hasMoreTokens()){
						temp[c]=st.nextToken();
						c++;
					}	
					c=0;
					try{
						//System.out.println("Text:"+PNC.text+" "+"Image Number: "+PNC.imageNumber()+"Text from File: "+ temp[0]+"Image number from file:"+temp[1]);
					if(PNC.text.equals(temp[0]) && PNC.imageNumber()==Integer.parseInt(temp[1])){
					//System.out.println(PNC.text+","+PNC.imageNumber());
						found=true;
						break;
					}
					}catch(NullPointerException e){
						found=false;
					}
					
					line=br2.readLine();
				}
			
				
			 
			   
			   
			   if(found){ token="+1";
			   positiveExampleNumber++;
			  // System.out.println(token+" "+"1:"+PNC.features.get("x-fraction")+" "+"2:"+PNC.features.get("y-fraction")+" "+"3:"+PNC.features.get("% thru line")+" "+"4:"+PNC.features.get("% thru page")+" "+"5:"+PNC.features.get("sequence") +" #"+PNC.imageNumber()+","+PNC.text+","+PNC.book());
			   }
			   else {
				negativeExampleNumber++;   
			   token="-1";}
			   //System.out.println("Text:"+PNC.text+" "+"Image number: "+PNC.imageNumber());
			  
			 // System.out.println(token+" "+"1:"+PNC.features.get("x-fraction")+" "+"2:"+PNC.features.get("y-fraction")+" "+"3:"+PNC.features.get("% thru line")+" "+"4:"+PNC.features.get("% thru page")+" "+"5:"+PNC.features.get("sequence") +" #"+PNC.imageNumber()+","+PNC.text+","+PNC.book());
			  truthArray.add(token+" "+"1:"+PNC.features.get("x-fraction")+" "+"2:"+PNC.features.get("y-fraction")+" "+"3:"+PNC.features.get("% thru line")+" "+"4:"+PNC.features.get("% thru page")+" "+"5:"+PNC.features.get("sequence") +" #"+PNC.imageNumber()+","+PNC.text+","+PNC.book());
               	
			}
			
		}
		//System.out.println("Number of positive examples: " +positiveExampleNumber);
		//System.out.println("Number of negative examples: "+negativeExampleNumber);
		
	}
	public void generateTestData(List<List<PageNumberCandidate>> book){
for(List<PageNumberCandidate> b: book){
			
			for( PageNumberCandidate PNC : b){

			System.out.println(0+" "+"1:"+PNC.features.get("x-fraction")+" "+"2:"+PNC.features.get("y-fraction")+" "+"3:"+PNC.features.get("% thru line")+" "+"4:"+PNC.features.get("% thru page")+" "+"5:"+PNC.features.get("sequence") +" #"+PNC.imageNumber()+","+PNC.text+","+PNC.book());
		   
			}
	}
	
	}
	
	public void generateRankData(List<List<PageNumberCandidate>> book){
		int right=0;
	    int wrong=0;
		ArrayList<String> rankArray=new ArrayList<String>();
		
		//ArrayList<String> finalrankArray=new ArrayList<String>();
		for(String s: truthArray){
	    	if(s.charAt(0)=='+'){
	    		s= "2"+s.substring(2,s.length());
	    		right++;
	    		
	    		
	    	}
	    	if(s.charAt(0)=='-'){
	    		s=s.substring(1,s.length());
	    		wrong++;
	    	}
	    	rankArray.add(s);
	    	numberOfCandidates++;
	    	//System.out.println(s);
	    }
		for(int i=0;i<=rankArray.size()-2;i++){
			//String current=rankArray.get(0);
			//System.out.println(QID);
			QIDS.add(QID+"");
			rankArray.set(i, rankArray.get(i).charAt(0)+" qid:"+QID+" "+rankArray.get(i).substring(2,rankArray.get(i).length()));
			if(!rankArray.get(i).substring(rankArray.get(i).
			indexOf('#')+1, rankArray.get(i).indexOf(',')).equals(rankArray.get(i+1).substring(rankArray.get(i+1).
					indexOf('#')+1, rankArray.get(i+1).indexOf(',')))){
				QID++;
			}
			
			//System.out.println(rankArray.get(i));
		}
		
		/*
		for(List<PageNumberCandidate> PNC: book){
			for(int k=0;k<truthArray.size();k++){
				truthArray.set(k, truthArray.get(k).charAt(0) + "qid:"+QID+" "+truthArray.get(k).substring(2,truthArray.get(k).length()));
				System.out.println(truthArray.get(k));
			}
			QID++;
			
		}
		
		*/
		//System.out.println("Number of positive examples: "+positiveExampleNumber);
		//System.out.println("Number of negative examples: "+negativeExampleNumber);
		//System.out.println(finalrankArray.size());
		//System.out.println("Number of positive examples in the test data:"+right);
		truthArray.removeAll(truthArray);
		
	}
	
	public void sortRankPredictions(String f, String f2, String f3) throws IOException{
		//System.out.println("Entered the method");
		int currentPos=0;
		double rightRank=0;
		double wrongRank=0;
	    ArrayList<Double> rankedScores= new ArrayList<Double>();
	    ArrayList<String> testRankData=new ArrayList<String>();
	    ArrayList<String> preferenceRank=new ArrayList<String>();
	    ArrayList<Integer> QIDS=new ArrayList<Integer>();
	    ArrayList<String> bestCandidates=new ArrayList<String>();
	    ArrayList<Integer> bestCandidatesLocation=new ArrayList<Integer>();
	    Double bestCandidateInt=null;
		BufferedReader br=new BufferedReader(new FileReader(f));
		BufferedReader br2=new BufferedReader(new FileReader(f2));
		BufferedReader br3=new BufferedReader(new FileReader(f3));
		String line="";
		line=br.readLine();
		while(line!=null){
		  rankedScores.add(Double.parseDouble(line));
		  line=br.readLine();
		}
		
		//System.out.println("Done with prediction reading");
		Arrays.sort(rankedScores.toArray());
		String line2="";
		line2=br2.readLine();
		while(line2!=null){
			testRankData.add(line2);
			line2=br2.readLine();
		}
		
		String line3="";
		line3=br3.readLine();
		while(line3!=null){
			QIDS.add(Integer.parseInt(line3));
			line3=br3.readLine();
		}
		//System.out.println(QIDS.size());
		//System.out.println("Done with test data reading");
		for( String s: testRankData){
			preferenceRank.add(s.charAt(0)+"");
			
		}
		//System.out.println("Done with separating preferences and query id");
		for(int i=0;i<=QIDS.size()-1;i++){
			if(i==QIDS.size()-1 && !QIDS.get(i).equals(QIDS.get(i-1))){
				bestCandidateInt=rankedScores.get(i);
				currentPos=i;
				bestCandidates.add(bestCandidateInt.toString());
				bestCandidatesLocation.add(currentPos);
				bestCandidateInt=null;
				currentPos=0;
			}
			if(i<QIDS.size()-1){
			if(QIDS.get(i).equals(QIDS.get(i+1))){
				if(rankedScores.get(i)>rankedScores.get(i+1)){
					bestCandidateInt=rankedScores.get(i);
					currentPos=i;
				}else if(rankedScores.get(i+1)>rankedScores.get(i)){
					bestCandidateInt=rankedScores.get(i+1);
					currentPos=i+1;
				}
			}else if(!QIDS.get(i).equals(QIDS.get(i+1)) && !QIDS.get(i).equals(QIDS.get(i-1))){
				bestCandidateInt=rankedScores.get(i);
				currentPos=i;
				bestCandidates.add(bestCandidateInt.toString());
				bestCandidatesLocation.add(currentPos);
				bestCandidateInt=null;
				currentPos=0;
			
				
			}else {
			
			bestCandidates.add(bestCandidateInt.toString());
			bestCandidatesLocation.add(currentPos);
			bestCandidateInt=null;
			currentPos=0;
			}
			}
		}
		System.out.println("The best ranked candidates");
		System.out.println("The number of best ranked candidates: "+bestCandidatesLocation.size());
		for(int i=0;i<bestCandidatesLocation.size();i++){
			System.out.println(testRankData.get(bestCandidatesLocation.get(i)));
			if(testRankData.get(bestCandidatesLocation.get(i)).charAt(0)=='2'){
				rightRank++;
			}else{
				wrongRank++;
			}
		}
		System.out.println("Candidate recall: "+(rightRank/912)* 100 +" %");
		System.out.println("Page Accuracy: "+(rightRank/1937)*100 +" %");
		System.out.println("Candidate accuracy: "+(rightRank/numberOfCandidates)*100+" %");
		System.out.println("Number of candidates that were correctly ranked: "+rightRank);
		System.out.println("Number of candidates that were wrong:"+ wrongRank);
		System.out.println("Total number of candidates: "+ numberOfCandidates);
	}
	
	
	
	public void accuracyofTest()  {
		
			Accuracy a=new Accuracy();
			try {
				a.checkAccuracy("SVM_files/predictedoutputFile.txt", "SVM_files/truthData.txt" );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
}
