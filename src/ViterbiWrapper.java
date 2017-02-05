package pagenumber;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import pagenumber.ExtractFeaturesBookWrapper.PageNumberCandidate;
//import pagenumber.Forward.ViterbiCandidate;
//import pagenumber.generateData.PageNumberPrediction;
//import pagenumber.Forward;

public class ViterbiWrapper {
	//static Forward f = new Forward();
	static HashMap<Integer, ArrayList<Double>> viterbiPages = new HashMap<Integer, ArrayList<Double>>();
	static HashMap<Integer, ArrayList<ViterbiCandidate>> book = new HashMap<Integer, ArrayList<ViterbiCandidate>>();
	static HashMap<Integer, ArrayList<Double>> rankedScoresMap = new HashMap<Integer, ArrayList<Double>>();
	static ArrayList<ViterbiCandidate> page1 = new ArrayList<ViterbiCandidate>();
	static ArrayList<ViterbiCandidate> page2 = new ArrayList<ViterbiCandidate>();
	static ArrayList<ViterbiCandidate> page3 = new ArrayList<ViterbiCandidate>();
	static HashMap<Integer, ArrayList<ViterbiCandidate>> oneBook;
	static ArrayList<String> listOfBookNames = new ArrayList<String>();
    static HashMap<String,String> viterbiOutput = new HashMap<String,String>();
 	static Integer[] keySet;
 	static HashMap<String, Integer> arrayRomans = new HashMap<String,Integer>();
 	static ViterbiCandidate maxArg = null;
	int numberOfBlanks=0;
	

