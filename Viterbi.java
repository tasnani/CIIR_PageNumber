package pagenumber;

import java.util.ArrayList;
import java.util.HashMap;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;
import pagenumber.Forward.ViterbiCandidate;
import pagenumber.Forward;

public class Viterbi {
static Forward f=new Forward();
static HashMap<Integer,ArrayList<Double>> viterbiPages=new HashMap<Integer,ArrayList<Double>>();
static HashMap<Integer,ArrayList<ViterbiCandidate>> book=new HashMap<Integer,ArrayList<ViterbiCandidate>>();
static HashMap<Integer,ArrayList<Double>> forward;
ArrayList<ArrayList<Double>> transitionProbabilities;
static HashMap<Integer,ArrayList<Double>> rankedScores=new HashMap<Integer,ArrayList<Double>>();
ArrayList<ViterbiCandidate> page1=new ArrayList<ViterbiCandidate>();
ArrayList<ViterbiCandidate> page2=new ArrayList<ViterbiCandidate>();
ArrayList<ViterbiCandidate> page3=new ArrayList<ViterbiCandidate>();

public Viterbi(){
	
	
	
	for(int pages=0;pages<3;pages++){
		viterbiPages.put(pages, new ArrayList<Double>());
		for(int candidate=0;candidate<3;candidate++){
			viterbiPages.get(pages).add(candidate,1.0);
			
		}
	}
	
	
}


public void initialize(){
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
	int index=0;
	for(int i=0;i<book.size();i++){
		rankedScores.put(i,  new ArrayList<Double>());
		
	      for(int j=0;j<book.get(i).size();j++){
	    	  rankedScores.get(i).add(j, book.get(i).get(j).rankedScores);
	    	
	    	  
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
	 
	
	
	
	
	
	Viterbi v=new Viterbi();
	v.initialize();
	v.doViterbi();
	
	/* TODO
	 * Research new function that calculates transition probabilities.
	 * Fix arg max at the end of the Viterbi pseudocode. It should return the domain(Viterbi Candidates) rather than the probability values. 
	 * Normalize all probabilities from 0 to 1.
	 */
	
   
}


}