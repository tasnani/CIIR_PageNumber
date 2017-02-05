package pagenumber;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.ws.util.StringUtils;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;
import pagenumber.generateData.PageNumberPrediction;
import pagenumber.Forward;
public class viterbiTest {

	static HashMap<Integer, ArrayList<Double>> viterbiPages = new HashMap<Integer, ArrayList<Double>>();
//	static HashMap<Integer, ArrayList<ViterbiCandidate>> book = new HashMap<Integer, ArrayList<ViterbiCandidate>>();
	static HashMap<Integer, ArrayList<Double>> rankedScoresMap = new HashMap<Integer, ArrayList<Double>>();
	static ArrayList<ViterbiCandidate> page1 = new ArrayList<ViterbiCandidate>();
	static ArrayList<ViterbiCandidate> page2 = new ArrayList<ViterbiCandidate>();
	static ArrayList<ViterbiCandidate> page3 = new ArrayList<ViterbiCandidate>();
	static HashMap<String, HashMap<Integer, ArrayList<ViterbiCandidate>>> allBooks;
	static ArrayList<String> listOfBookNames = new ArrayList<String>();
    static HashMap<Integer,String> viterbiOutput = new HashMap<Integer,String>();
 	static Integer[] keySet;
 	static HashMap<String, Integer> arrayRomans = new HashMap<String,Integer>();
 	static ViterbiCandidate maxArg = null;
	int numberOfBlanks=0;
	static ViterbiCandidate viterbiCandidate;
	static HashMap<Integer, ArrayList<ViterbiCandidate>> book;
	


	public static class ViterbiCandidate {
		String text;
		int page;
		double rankedScores;
		int rank;
		ViterbiCandidate previousBestCandidate;

		public ViterbiCandidate(String t, int p, double score, int rank, ViterbiCandidate previousBestCandidate) {
			this.text = t;
			this.page = p;
			this.rankedScores = score;
			this.rank = rank;
			this.previousBestCandidate = previousBestCandidate;
		}
	}
	
public static HashMap<Integer, ArrayList<ViterbiCandidate>> initialize() throws IOException{
	//allBooks = new HashMap<String, HashMap<Integer, ArrayList<ViterbiCandidate>>>();
    book = new HashMap<Integer, ArrayList<ViterbiCandidate>>();

	FileReader FR2 = new FileReader("SVM_files/CSVTEST3.txt");
	BufferedReader BR2 = new BufferedReader(FR2);
	//CSV file reader 

	String line = BR2.readLine();
	//System.out.println(line);
	while(line != null){
		viterbiCandidate = new ViterbiCandidate(returnCandidateText(line).trim(),
				Integer.parseInt(returnPageID(line).trim()),
				Double.parseDouble(returnCandidateScore(line).trim()),0,null);
	    if(!book.containsKey(viterbiCandidate.page)){
	    	book.put(viterbiCandidate.page, new ArrayList<ViterbiCandidate>());
	    }
	    book.get(viterbiCandidate.page).add(viterbiCandidate);
	    line = BR2.readLine();
	}
	Integer[] keySet = new Integer[book.keySet().size()];
	keySet = book.keySet().toArray(keySet);
	for(int page = 0 ; page < keySet.length; page++){
		Collections.sort(book.get(keySet[page]),
				new Comparator<ViterbiCandidate>() {
					public int compare(ViterbiCandidate c1, ViterbiCandidate c2) {
						return -Double.compare(c1.rankedScores, c2.rankedScores);
					}
				});
		for (int candidate = 0; candidate < book.get(keySet[page])
				.size(); candidate++) {
			book.get(keySet[page]).get(candidate).rank = candidate + 1;
		}
	
	}
	
	
	
	return book;
	
}

public static String returnPageID(String line){
	String page_id = line.substring(line.indexOf("”"), line.lastIndexOf(",")).trim();
	//System.out.println(page_id);
	page_id = page_id.substring(page_id.indexOf(",")+1,page_id.length()).trim();
	//System.out.println(page_id);
	return page_id;
	
}

public static String returnCandidateText(String line){
	//System.out.println(line.contains("“"));
	
	String candidateText = line.substring(line.indexOf("“")+1 , line.lastIndexOf("”")).trim();
	//System.out.println(candidateText);
	candidateText = candidateText.substring(0, candidateText.length());
	//System.out.println(candidateText);
	return candidateText;
}

public static String returnCandidateScore(String line){
	String candidateScore = line.substring(line.lastIndexOf(",")+1, line.length());
	return candidateScore;
}

public static int convertRomanToInt(ViterbiCandidate c){
		
		arrayRomans.put("i", 1); arrayRomans.put("ii", 2); arrayRomans.put("iii", 3); arrayRomans.put("iv", 4); arrayRomans.put("v", 5);
		arrayRomans.put("vi", 6); arrayRomans.put("vii", 7); arrayRomans.put("viii", 8); arrayRomans.put("ix", 9); arrayRomans.put("x", 10); 
		
		if(arrayRomans.containsKey(c.text)){
			return arrayRomans.get(c.text);
		}else{
			return 0;
		}
		
	}
	
