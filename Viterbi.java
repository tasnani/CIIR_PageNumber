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

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;
import pagenumber.generateData.PageNumberPrediction;
import pagenumber.Forward;

public class Viterbi {
	static Forward f = new Forward();
	static HashMap<Integer, ArrayList<Double>> viterbiPages = new HashMap<Integer, ArrayList<Double>>();
	static HashMap<Integer, ArrayList<ViterbiCandidate>> book = new HashMap<Integer, ArrayList<ViterbiCandidate>>();
	static HashMap<Integer, ArrayList<Double>> rankedScoresMap = new HashMap<Integer, ArrayList<Double>>();
	static ArrayList<ViterbiCandidate> page1 = new ArrayList<ViterbiCandidate>();
	static ArrayList<ViterbiCandidate> page2 = new ArrayList<ViterbiCandidate>();
	static ArrayList<ViterbiCandidate> page3 = new ArrayList<ViterbiCandidate>();
	static HashMap<String, HashMap<Integer, ArrayList<ViterbiCandidate>>> allBooks;
	static ArrayList<String> listOfBookNames = new ArrayList<String>();
	static Integer[] keySet;
	int numberOfBlanks=0;
	static ViterbiCandidate blankCandidate;

	public Viterbi() {

	}

	public static class ViterbiCandidate {
		String text;
		int page;
		double rankedScores;
		int rank;

		public ViterbiCandidate(String t, int p, double score, int rank) {
			this.text = t;
			this.page = p;
			this.rankedScores = score;
			this.rank = rank;
		}
	}

	public static Double calculateTransitionProbabilities(ViterbiCandidate c1, ViterbiCandidate c2) {
		return ((c1.rankedScores * c2.rankedScores) / c2.rankedScores);
	}

	public static void initialize() throws IOException {

		allBooks = new HashMap<String, HashMap<Integer, ArrayList<ViterbiCandidate>>>();
		FileReader FR = new FileReader("SVM_files/rankPredictions.txt");
		BufferedReader BR = new BufferedReader(FR);

		FileReader FR2 = new FileReader("SVM_files/rankTestData.txt");
		BufferedReader BR2 = new BufferedReader(FR2);

		String line = BR2.readLine();
		String rankedScoreValue = BR.readLine();
		String actualNameOfBook = line.substring(line.lastIndexOf(',') + 1, line.length()).trim();
		Integer actualPageImage = Integer.parseInt(line.substring(line.indexOf('#') + 1, line.indexOf(',')).trim());
		allBooks.put(actualNameOfBook, new HashMap<Integer, ArrayList<ViterbiCandidate>>());
		allBooks.get(actualNameOfBook).put(actualPageImage, new ArrayList<ViterbiCandidate>());
		String candidateText = line.substring(line.indexOf(',') + 1, line.lastIndexOf(',')).trim();
		allBooks.get(actualNameOfBook).get(actualPageImage)
		.add(new ViterbiCandidate(candidateText, actualPageImage, Double.parseDouble(rankedScoreValue), 0));
		blankCandidate = new ViterbiCandidate("blank",actualPageImage,0.01,0);
	   allBooks.get(actualNameOfBook).get(actualPageImage).add(blankCandidate);

		listOfBookNames.add(actualNameOfBook);
		while (line != null) {
			Integer pageImage = Integer.parseInt(line.substring(line.indexOf('#') + 1, line.indexOf(',')));
			candidateText = line.substring(line.indexOf(',') + 1, line.lastIndexOf(',')).trim();
			String nameOfBook = line.substring(line.lastIndexOf(',') + 1, line.length()).trim();
			
			if (allBooks.containsKey(nameOfBook) == false) {
				allBooks.put(nameOfBook, new HashMap<Integer, ArrayList<ViterbiCandidate>>());
				actualNameOfBook = nameOfBook;
				listOfBookNames.add(actualNameOfBook);
			}
			if (allBooks.get(actualNameOfBook).containsKey(pageImage) == false) {
				allBooks.get(nameOfBook).put(pageImage, new ArrayList<ViterbiCandidate>());
				actualPageImage = pageImage;
			    allBooks.get(actualNameOfBook).get(actualPageImage).add(blankCandidate);
				
			}

			allBooks.get(actualNameOfBook).get(actualPageImage)
					.add(new ViterbiCandidate(candidateText, pageImage, Double.parseDouble(rankedScoreValue), 0));
			rankedScoreValue = BR.readLine();
			line = BR2.readLine();

		}
	
		for (int bookName = 0; bookName < listOfBookNames.size(); bookName++) {
			Integer[] keySet = new Integer[allBooks.get(listOfBookNames.get(bookName)).keySet().size()];
			keySet = allBooks.get(listOfBookNames.get(bookName)).keySet().toArray(keySet);
			
			for (int page = 0; page < keySet.length; page++) {
				Collections.sort(allBooks.get(listOfBookNames.get(bookName)).get(keySet[page]),
						new Comparator<ViterbiCandidate>() {
							public int compare(ViterbiCandidate c1, ViterbiCandidate c2) {
								if (c1.rankedScores == c2.rankedScores)
									return 0;
								return c1.rankedScores < c2.rankedScores ? -1 : 1;
							}
						});
				for (int candidate = 0; candidate < allBooks.get(listOfBookNames.get(bookName)).get(keySet[page])
						.size(); candidate++) {
					allBooks.get(listOfBookNames.get(bookName)).get(keySet[page]).get(candidate).rank = candidate + 1;
				}
				
                
                
			}
		}
	}

	
	
