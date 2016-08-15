package pagenumber;

import java.util.ArrayList;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;

public class Viterbi {
ArrayList<ArrayList<Double>> viterbiPages;
ArrayList<ArrayList<ViterbiCandidate>> backpointer;
ArrayList<ArrayList<ViterbiCandidate>> book;
ArrayList<ArrayList<Double>> forward;
ArrayList<ArrayList<Double>> transitionProbabilities;
ArrayList<ArrayList<Double>> rankedScores;
public Viterbi(ArrayList<ArrayList<Double>> forward, ArrayList<ArrayList<ViterbiCandidate>> book, ArrayList<ArrayList<Double>> transitionProbabilties, ArrayList<ArrayList<Double>> rankedScores){
	this.forward=forward;
	this.book=book;
	this.rankedScores=rankedScores;
	this.transitionProbabilities=transitionProbabilities;
	
}




public Double findMAX(int page, int candidate, int lowerBound, int upperBound, ArrayList<ArrayList<Double>> viterbiPages, ArrayList<ArrayList<Double>> transitionProbabilities){
	Double maximum=0.0;
	for(int i=lowerBound;i<=upperBound;i++){
		Double newValue=viterbiPages.get(page-1).get(i) * (Double)transitionProbabilities.get(i).get(candidate);
		if(newValue>maximum){
			maximum=newValue;
		}
	}
	return maximum;
}

public void doViterbi(){
 for(int candidate=0;candidate<book.get(0).size();candidate++){
	forward.get(0).set(candidate, transitionProbabilities.get(0).get(candidate)* 
			rankedScores.get(0).get(candidate));
	backpointer.get(0).set(candidate, new ViterbiCandidate("", 0,0.0));
 }
 for(int page=1;page<book.size();page++){
	 for(int candidate=0;candidate<book.get(page).size();candidate++){
		 viterbiPages.get(page).set(candidate, findMAX(page,candidate,0,book.get(page).size(), viterbiPages,transitionProbabilities));
	 }
 }
 int lastPage=book.size()-1;
 int lastCandidate=book.get(lastPage).size()-1;
 viterbiPages.get(lastPage).set(lastCandidate, findMAX(lastPage+1, lastCandidate,0,book.get(lastPage).size(),viterbiPages,transitionProbabilities));
 
 
}

public static void main(String args[]){
	ArrayList<ArrayList<ViterbiCandidate>> book;
	ArrayList<ArrayList<Double>> rankedScores;
	ArrayList<ArrayList<Double>> forward;
	ArrayList<ArrayList<Double>> transitionProbabilities;
	Forward f=new Forward();
	f.calculateForward();
	book=f.returnBook();
	rankedScores=f.returnRankedScores();
	transitionProbabilities=f.returnTransitionProbabilities();
	forward=f.returnForward();
	
	Viterbi v=new Viterbi(forward,book,transitionProbabilities, rankedScores);
	
	/* TODO
	 * Research new function that calculates transition probabilities.
	 * Fix arg max at the end of the Viterbi pseudocode. It should return the domain(Viterbi Candidates) rather than the probability values. 
	 */
	
   
}


}