	public static boolean checkIfRoman(ViterbiCandidate c){
		if(arrayRomans.containsKey(c.text)){
			return true;
		}
		return false;
	}
	
	public static boolean checkIfArabic(ViterbiCandidate c){
		char[] charArray = c.text.toCharArray();
		for (char ch: charArray){
			if(!Character.isDigit(ch)){
				return false;
			}
		}
		return true;
	}
	

	public static Double calculateTransitionProbabilities(ViterbiCandidate c1, ViterbiCandidate c2) {
		//HEURISTICS
		
		Integer difference;
		if(checkIfArabic(c1) && checkIfArabic(c2)){
			difference = Integer.parseInt(c2.text) - Integer.parseInt(c1.text);
			Double value = 1 - 0.05* difference;
			if(value<0) return Math.abs(value);
			return value;
		} if(checkIfArabic(c1) && checkIfArabic(c2)){
			difference = arrayRomans.get(c2.text) - arrayRomans.get(c1.text);
			Double value = 1 - 0.05*difference;
			if( value < 0) return Math.abs(value);
			return value;
			
		}else if(checkIfArabic(c1)  && checkIfRoman(c2)){
			difference = convertRomanToInt(c2) - Integer.parseInt(c1.text);
			Double value = 0.5 - 0.05* difference;
			if(value<0) return Math.abs(value);
			return value;
			//means that the next page is roman and the previous page is arabic 
		}else if(checkIfRoman(c1) && checkIfArabic(c2)){
			difference = Integer.parseInt(c2.text) - convertRomanToInt(c1);
			Double value = 1- 0.05* difference;
			if(value<0) return Math.abs(value);
			return value;
			//means that the next page is arabic and the previous page is roman
		}else if((!checkIfRoman(c1) && !checkIfArabic(c1)) && (!checkIfRoman(c2) && !checkIfArabic(c2))){
			return 0.2;
		}else if( c1.text.equals("blank") && (checkIfArabic(c2) || checkIfRoman(c2))){
			return 0.5;
		}
		
		
		
		
	    return 0.1;
		//return ((c1.rankedScores * c2.rankedScores) / c2.rankedScores);
	}
	
	public static Double findMAX(Integer page, int candidate, int lowerBound, int upperBound,
			HashMap<Integer, ArrayList<Double>> viterbiPages, HashMap<Integer, ArrayList<ViterbiCandidate>> book ) {
		Double maximum = 0.0;
		
		for (int i = lowerBound; i < upperBound; i++) {
			int previousPage= page-1;
			
			try {
				
				Double newValue = viterbiPages.get(keySet[previousPage]).get(i)
						* calculateTransitionProbabilities(book.get(keySet[previousPage]).get(i), book.get(keySet[page]).get(candidate))
						* rankedScoresMap.get(keySet[page]).get(candidate);

				if (newValue > maximum) {
					maximum = newValue;
					maxArg = book.get(keySet[previousPage]).get(i);
					
				}
			} catch (NullPointerException NE) {
				System.err.println(i+"\t"+page+"\t"+candidate+"\t"+lowerBound+"\t"+upperBound +"\t"+book.get(keySet[page-1]).size());
			}
		  
			
		}
		
    
		return maximum;
	}
	
