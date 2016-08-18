package pagenumber;

import java.util.ArrayList;


import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;
import pagenumber.Forward;

public class Viterbi {
static Forward f=new Forward();
static ArrayList<ArrayList<Double>> viterbiPages=new ArrayList<ArrayList<Double>>();
static ArrayList<ArrayList<ViterbiCandidate>> backpointer;
static ArrayList<ArrayList<ViterbiCandidate>> book;
static ArrayList<ArrayList<Double>> forward;
ArrayList<ArrayList<Double>> transitionProbabilities;
static ArrayList<ArrayList<Double>> rankedScores;
public Viterbi(ArrayList<ArrayList<Double>> forward, ArrayList<ArrayList<ViterbiCandidate>> book, ArrayList<ArrayList<Double>> rankedScores){
	this.forward=forward;
	this.book=book;
	this.rankedScores=rankedScores;
	for(int pages=0;pages<3;pages++){
		viterbiPages.add(pages, new ArrayList<Double>());
		for(int candidate=0;candidate<3;candidate++){
			viterbiPages.get(pages).add(candidate,0.0);
			
		}
	}
	
	
}




public static Double findMAX(int page, int candidate, int lowerBound, int upperBound, ArrayList<ArrayList<Double>> viterbiPages){
	Double maximum=0.0;
	int realPage=page;
	for(int i=lowerBound;i<upperBound;i++){
		if(page==book.size()) realPage=page-1;
		Double newValue=viterbiPages.get(page-1).get(i) * f.calculateTransitionProbabilities(book.get(realPage).get(i),book.get(realPage).get(candidate));
		if(newValue>maximum){
			maximum=newValue;
		}
	}
	return maximum;
}

public static void doViterbi(){
 for(int candidate=0;candidate<book.get(0).size();candidate++){
	forward.get(0).set(candidate, f.calculateTransitionProbabilities(book.get(0).get(0), book.get(0).get(candidate))* 
			rankedScores.get(0).get(candidate));
	
 }
 for(int page=1;page<book.size();page++){
	 for(int candidate=0;candidate<book.get(page).size();candidate++){
		 viterbiPages.get(page).set(candidate, findMAX(page,candidate,1,book.get(page).size(), viterbiPages));
	 }
 }
 int lastPage=book.size()-1;
 int lastCandidate=book.get(lastPage).size()-1;
 viterbiPages.get(lastPage).set(lastCandidate, findMAX(lastPage+1, lastCandidate,0,book.get(lastPage).size(),viterbiPages));
   for(int page=0;page<book.size();page++){
	   System.out.println(book.get(page).get(findMaxArg(0,book.get(page).size(),page)).text);
   }
 
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

public static void main(String args[]){
	 
	
	f.calculateForward();
	book=f.returnBook();
	rankedScores=f.returnRankedScores();
	forward=f.returnForward();
	
	Viterbi v=new Viterbi(forward,book,rankedScores);
	doViterbi();
	
	/* TODO
	 * Research new function that calculates transition probabilities.
	 * Fix arg max at the end of the Viterbi pseudocode. It should return the domain(Viterbi Candidates) rather than the probability values. 
	 * Normalize all probabilities from 0 to 1.
	 */
	
   
}


}