	public static Double findMAX(Integer page, int candidate, int lowerBound, int upperBound,
			HashMap<Integer, ArrayList<Double>> viterbiPages, HashMap<Integer, ArrayList<ViterbiCandidate>> book ) {
		Double maximum = 0.0;
		Integer realPage = 0;
		
			
			realPage = page;
			
		
		for (int i = lowerBound; i < upperBound; i++) {

			try {
				
				Double newValue = viterbiPages.get(realPage).get(i)
						* calculateTransitionProbabilities(book.get(realPage).get(i), book.get(realPage).get(candidate))
						* rankedScoresMap.get(realPage).get(candidate);

				if (newValue > maximum) {
					maximum = newValue;
				}
			} catch (NullPointerException NE) {
				if(page == keySet.length) System.out.println("It was the last page" +page);
				System.out.println(realPage);
				
				System.out.println(book.get(realPage).get(i));
				System.err.println(i+"\t"+page+"\t"+candidate+"\t"+lowerBound+"\t"+upperBound);
				
			    
			}
		   catch(IndexOutOfBoundsException IE){
			 
			   System.out.println(upperBound + "\t" + i +"\t"+ realPage + "\t"+ book.get(realPage).size());
			   if(page == keySet.length) System.out.println("It was the last page");
		}
			
		}
    
		return maximum;
	}
	
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
		
		for(int page=0;page<keySet.length;page++){
			for(int candidate=0;candidate<book.get(keySet[page]).size();candidate++){
				if(book.get(keySet[page]).get(candidate)==blankCandidate) {
				//System.out.println("Found a blank candidate");
				rankedScoresMap.get(keySet[page]).set(candidate, 0.35 );
				} else{ 
				rankedScoresMap.get(keySet[page]).set(candidate, (book.get(keySet[page]).
				get(candidate).rankedScores - minimumScore)/(maximumScore-minimumScore));}
				//System.out.print(" "+rankedScoresMap.get(keySet[page]).get(candidate));
			}
			
		}
		//System.out.println();
	}
	public static void doViterbi(HashMap<Integer, ArrayList<ViterbiCandidate>> book) {
	
		keySet = new Integer[book.keySet().size()];
		book.keySet().toArray(keySet);
		for (int i = 0; i < keySet.length; i++) {
			viterbiPages.put(keySet[i], new ArrayList<Double>());
			rankedScoresMap.put(keySet[i], new ArrayList<Double>());
			for (int k = 0; k < book.get(keySet[i]).size() +1; k++) {
				viterbiPages.get(keySet[i]).add(k, 1.0);
				rankedScoresMap.get(keySet[i]).add(k, 1.0);
			}
		}
		loadNormalizedRankedScores(keySet,book,rankedScoresMap);

		int index = 0;
		for (int candidate = 0; candidate < book.get(keySet[0]).size(); candidate++) {
		
			viterbiPages.get(keySet[0]).set(candidate,
					calculateTransitionProbabilities(book.get(keySet[0]).get(0), book.get(keySet[0]).get(candidate))
							* rankedScoresMap.get(keySet[0]).get(candidate));
		}

		for (int page = 1; page <= book.keySet().size() -1; page++) {
			for (int candidate = 0; candidate < book.get(keySet[page]).size(); candidate++) {

				viterbiPages.get(keySet[page]).set(candidate,
						findMAX(keySet[page], candidate, 0, book.get(keySet[page]).size(), viterbiPages,book));
			}
		}
		int lastPage = keySet.length - 1;
		int addedlastPage = lastPage +1;
		int lastCandidate = book.get(keySet[lastPage]).size() - 1;
		for (int candidate = 0; candidate < book.get(keySet[lastPage]).size(); candidate++) {
			
			viterbiPages.get(keySet[lastPage]).set(lastCandidate,
					calculateTransitionProbabilities(book.get(keySet[lastPage]).get(lastCandidate), book.get(keySet[lastPage]).get(candidate))
							* rankedScoresMap.get(keySet[lastPage]).get(candidate));
		}
		
		for (int page = 0; page < book.keySet().size(); page++) {
			int maxCandidate = findMaxArg(0, book.get(keySet[page]).size(), keySet[page]);
			System.out.print(" " + book.get(keySet[page]).get(maxCandidate).text + "[Rank: "+ book.get(keySet[page]).get(maxCandidate).rank + "]" );
		}
		
		//"[Rank: "+ book.get(keySet[page]).get(maxCandidate).rank + "]"
		//"[Score: "+ book.get(keySet[page]).get(maxCandidate).rankedScores + "]"
		viterbiPages.clear();

	}

	public static int findMaxArg(int upperBound, int lowerBound, int page) {
		int maxCandidate = 0;
		Double maxCandidateValue = 0.0;
		for (int i = upperBound; i < lowerBound; i++) {
			if (viterbiPages.get(page).get(i) > maxCandidateValue) {

				maxCandidateValue = viterbiPages.get(page).get(i);

				maxCandidate = i;
			}
		}
		return maxCandidate;
	}

	public static void main(String args[]) throws IOException {
		initialize();

		for (int i = 0; i < listOfBookNames.size(); i++) {
			System.out.println("Book: " + (i + 1) + " " + "," + "Name: " + listOfBookNames.get(i));
			doViterbi(allBooks.get(listOfBookNames.get(i)));
			System.out.println();
			System.out.println();

		}
		/*
		 * TODO Research new function that calculates transition probabilities.
		 */

	}

}