	public static ViterbiCandidate returnMaxArg(ViterbiCandidate c){
		return c;
	}
	//find the maximum and minimum ranked score throughout the book and use it to normalize all the candidates
	//ranked scores except for the blank candidate, where its score is hardcoded. 
	public static void loadNormalizedRankedScores(Integer[] keySet,HashMap<Integer,ArrayList<ViterbiCandidate>> book,HashMap<Integer,ArrayList<Double>> rankedScoresMap){
		
		Double minimumScore = 1000000.0;
	    Double maximumScore = -1000000.0;
		for(int page=0;page<keySet.length;page++){
			for(int candidate=0;candidate<book.get(keySet[page]).size();candidate++){
				Double rankedScoreValue=book.get(keySet[page]).get(candidate).rankedScores;
				if(rankedScoreValue< minimumScore) minimumScore = rankedScoreValue;
				if(rankedScoreValue > maximumScore) maximumScore = rankedScoreValue;
				
			}
			
			
		}
		//did normalize across all pages
		for(int page=0;page<keySet.length;page++){
			for(int candidate=0;candidate<book.get(keySet[page]).size();candidate++){
				if(book.get(keySet[page]).get(candidate).text.equals("blank")) {
				rankedScoresMap.get(keySet[page]).set(candidate, 0.35 );
				} else{ 
				rankedScoresMap.get(keySet[page]).set(candidate, (book.get(keySet[page]).
				get(candidate).rankedScores - minimumScore)/(maximumScore-minimumScore));}
				//System.out.print(" "+rankedScoresMap.get(keySet[page]).get(candidate));
			}
			
		}
		//System.out.println();
	}
	public static double howLikelyToStart(ViterbiCandidate c){
		if(c.text.equals("1") || c.text.equals("i")){
			return 1;
		}else{
			if(checkIfRoman(c)){
				int result = convertRomanToInt(c);
				if(result == 0){
					return 0.2;
				}
			}else if(checkIfArabic(c)){
				int difference = Integer.parseInt(c.text)-1;
				
				return 1 - 0.05 * Math.abs(difference);
				
			}
		}
		return 0.2;
	}
	
	public static double howLikelyToEnd(ViterbiCandidate c){
		if(checkIfRoman(c)){
			return 0.2;
		}else if(!checkIfRoman(c) && !checkIfArabic(c)){
			return 0.1;
		}else if(checkIfArabic(c)){
			if(c.text.equals("1")) return 0.1;
			Integer difference = Integer.parseInt(c.text) - Integer.parseInt(c.previousBestCandidate.text);
			return 1-0.05* Math.abs(difference);
			//check for the previous candidate and some threshold for the number of pages
		}
		return 0.2;
	}
	//initialize the ranked scores hashmap and viterbi data structure 
	public static void doViterbi(HashMap<Integer, ArrayList<ViterbiCandidate>> book) {
	    String viterbiAnswer = "";
	    
		keySet = new Integer[book.keySet().size()];
	    
		book.keySet().toArray(keySet);
		for (int i = 0; i < keySet.length; i++) {
			viterbiPages.put(keySet[i], new ArrayList<Double>());
			rankedScoresMap.put(keySet[i], new ArrayList<Double>());
			for (int k = 0; k < book.get(keySet[i]).size(); k++) {
				viterbiPages.get(keySet[i]).add(k, 1.0);
				rankedScoresMap.get(keySet[i]).add(k, 1.0);
			}
		}
		loadNormalizedRankedScores(keySet,book,rankedScoresMap);

		int index = 0;
		
		for (int candidate = 0; candidate < book.get(keySet[0]).size(); candidate++) {
		
			viterbiPages.get(keySet[0]).set(candidate,
					howLikelyToStart(book.get(keySet[0]).get(candidate)) 
							* rankedScoresMap.get(keySet[0]).get(candidate));
		}

		for (int page = 1; page < book.keySet().size(); page++) {
			for (int candidate = 0; candidate < book.get(keySet[page]).size(); candidate++) {
				double maxProbability = findMAX(page, candidate, 0, book.get(keySet[page-1]).size(),viterbiPages,book);
				ViterbiCandidate previousMaxArg = returnMaxArg(maxArg);
				book.get(keySet[page]).get(candidate).previousBestCandidate = previousMaxArg;
				viterbiPages.get(keySet[page]).set(candidate,
						maxProbability);
				
			}
		}
		
		int lastPage = keySet.length - 1;
		int lastCandidate = book.get(keySet[lastPage]).size() - 1;
		findMAX(lastPage, lastCandidate, 0, book.get(keySet[lastPage-1]).size(), viterbiPages,book);
	    ViterbiCandidate previousMaxArg = returnMaxArg(maxArg);
	    int maxCandidateIndex = 0;
		Double max = 0.0;
		for (int candidate = 0; candidate < book.get(keySet[lastPage]).size(); candidate++) {
			Double val = viterbiPages.get(keySet[lastPage]).get(candidate) * howLikelyToEnd(book.get(keySet[lastPage]).get(candidate));
			if(val>max) {
				max = val;
				maxCandidateIndex = candidate; }
					
		}
		book.get(keySet[lastPage]).get(maxCandidateIndex).previousBestCandidate = previousMaxArg;
		viterbiPages.get(keySet[lastPage]).set(maxCandidateIndex,max);
		
		ViterbiCandidate curr = book.get(keySet[lastPage]).get(maxCandidateIndex);
		viterbiAnswer = curr.text+","+ curr.page;
		System.out.println(viterbiAnswer);
		
		while ( curr.previousBestCandidate!= null) {
		
			System.out.println(curr.previousBestCandidate.text+","+curr.previousBestCandidate.page);
			curr = curr.previousBestCandidate;
			
		}
		
		//viterbiOutput.put(1, viterbiAnswer);
		//System.out.println(viterbiOutput.containsKey(bookName));
		//System.out.println(bookName);
		//"[Rank: "+ book.get(keySet[page]).get(maxCandidate).rank + "]"
		//"[Score: "+ book.get(keySet[page]).get(maxCandidate).rankedScores + "]"
		viterbiPages.clear();
		rankedScoresMap.clear();
		
	}