	public ViterbiWrapper() {

	}

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
			return 1 - 0.05* difference;
		}else if(checkIfArabic(c1)  && checkIfRoman(c2)){
			difference = convertRomanToInt(c2) - Integer.parseInt(c1.text);
			return 0.5 - 0.05* difference;
			//means that the next page is roman and the previous page is arabic 
		}else if(checkIfRoman(c1) && checkIfArabic(c2)){
			difference = Integer.parseInt(c2.text) - convertRomanToInt(c1);
			return 1- 0.05* difference;
			//means that the next page is arabic and the previous page is roman
		}else if((!checkIfRoman(c1) && !checkIfArabic(c1)) && (!checkIfRoman(c2) && !checkIfArabic(c2))){
			return 0.2;
		}
		
		
		
		
	    return 0.1;
		//return ((c1.rankedScores * c2.rankedScores) / c2.rankedScores);
	}
   //Loading in data from the test and ranked scores files into the book hashmap data structures for all books. 
	public HashMap<Integer, ArrayList<ViterbiCandidate>> initialize(String predictionsURL, String testDataURL) throws IOException {

		oneBook = new HashMap<Integer, ArrayList<ViterbiCandidate>>();
		FileReader FR = new FileReader(predictionsURL);
		BufferedReader BR = new BufferedReader(FR);

		FileReader FR2 = new FileReader(testDataURL);
		BufferedReader BR2 = new BufferedReader(FR2);

		String line = BR2.readLine();
		String rankedScoreValue = BR.readLine();
		String actualNameOfBook = line.substring(line.lastIndexOf(',') + 1, line.length()).trim();
		Integer actualPageImage = Integer.parseInt(line.substring(line.indexOf('#') + 1, line.indexOf(',')).trim());
		//allBooks.put(actualNameOfBook, new HashMap<Integer, ArrayList<ViterbiCandidate>>());
		oneBook.put(actualPageImage, new ArrayList<ViterbiCandidate>());
		String candidateText = line.substring(line.indexOf(',') + 1, line.lastIndexOf(',')).trim();
		oneBook.get(actualPageImage)
		.add(new ViterbiCandidate(candidateText, actualPageImage, Double.parseDouble(rankedScoreValue), 0,null));
		
	    oneBook.get(actualPageImage).add(new ViterbiCandidate("blank",actualPageImage,0.01,0,null));

		//listOfBookNames.add(actualNameOfBook);
		while (line != null) {
			Integer pageImage = Integer.parseInt(line.substring(line.indexOf('#') + 1, line.indexOf(',')));
			candidateText = line.substring(line.indexOf(',') + 1, line.lastIndexOf(',')).trim();
			String nameOfBook = line.substring(line.lastIndexOf(',') + 1, line.length()).trim();
			
			/*
			if (allBooks.containsKey(nameOfBook) == false) {
				allBooks.put(nameOfBook, new HashMap<Integer, ArrayList<ViterbiCandidate>>());
				actualNameOfBook = nameOfBook;
				listOfBookNames.add(actualNameOfBook);
			}
			*/
			if (oneBook.containsKey(pageImage) == false) {
				oneBook.put(pageImage, new ArrayList<ViterbiCandidate>());
				actualPageImage = pageImage;
			    oneBook.get(actualPageImage).add(new ViterbiCandidate("blank",actualPageImage,0.01,0,null));
				
			}

			oneBook.get(actualPageImage)
					.add(new ViterbiCandidate(candidateText, pageImage, Double.parseDouble(rankedScoreValue), 0,null));
			rankedScoreValue = BR.readLine();
			line = BR2.readLine();

		}
	// sort the lists of candidates for each page in a book by their ranked score in descending order
		for (int bookName = 0; bookName < listOfBookNames.size(); bookName++) {
			Integer[] keySet = new Integer[oneBook.keySet().size()];
			keySet = oneBook.keySet().toArray(keySet);
			
			for (int page = 0; page < keySet.length; page++) {
				Collections.sort(oneBook.get(keySet[page]),
						new Comparator<ViterbiCandidate>() {
							public int compare(ViterbiCandidate c1, ViterbiCandidate c2) {
								return -Double.compare(c1.rankedScores, c2.rankedScores);
							}
						});
				//assign new rank to each candidate after being sorted. 
				for (int candidate = 0; candidate < oneBook.get(keySet[page])
						.size(); candidate++) {
					oneBook.get(keySet[page]).get(candidate).rank = candidate + 1;
				}
				
                
                
			}
		}
		return oneBook;
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
				return 1 - 0.05 * difference;
				
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
			return 1-0.05*difference;
			//check for the previous candidate and some threshold for the number of pages
		}
		return 0.2;
	}
	//initialize the ranked scores hashmap and viterbi data structure 
	public static void doViterbi(HashMap<Integer, ArrayList<ViterbiCandidate>> book) throws IOException {
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
		viterbiAnswer = curr.page+","+curr.text;
		
		while ( curr.previousBestCandidate!= null) {
		
			viterbiAnswer = viterbiAnswer + " " + curr.previousBestCandidate.page+","+curr.previousBestCandidate.text;
			curr = curr.previousBestCandidate;
			
		}
		//System.out.print(viterbiAnswer);
		String directory = "/Users/tanasn/Documents/workspace/CIIR_PageNumber/outputs/";
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        System.out.println("Enter a name for the file you want to save the output to:");
        String outputFileName = br.readLine();
        String savedDirectory = directory+outputFileName;
        System.out.println();
        System.out.println();
        System.out.println("Creating file at: "+savedDirectory+".....");
        File outputFile = new File(savedDirectory);
        System.out.println();
        System.out.println();
        if(outputFile.createNewFile()){
        	System.out.println(outputFileName+" was created and saved to" + savedDirectory);
        }else{
        	System.out.println(outputFileName+" could not be created and saved to" + savedDirectory);
        }
        System.out.println();
        System.out.println();
        System.out.println("Now writing output to " +outputFileName+ ".....");
        FileWriter fileWriter = new FileWriter(outputFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);
      

        printWriter.print("Format: Page ID, Best page number candidate");
        printWriter.print("\n");
		StringTokenizer st = new StringTokenizer(viterbiAnswer);
		Stack<String> stack = new Stack<String>();
		while(st.hasMoreTokens()){
			stack.push(st.nextToken());
		}
		while(!stack.isEmpty()){
			printWriter.println(stack.pop());
		}
		System.out.println();
		System.out.println();
		System.out.println("Output written successfully to "+outputFileName);
		printWriter.close();
		
		
		//viterbiOutput.put(,viterbiAnswer);
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

	//public static void main(String args[]) throws IOException {
		//initialize();

		
			//doViterbi(oneBook);
			//System.out.println();
			//System.out.println();

		
		
		//checkViterbiAccuracy(viterbiOutput);
		/*
		 * TODO Research new function that calculates transition probabilities.
		 */

	//}
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

}
