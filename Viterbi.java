package pagenumber;

import java.util.ArrayList;
import java.util.HashMap;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;
import pagenumber.Forward;

public class Viterbi {
static Forward f=new Forward();
static HashMap<Integer,ArrayList<Double>> viterbiPages=new HashMap<Integer,ArrayList<Double>>();
static HashMap<Integer,ArrayList<ViterbiCandidate>> book;
static HashMap<Integer,ArrayList<Double>> forward;
ArrayList<ArrayList<Double>> transitionProbabilities;
static HashMap<Integer,ArrayList<Double>> rankedScores;
public Viterbi(HashMap<Integer,ArrayList<ViterbiCandidate>>  book,HashMap<Integer,ArrayList<Double>> rankedScores){
	
	this.book=book;
	this.rankedScores=rankedScores;
	
	for(int pages=0;pages<3;pages++){
		viterbiPages.put(pages, new ArrayList<Double>());
		for(int candidate=0;candidate<3;candidate++){
			viterbiPages.get(pages).add(candidate,1.0);
			
		}
	}
	
	
}




public static Double findMAX(int page, int candidate, int lowerBound, int upperBound, HashMap<Integer,ArrayList<Double>> viterbiPages){
	Double maximum=0.0;
	int realPage=0;
	if(page==book.size()) {realPage=page-1;} else 
	 {realPage=page;}
	for(int i=lowerBound;i<upperBound;i++){
		
		Double newValue=viterbiPages.get(realPage).get(i) * f.calculateTransitionProbabilities(book.get(realPage).get(i),book.get(realPage).get(candidate))*rankedScores.get(realPage).get(candidate);
		
		if(newValue>maximum){
			maximum=newValue;
		}
	}
	
	return maximum;
}

public static void doViterbi(){
	for(int candidate=0;candidate<book.get(0).size();candidate++){
		viterbiPages.get(0).set(candidate, f.calculateTransitionProbabilities(book.get(0).get(0),book.get(0).get(candidate))* rankedScores.get(0).get(candidate));
	}
	
 for(int page=1;page<book.size();page++){
	 for(int candidate=0;candidate<book.get(page).size();candidate++){
		 viterbiPages.get(page).set(candidate, findMAX(page,candidate,0
				 ,book.get(page).size(), viterbiPages));
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
		System.out.println("Page:"+ page +" " +"value: "+viterbiPages.get(page).get(i));
		if(viterbiPages.get(page).get(i)>maxCandidateValue){
			
			maxCandidateValue=viterbiPages.get(page).get(i);
			
			maxCandidate=i;
		}
	}
	return maxCandidate;
}

public static void main(String args[]){
	 
	
	f.initialize();
	book=f.returnBook();
	rankedScores=f.rankedScoresHashMap;
	
	
	Viterbi v=new Viterbi(book,rankedScores);
	v.doViterbi();
	
	/* TODO
	 * Research new function that calculates transition probabilities.
	 * Fix arg max at the end of the Viterbi pseudocode. It should return the domain(Viterbi Candidates) rather than the probability values. 
	 * Normalize all probabilities from 0 to 1.
	 */
	
   
}


}