	public static int findMaxArg(int upperBound, int lowerBound, int page) {
		int maxCandidate = 0;
		Double maxCandidateValue = 0.0;
		for (int i = lowerBound; i < upperBound; i++) {
			if (viterbiPages.get(page).get(i) > maxCandidateValue) {
				maxCandidateValue = viterbiPages.get(page).get(i);
				maxCandidate = i;
			}
		}
		return maxCandidate;
	}

	public static void main(String args[]) throws IOException {
		initialize();

		//for (int i = 0; i < listOfBookNames.size(); i++) {
			//System.out.println("Book: " + (i + 1) + " " + "," + "Name: " + listOfBookNames.get(i));
			doViterbi(book);
			System.out.println();
			System.out.println();

		//}
		
		//checkViterbiAccuracy(viterbiOutput);
		/*
		 * TODO Research new function that calculates transition probabilities.
		 */

	}
	/*
	public static void checkViterbiAccuracy(HashMap<String, String> viterbiOutput) throws IOException{
		double correct = 0;
		double totalTokens = 0;
		HashMap<String,String> booksTruthData = new HashMap<String, String>();
		booksTruthData.put("gunnartaleofnors00boyerich","truth_data/gunnar-annotated.txt" );
		booksTruthData.put("historicfurnishi00gras","truth_data/histor-annotated.txt" );
		booksTruthData.put("jack00shergoog","truth_data/jack00-annotated.txt");
		booksTruthData.put("johannladislavp01pyrkgoog","truth_data/johann-annotated.txt");
		String[] viterbiOutputKeySet = new String[viterbiOutput.keySet().size()];
		//System.out.println(viterbiOutput);
	    
		
		String[] booksTruthDataKeySet = new String[booksTruthData.keySet().size()];
		viterbiOutputKeySet = viterbiOutput.keySet().toArray(viterbiOutputKeySet);
		
		booksTruthDataKeySet = booksTruthData.keySet().toArray(booksTruthDataKeySet);
		for(int k = 0;k < booksTruthDataKeySet.length ; k++){
			
		for(int i = 0; i< viterbiOutputKeySet.length ; i++){
			
			if(booksTruthDataKeySet[k].equals((viterbiOutputKeySet[i]))){
				
				String[] viterbiOutputTokens = viterbiOutput.get(viterbiOutputKeySet[i]).split("\\s+");
				totalTokens = viterbiOutputTokens.length;
				
				FileReader fileReader = new FileReader(booksTruthData.get(booksTruthDataKeySet[k]));
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String line = bufferedReader.readLine();
				while(line != null){
					//totalTokens++;
					
					line = line.replaceAll("\\s+","").trim();
					for(int j =0;j < viterbiOutputTokens.length; j++){
						if(line.equals(viterbiOutputTokens[j])){
							correct++;
						}
					}
					line = bufferedReader.readLine();
					
				}
			
				System.out.println("Book name: "+booksTruthDataKeySet[k]+" ,"+"Accuracy: "+(correct/totalTokens)*100+" %");
			    correct = 0.0;
			    totalTokens=0.0;
				
			}
			
		}
		
		}
	}

*/
}
