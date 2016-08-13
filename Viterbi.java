package pagenumber;

import java.util.ArrayList;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;

public class Viterbi {
ArrayList<ArrayList<Double>> viterbiPages;
ArrayList<ArrayList<PageNumberCandidate>> backpointer;
ArrayList<ArrayList<PageNumberCandidate>> book;
ArrayList<ArrayList<Double>> forward;
ArrayList<ArrayList<Double>> transitionProbabilities;
ArrayList<ArrayList<Double>> rankedScores;
int candidateSize;
public void calculateTotalCandidatesN(){
candidateSize=0;
for(int i=0;i<viterbiPages.size();i++){
	candidateSize=candidateSize+viterbiPages.get(i).size();
}
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
	backpointer.get(0).set(candidate, new PageNumberCandidate(null, null));
 }
 for(int page=1;page<book.size();page++){
	 for(int candidate=0;candidate<book.get(page).size();candidate++){
		 viterbiPages.get(page).set(candidate, findMAX(page,candidate,0,book.get(page).size(), viterbiPages,transitionProbabilities));
	 }
 }
 int lastPage=book.size()-1;
 int lastCandidate=book.get(lastPage).size()-1;
 viterbiPages.get(lastPage).set(lastCandidate, findMAX(lastPage+1, lastCandidate,0,candidateSize,viterbiPages,transitionProbabilities));
 
 
}



}