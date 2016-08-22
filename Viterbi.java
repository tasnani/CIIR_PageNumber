package pagenumber;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;
import pagenumber.generateData.PageNumberPrediction;
import pagenumber.Forward;

public class Viterbi {
static Forward f=new Forward();
static HashMap<Integer,ArrayList<Double>> viterbiPages=new HashMap<Integer,ArrayList<Double>>();
static HashMap<Integer,ArrayList<ViterbiCandidate>> book=new HashMap<Integer,ArrayList<ViterbiCandidate>>();
 static HashMap<Integer,ArrayList<Double>> rankedScoresMap=new HashMap<Integer,ArrayList<Double>>();
static ArrayList<ViterbiCandidate> page1=new ArrayList<ViterbiCandidate>();
static ArrayList<ViterbiCandidate> page2=new ArrayList<ViterbiCandidate>();
static ArrayList<ViterbiCandidate> page3=new ArrayList<ViterbiCandidate>();
static HashMap<String, HashMap<Integer,ArrayList<ViterbiCandidate>>> allBooks;
static ArrayList<String> listOfBookNames =new ArrayList<String>();
static Integer[] keySet;


public Viterbi(){
	
	
	/*
	for(int pages=0;pages<3;pages++){
		viterbiPages.put(pages, new ArrayList<Double>());
		for(int candidate=0;candidate<3;candidate++){
			viterbiPages.get(pages).add(candidate,1.0);
			
		}
	}
	*/
	
}

public static class ViterbiCandidate{
	String text;
	int page;
	static double rankedScores;
	public ViterbiCandidate(String t, int p, double score){
		this.text=t;
		this.page=p;
		this.rankedScores=score;
	}
}

