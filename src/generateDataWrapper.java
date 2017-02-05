package pagenumber;
//import pagenumber.ExtractFeaturesBook;
import pagenumber.ExtractFeaturesBookWrapper;

import pagenumber.ExtractFeaturesBookWrapper.PageNumberCandidate;
import java.awt.font.LineBreakMeasurer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class generateDataWrapper {
String line="";
int c=0;

String[] temp=new String[2];
HashMap<String,String> truthDataMap=new HashMap<String,String>();
ArrayList<String> QIDS=new ArrayList<String>();
ArrayList<String> correctExamplesInTestData=new ArrayList<String>();
ArrayList<String> misclassifiedData=new ArrayList<String>();
ArrayList<String> rankArray=new ArrayList<String>();


String token;
int positiveExampleNumber=0;
int negativeExampleNumber=0;

int numberOfCandidates=0;
int QID=1;
int counter=0;
int total=0;
int right=0;
int wrong=0;
int numberOfPages=0;

ArrayList<String> truthArray=new ArrayList<String>();
	public void generateTrainingData(String truthData,List<List<ExtractFeaturesBookWrapper.PageNumberCandidate>> book) throws IOException{
		//System.out.println("Entered");
		
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
			numberOfPages=numberOfPages+book.size();
		for(List<ExtractFeaturesBookWrapper.PageNumberCandidate> b: book){
			
			for( ExtractFeaturesBookWrapper.PageNumberCandidate PNC : b){
				//System.out.println(b);
				//System.out.println(book);
					
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
			  
		     String s=token+" 1:"+PNC.get("is a number")+" "
		     +"2:"+PNC.get("does look like a number")+" "
		    		 +"3:"+PNC.get("x-fraction")+" "+"4:"+
		    		 PNC.get("sequence")+" "+"5:"+PNC.get("y-fraction")+" "+
		     "6:"+PNC.get("downward % thru line")+" "+
		    		"7:"+ PNC.get("upward % thru line")+" "+
		     "8:"+PNC.get("downward % thru page")+
		    		 " "+"9:"+PNC.get("upward % thru page")+
		    		 " #"+PNC.imageNumber()+","+PNC.text+","+PNC.book();
			 truthArray.add(s);
			 
			//System.out.println(s);
			  
			  // if(token.equals("+1")) correctExamplesInTestData.add(s);
			}
			
		}
		
		//System.out.println("Number of positive examples: " +positiveExampleNumber);
		//System.out.println("Number of negative examples: "+negativeExampleNumber);
		System.out.println("Total number of pages: "+numberOfPages);
	}
	//ArrayList<String> tempArray = new ArrayList<String>();
	public void generateTestData(List<List<ExtractFeaturesBookWrapper.PageNumberCandidate>> book, PrintWriter qidOutput, PrintWriter output){
		int totalNumberOfCandidates = 0;
		ArrayList<String> rankArray = new ArrayList<String>();
		for(List<ExtractFeaturesBookWrapper.PageNumberCandidate> b: book){
			
			for( ExtractFeaturesBookWrapper.PageNumberCandidate PNC : b){
			String s  = 0+" "+"1:"+PNC.features.get("is a number")
			+" "+"2:"+PNC.features.get("does look like a number")
			+" "+"3:"+PNC.features.get("x-fraction")
			+" "+"4:"+PNC.features.get("sequence")
			+" "+"5:"+PNC.features.get("y-fraction")
			+" "+"6:"+PNC.features.get("downward % thru line")
			+" "+"7:"+PNC.features.get("upward % thru line")
			+" "+"8:"+PNC.features.get("downward % thru page")
			+" "+"9:"+PNC.features.get("upward % thru page")
			+" #"+PNC.imageNumber()+","+PNC.text+","+PNC.book();
			rankArray.add(s);
			totalNumberOfCandidates++;
			//System.out.println(s);
			
		   
			}
			
			for(int i=0;i<=rankArray.size()-2;i++){
				
				QIDS.add(QID+"");
				qidOutput.println(QID);
				rankArray.set(i, rankArray.get(i).charAt(0)+" qid:"+QID+" "+rankArray.get(i).substring(2,rankArray.get(i).length()));
				if(!rankArray.get(i).substring(rankArray.get(i).
				indexOf('#')+1, rankArray.get(i).indexOf(',')).equals(rankArray.get(i+1).substring(rankArray.get(i+1).
						indexOf('#')+1, rankArray.get(i+1).indexOf(',')))){
					QID++;
				}
				
				output.println(rankArray.get(i));
	
			}
		}
	}
	
	public void generateRankData(PrintWriter output, PrintWriter qidOutput ,List<List<ExtractFeaturesBookWrapper.PageNumberCandidate>> book) throws FileNotFoundException{
		
	   
	    wrong=0;
	    ArrayList<String> rankArray=new ArrayList<String>();
		
	   
		for(String s: truthArray){
			
	    	if(s.charAt(0)=='+'){
	    		s="0"+s.substring(2,s.length());
	    		//System.out.println(s);
	    		right++;
	    		correctExamplesInTestData.add(s);
	    		//System.out.println("Size of correct examples in test data:"+correctExamplesInTestData.size());
	    		
	    	}
	    	if(s.charAt(0)=='-'){
	    		s="0"+s.substring(2,s.length());
	    		wrong++;
	    		
	    	}
	    	
	    	rankArray.add(s);
	    	numberOfCandidates++;
	    	//System.out.println(s);
	    	 
	    	 
	    }
		for(int i=0;i<=rankArray.size()-2;i++){
			
			QIDS.add(QID+"");
			qidOutput.println(QID);
			rankArray.set(i, rankArray.get(i).charAt(0)+" qid:"+QID+" "+rankArray.get(i).substring(2,rankArray.get(i).length()));
			if(!rankArray.get(i).substring(rankArray.get(i).
			indexOf('#')+1, rankArray.get(i).indexOf(',')).equals(rankArray.get(i+1).substring(rankArray.get(i+1).
					indexOf('#')+1, rankArray.get(i+1).indexOf(',')))){
				QID++;
			}
			
			output.println(rankArray.get(i));
			
			total++;
		}
		//output.close();
		//qidOutput.close();
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
		//
		//System.out.println("Total number of candidates: "+total);
		
	}
	
	public static class PageNumberPrediction implements Comparable<PageNumberPrediction> {
		int preferenceRank;
		double score;
		int qid;
		String actualData;
		@Override
		public int compareTo(PageNumberPrediction o) {
			// sorting by default puts smallest first
			// negative will put biggest first
			return -Double.compare(this.score, o.score);
		}
		//TODO: make this correct?
		public boolean isCorrect() {
			return preferenceRank == 2;
		}
	}
	
	public void sortRankPredictions2(String rankPredictionsFile, String testDataFile, String queryIDFile) throws IOException{
	    ArrayList<Double> rankedScores= new ArrayList<Double>();
	    ArrayList<String> testRankData=new ArrayList<String>();
	    ArrayList<String> preferenceRank=new ArrayList<String>();
	    ArrayList<Integer> QIDS=new ArrayList<Integer>();
	   
		loadPredictions(rankPredictionsFile, rankedScores);
		readAllLines(testDataFile, testRankData,preferenceRank);
		loadQIDs(queryIDFile, QIDS);
		
		
		
		System.out.println("Start: "+QIDS.size()+"\t"+ testRankData.size()+"\t"+rankedScores.size()+"\t"+preferenceRank.size());
		
		
		
		ArrayList<PageNumberPrediction> all_data = new ArrayList<PageNumberPrediction>();
		int index=0;
		for(int i=0;i<rankedScores.size();i++){
			PageNumberPrediction pred=new PageNumberPrediction();
			pred.preferenceRank=Integer.parseInt(preferenceRank.get(index));
			pred.score=rankedScores.get(index);
			pred.qid=QIDS.get(index);
			pred.actualData=testRankData.get(index);
			
			all_data.add(index, pred);
			index++;
		}
		// TODO fill out list
		System.out.println("Is all_data empty ?"+all_data.size());
		Map<Integer, List<PageNumberPrediction>> predictionsByQid = new HashMap<>();
		
		//Done: fill out map
		for(PageNumberPrediction pred : all_data) {
			int qid = pred.qid;
			if(!predictionsByQid.containsKey(qid)) {
				predictionsByQid.put(qid, new ArrayList<>());
			}
			predictionsByQid.get(qid).add(pred);
		}
	    System.out.println("possible right:"+right);
		double totalPossible = right; //TODO this isn't zero, really
		double correctAt1 = 0;
		double correctAt2 = 0;
		double correctAt3=0;
		double correctAt4=0;
		double correctAt5=0;
		double correctAt6=0;
		double correctAt7=0;
		double correctAt8=0;
		ArrayList<PageNumberPrediction> errorAtRank1=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank2=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank3=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank4=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank5=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank6=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank7=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> errorAtRank8=new ArrayList<PageNumberPrediction>();
		ArrayList<PageNumberPrediction> misclassified=new ArrayList<PageNumberPrediction>();
		
		int total = predictionsByQid.size();
		if(predictionsByQid.values().isEmpty()) System.out.println("it is empty :(");
		for (List<PageNumberPrediction> predictionsForPage : predictionsByQid.values()) {
			// sort these, so the best is first
			Collections.sort(predictionsForPage);
		    try{
			if(predictionsForPage.get(0).isCorrect()) {
				
				correctAt1++;
				//System.out.println(predictionsForPage.get(0).actualData);
			}else{
				System.out.println(predictionsForPage.get(0).actualData);
				misclassified.add(predictionsForPage.get(0));
			}
		    }catch(IndexOutOfBoundsException ie){
		    	
		    }
		    try{
			if(predictionsForPage.get(1).isCorrect()) {
				errorAtRank1.add(predictionsForPage.get(1));
				
				correctAt2++;
				
			}else{
				System.out.println(predictionsForPage.get(1).actualData);
			}
		    }catch(IndexOutOfBoundsException ie){
		    	
		    }
			try{
			if(predictionsForPage.get(2).isCorrect()){
				errorAtRank2.add(predictionsForPage.get(2));
				
				correctAt3++;
			}else{
				System.out.println(predictionsForPage.get(2).actualData);
			}
			}catch(IndexOutOfBoundsException ie){
				
			}
			try{
            if(predictionsForPage.get(3).isCorrect()){
            	errorAtRank3.add(predictionsForPage.get(3));
				
				correctAt4++;
			}else{
				System.out.println(predictionsForPage.get(3).actualData);
			}
			}catch(IndexOutOfBoundsException ie){
				
			}  
			try{
	            if(predictionsForPage.get(4).isCorrect()){
	            	errorAtRank4.add(predictionsForPage.get(4));
					correctAt5++;
				}else{
					System.out.println(predictionsForPage.get(4).actualData);
				}
				}catch(IndexOutOfBoundsException ie){
					
				} 
			try{
	            if(predictionsForPage.get(5).isCorrect()){
	            
	            	errorAtRank5.add(predictionsForPage.get(5));
					correctAt6++;
					
				}else{
					System.out.println(predictionsForPage.get(5).actualData);
				}
				}catch(IndexOutOfBoundsException ie){
					
				} 
			try{
	            if(predictionsForPage.get(6).isCorrect()){
	            	errorAtRank6.add(predictionsForPage.get(6));
					correctAt7++;
				}else{
					System.out.println(predictionsForPage.get(6).actualData);
				}
				}catch(IndexOutOfBoundsException ie){
					
				} 
			try{
	            if(predictionsForPage.get(7).isCorrect()){
	            	errorAtRank7.add(predictionsForPage.get(7));
					correctAt8++;
				}else{
					System.out.println(predictionsForPage.get(7).actualData);
					}
				}catch(IndexOutOfBoundsException ie){
					
				} 
		}
	    
		double correctAt1PCT=(correctAt1/totalPossible)*100;
		double correctAt2PCT=(correctAt2/totalPossible)*100;
		double correctAt3PCT=(correctAt3/totalPossible)*100;
		double correctAt4PCT=(correctAt4/totalPossible)*100;
		//System.out.println("correctAt4PCT:"+correctAt4PCT);
		double correctAt5PCT=(correctAt5/totalPossible)*100;
		System.out.println("At rank 1: "+correctAt1PCT);
		System.out.println("At rank 2: "+(correctAt1PCT +correctAt2PCT));
		System.out.println("At rank 3: "+(correctAt1PCT +correctAt2PCT+correctAt3PCT));
		System.out.println("At rank 4: "+(correctAt1PCT +correctAt2PCT+correctAt3PCT+correctAt4PCT));
		System.out.println("At rank 5: "+(correctAt1PCT +correctAt2PCT+correctAt3PCT+correctAt4PCT+correctAt5PCT));
		
		System.out.println("Correct pages at 2nd rank:");
		for(int i=0;i<errorAtRank1.size();i++){
			for(int k=0;k<misclassified.size();k++){
				if(misclassified.get(k).qid==errorAtRank1.get(i).qid){
					System.out.println("Correct: "+errorAtRank1.get(i).actualData+", "+"Incorrect: "+misclassified.get(k).actualData);
				}
			}
		}
		
		System.out.println("Correct pages at 3rd rank:");
		for(int i=0;i<errorAtRank2.size();i++){
			for(int k=0;k<misclassified.size();k++){
				if(misclassified.get(k).qid==errorAtRank2.get(i).qid){
					System.out.println("Correct: "+errorAtRank2.get(i).actualData+", "+"Incorrect: "+misclassified.get(k).actualData);
				}
			}
		}
		System.out.println("Correct pages at 4th rank:");
		for(int i=0;i<errorAtRank3.size();i++){
			for(int k=0;k<misclassified.size();k++){
				if(misclassified.get(k).qid==errorAtRank3.get(i).qid){
					System.out.println("Correct: "+errorAtRank3.get(i).actualData+", "+"Incorrect: "+misclassified.get(k).actualData);
				}
			}
		}
		System.out.println("Correct pages at 5th rank:");
		for(int i=0;i<errorAtRank4.size();i++){
			for(int k=0;k<misclassified.size();k++){
				if(misclassified.get(k).qid==errorAtRank4.get(i).qid){
					System.out.println("Correct: "+errorAtRank4.get(i).actualData+", "+"Incorrect: "+misclassified.get(k).actualData);
				}
			}
		}
		System.out.println("Correct pages at 6th rank: ");
		for(int i=0;i<errorAtRank5.size();i++){
			for(int k=0;k<misclassified.size();k++){
				if(misclassified.get(k).qid==errorAtRank5.get(i).qid){
					System.out.println("Correct: "+errorAtRank5.get(i).actualData+", "+"Incorrect: "+misclassified.get(k).actualData);
				}
			}
		}
		System.out.println("Correct pages at 7th rank: ");
		for(int i=0;i<errorAtRank6.size();i++){
			for(int k=0;k<misclassified.size();k++){
				if(misclassified.get(k).qid==errorAtRank6.get(i).qid){
					System.out.println("Correct: "+errorAtRank6.get(i).actualData+", "+"Incorrect: "+misclassified.get(k).actualData);
				}
			}
		}
		
	}
	/*
	public void sortRankPredictions(String rankPredictionsFile, String testDataFile, String queryIDFile) throws IOException{
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
		
		loadPredictions(rankPredictionsFile, rankedScores);
		readAllLines(testDataFile, testRankData, preferenceRank);
		loadQIDs(queryIDFile, QIDS);

		
		//System.out.println("Done with prediction reading");		
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
		ArrayList<String> rightRankArray=new ArrayList<String>();
		for(int i=0;i<bestCandidatesLocation.size();i++){
			System.out.println(testRankData.get(bestCandidatesLocation.get(i)));
			if(testRankData.get(bestCandidatesLocation.get(i)).charAt(0)=='2'){
				rightRank++;
				rightRankArray.add(testRankData.get(bestCandidatesLocation.get(i)));
			}else{
				wrongRank++;
			}
		}
		
      boolean found=false;
		
		int notFoundCounter=0;
		
		System.out.println("------------------------------------------------------------------------------------------");
		
		//System.out.println("size of correct examples in test data:"+correctExamplesInTestData.size());
			for(int i=0;i<correctExamplesInTestData.size();i++){
				
				 for(int k=0;k<rightRankArray.size();k++){
					 if(rightRankArray.get(k).substring(rightRankArray.get(k).indexOf('#'),rightRankArray.get(k).length()).equals
						(correctExamplesInTestData.get(i).substring(correctExamplesInTestData.get(i).indexOf('#'), 
								correctExamplesInTestData.get(i).length()))){
					
					found=true;
					break;
				}
				}
				
				if(!found) {
				//System.out.println(correctExamplesInTestData.get(i));
				misclassifiedData.add(correctExamplesInTestData.get(i));
				notFoundCounter++;}
				found =false;

				
				
		}
			
			//System.out.println("Not found counter" + notFoundCounter);
			System.out.println("size of query ids: "+QIDS.size());
			System.out.println("size of test data: "+testRankData.size());
			System.out.println("Printing locations of misclassified data: ");
			System.out.println("Size of misclassified data: "+misclassifiedData.size());
			System.out.println("size of ranked data:"+testRankData.size());
			for(int k=0;k<misclassifiedData.size();k++){
			for(int i=0;i<testRankData.size();i++){
				if(testRankData.get(i).contains(misclassifiedData.get(k).substring(misclassifiedData.get(k).indexOf('#'), 
						misclassifiedData.get(k).length())))		
						{
							System.out.println(testRankData.get(i));
							
							
						}
			}
					
					
			}
			*/
			/*
			for(int i=0;i<rightRankArray.size();i++){
				System.out.println(rightRankArray.get(i));
			}
	
		//System.out.println("Not correctly found"+ counterNotFound);
		/*
		System.out.println("testRankData"+testRankData.size());
		
		System.out.println("Candidate recall: "+(rightRank/912)* 100 +" %");
		System.out.println("Page Accuracy: "+(rightRank/1937)*100 +" %");
		System.out.println("Candidate accuracy: "+(rightRank/numberOfCandidates)*100+" %");
		System.out.println("Number of candidates that were correctly ranked: "+rightRank);
		System.out.println("Number of candidates that were wrong:"+ wrongRank);
		System.out.println("Total number of candidates: "+ numberOfCandidates);
		
	}
	*/
	public void loadQIDs(String queryIDFile, ArrayList<Integer> QIDS) throws FileNotFoundException, IOException {
		BufferedReader br3=new BufferedReader(new FileReader(queryIDFile));
		String line3="";
		line3=br3.readLine();
		while(line3!=null){
			QIDS.add(Integer.parseInt(line3));
			line3=br3.readLine();
		}
	}
	public void readAllLines(String testDataFile, ArrayList<String> testRankData, ArrayList<String> preferenceRank)
			throws FileNotFoundException, IOException {
		BufferedReader br2=new BufferedReader(new FileReader(testDataFile));
		String line2="";
		line2=br2.readLine();
		while(line2!=null){
			testRankData.add(line2);
			preferenceRank.add(line2.charAt(0)+"");
			line2=br2.readLine();
		}
	}
	
	public static void loadPredictions(String rankPredictionsFile, ArrayList<Double> rankedScores)
			throws FileNotFoundException, IOException {
		BufferedReader br=new BufferedReader(new FileReader(rankPredictionsFile));
		String line="";
		line=br.readLine();
		while(line!=null){
		  rankedScores.add(Double.parseDouble(line));
		  line=br.readLine();
		}
	}
	
	
	/*
	public void accuracyofTest()  {
		
			Accuracy a=new Accuracy();
			try {
				a.checkAccuracy("SVM_files/predictedoutputFile.txt", "SVM_files/truthData.txt" );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	*/
	
}