	public static Double calculateTransitionProbabilities(ViterbiCandidate c1, ViterbiCandidate c2){
		return ((c1.rankedScores * c2.rankedScores)/c2.rankedScores);	
	}
public static void initialize() throws IOException{
	
 allBooks=new HashMap<String,HashMap<Integer,ArrayList<ViterbiCandidate>>>();
	FileReader FR=new FileReader("SVM_files/rankPredictions.txt");
	BufferedReader BR=new BufferedReader(FR);
	
	FileReader FR2=new FileReader("SVM_files/rankTestData.txt");
	BufferedReader BR2=new BufferedReader(FR2);
	
	String line=BR2.readLine();
	String rankedScoreValue=BR.readLine();
	String actualNameOfBook=line.substring(line.lastIndexOf(',')+1, line.length());
	Integer actualPageImage=Integer.parseInt(line.substring(line.indexOf('#')+1,line.indexOf(',')));
	allBooks.put(actualNameOfBook, new HashMap<Integer,ArrayList<ViterbiCandidate>>());
	allBooks.get(actualNameOfBook).put(actualPageImage, new ArrayList<ViterbiCandidate>());
	
	listOfBookNames.add(actualNameOfBook);
	while(line!=null){
		Integer pageImage=Integer.parseInt(line.substring(line.indexOf('#')+1,line.indexOf(',')));
		String candidateText=line.substring(line.indexOf(',')+1,line.lastIndexOf(','));
		String nameOfBook=line.substring(line.lastIndexOf(',')+1, line.length());
		if(allBooks.containsKey(nameOfBook)==false){
		allBooks.put(nameOfBook, new HashMap<Integer,ArrayList<ViterbiCandidate>>());
		actualNameOfBook=nameOfBook;
		listOfBookNames.add(actualNameOfBook);
		}
		if(allBooks.get(actualNameOfBook).containsKey(pageImage)==false){
			allBooks.get(nameOfBook).put(pageImage, new ArrayList<ViterbiCandidate>());
			actualPageImage=pageImage;
		}
		
		
	  allBooks.get(actualNameOfBook).get(actualPageImage).add(new ViterbiCandidate(candidateText,pageImage,Double.parseDouble(rankedScoreValue)));
	  rankedScoreValue=BR.readLine();
	  line=BR2.readLine();	
		
	}
	
	
	
	
	
	
	
	
	
	
	/*
	page1.add(0,new ViterbiCandidate("1",0,0.9));
	page1.add(1,new ViterbiCandidate("e",0,0.7));
	page1.add(2,new ViterbiCandidate("k",0,0.4));
	page2.add(0,new ViterbiCandidate("f",1,0.701));
	page2.add(1,new ViterbiCandidate("2",1,0.700));
	page2.add(2,new ViterbiCandidate("c",1,0.3));
	page3.add(0,new ViterbiCandidate("3",2,0.85));
	page3.add(1,new ViterbiCandidate("p",2,0.849));
	page3.add(2,new ViterbiCandidate("err",2,0.01));
	
	
	
	book.put(0, page1);
	book.put(1, page2);
	book.put(2, page3);
	
	/*
 	allCandidates.add(0,new ViterbiCandidate("1",0,0.9));
	allCandidates.add(1,new ViterbiCandidate("f",1,0.5));
	allCandidates.add(2,new ViterbiCandidate("3",2,0.9));
	
	allCandidates.add(3,new ViterbiCandidate("e",0,0.4));
	allCandidates.add(4,new ViterbiCandidate("2",1,0.7));
	allCandidates.add(5,new ViterbiCandidate("err",2,0.2));
	
	
	allCandidates.add(6,new ViterbiCandidate("k",0,0.3));
	allCandidates.add(7,new ViterbiCandidate("c",1,0.3));
	allCandidates.add(8,new ViterbiCandidate("p",2,0.3));
	*/
	
	/*nt index=0;
	for(int i=0;i<book.size();i++){
		rankedScoresMap.put(i,  new ArrayList<Double>());
		
	      for(int j=0;j<book.get(i).size();j++){
	    	  rankedScoresMap.get(i).add(j, book.get(i).get(j).rankedScores);
	    	
	    	  
	      }
	      Collections.sort(rankedScoresMap.get(i));
			
		}
	*/
	}
	

public static Double findMAX(Integer page, int candidate, int lowerBound, int upperBound, HashMap<Integer,ArrayList<Double>> viterbiPages){
	Double maximum=0.0;
	Integer realPage=0;
	if(page==keySet.length) {realPage=page-1;} 
	else {realPage=page;}
	for(int i=lowerBound;i<upperBound;i++){
		
		try{
		Double newValue=viterbiPages.get(realPage).get(i) * calculateTransitionProbabilities(book.get(realPage).get(i),book.get(realPage).get(candidate))*rankedScoresMap.get(realPage).get(candidate);
		
		if(newValue>maximum){
			maximum=newValue;
		}
		} catch(NullPointerException NE){
			return 0.0;
		}
	}
	
	return maximum;
}

public static void doViterbi(HashMap<Integer,ArrayList<ViterbiCandidate>> book){
	
	keySet = new Integer[book.keySet().size()];
	book.keySet().toArray(keySet);
	for(int i=0;i<keySet.length;i++){
		viterbiPages.put(keySet[i], new ArrayList<Double>());
		rankedScoresMap.put(keySet[i], new ArrayList<Double>() );
		for(int k=0;k<book.get(keySet[i]).size();k++){
			viterbiPages.get(keySet[i]).add(k,1.0);
			rankedScoresMap.get(keySet[i]).add(k,1.0);
		}
	}
	
	int index=0;
	for(int candidate=0;candidate<book.get(keySet[0]).size();candidate++){
		viterbiPages.get(keySet[0]).set(candidate, calculateTransitionProbabilities(book.get(keySet[0]).get(0),book.get(keySet[0]).get(candidate))* rankedScoresMap.get(keySet[0]).get(candidate));
	}
	
 for(int page=1;page<book.keySet().size();page++){
	 for(int candidate=0;candidate<book.get(keySet[page]).size();candidate++){
		 
		 viterbiPages.get(keySet[page]).set(candidate, findMAX(keySet[page],candidate,0
				 ,book.get(keySet[page]).size(), viterbiPages));
	 }
 }
 int lastPage=keySet.length -1;
 int lastCandidate=book.get(keySet[lastPage]).size()-1;
   viterbiPages.get(keySet[lastPage]).set(lastCandidate, findMAX(lastPage+1, lastCandidate,0,book.get(keySet[lastPage]).size(),viterbiPages));
   for(int page=0;page<book.keySet().size();page++){
	   System.out.print(" " +book.get(keySet[page]).get(findMaxArg(0,book.get(keySet[page]).size(),keySet[page])).text);
   }
   viterbiPages.clear();
 
}


public static int findMaxArg(int upperBound, int lowerBound, int page){
	int maxCandidate=0;
	Double maxCandidateValue=0.0;
	for(int i=upperBound; i<lowerBound;i++){
		if(viterbiPages.get(page).get(i)>maxCandidateValue){
			
			maxCandidateValue=viterbiPages.get(page).get(i);
			
			maxCandidate=i;
		}
	}
	return maxCandidate;
}

public static void main(String args[]) throws IOException{
	initialize();
	
	for(int i=0;i<listOfBookNames.size();i++){
		System.out.println("Book: "+(i+1)+" "+","+"Name: "+listOfBookNames.get(i));
		doViterbi(allBooks.get(listOfBookNames.get(i)));
		System.out.println();
		System.out.println();
		
	}
	/* TODO
	 * Research new function that calculates transition probabilities.
	 * Normalize all probabilities from 0 to 1.
	 * Incorporate real data here
	 * -ranked scores gotten
	 */
	
   
}

}
