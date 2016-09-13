package pagenumber;


import java.util.ArrayList;
import java.util.HashMap;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;

public class Forward {
	
public static class ViterbiCandidate{
	String text;
	int page;
	double rankedScores;
	public ViterbiCandidate(String t, int p, double score){
		this.text=t;
		this.page=p;
		this.rankedScores=score;
	}
}

HashMap<Integer,ArrayList<ViterbiCandidate>> bookHashMap=new HashMap<Integer,ArrayList<ViterbiCandidate>>();
ArrayList<ViterbiCandidate> page1=new ArrayList<ViterbiCandidate>();
ArrayList<ViterbiCandidate> page2=new ArrayList<ViterbiCandidate>();
ArrayList<ViterbiCandidate> page3=new ArrayList<ViterbiCandidate>();
HashMap<Integer, ArrayList<Double>> forwardHashMap=new HashMap<Integer,ArrayList<Double>>();
HashMap<Integer, ArrayList<Double>> rankedScoresHashMap=new HashMap<Integer,ArrayList<Double>>();



int candidateSize=0;
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
	
	
	
	bookHashMap.put(0, page1);
	bookHashMap.put(1, page2);
	bookHashMap.put(2, page3);
	
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
	for(int i=0;i<bookHashMap.size();i++){
		rankedScoresHashMap.put(i,  new ArrayList<Double>());
		forwardHashMap.put(i, new ArrayList<Double>());
	      for(int j=0;j<bookHashMap.get(i).size();j++){
	    	  rankedScoresHashMap.get(i).add(j, bookHashMap.get(i).get(j).rankedScores);
	    	  forwardHashMap.get(i).add(j,1.0);
	    	  
	      }
			
		}
	}
	
	
	

public void normalize(Double val){
	
}
public Double calculateTransitionProbabilities(ViterbiCandidate c1, ViterbiCandidate c2){
	return ((c1.rankedScores * c2.rankedScores)/c2.rankedScores);	
}
public Double findSUM(int page, int candidate, int lowerBound ,int upperBound, HashMap<Integer,ArrayList<Double>> forward, HashMap<Integer,ArrayList<Double>> rankedScores){
	Double value=0.0;
	int realPage=0;
	if(page==bookHashMap.size()){
	 realPage=bookHashMap.size()-1; } else {
	 realPage=page;}
	for(int i=lowerBound;i<upperBound; i++){
		 value= value + forwardHashMap.get(realPage).get(candidate) * calculateTransitionProbabilities(bookHashMap.get(realPage).get(i),bookHashMap.get(realPage).get(candidate)) * rankedScoresHashMap.get(realPage).get(candidate);
	}
 return value;
}
public void calculateForward(){
      initialize();
for(int candidate=1;candidate<bookHashMap.get(0).size();candidate++){
	forwardHashMap.get(0).set(candidate, calculateTransitionProbabilities(bookHashMap.get(0).get(0),bookHashMap.get(0).get(candidate))* rankedScoresHashMap.get(0).get(candidate));
}

for(int page=1;page<bookHashMap.size();page++){
	for(int candidate=0;candidate<bookHashMap.get(page).size();candidate++){
		forwardHashMap.get(page).set(candidate, findSUM(page, candidate, 0,bookHashMap.get(page).size(),forwardHashMap,rankedScoresHashMap));
	}
}
int lastPage=bookHashMap.size();
int lastCandidate=bookHashMap.get(lastPage-1).size()-1;
forwardHashMap.get(lastPage-1).set(lastCandidate, findSUM(lastPage,lastCandidate,0,bookHashMap.get(lastPage-1).size(),forwardHashMap,rankedScoresHashMap));
 

}
public HashMap<Integer, ArrayList<Double>> returnForward(){
	return forwardHashMap;
}
public HashMap<Integer,ArrayList<ViterbiCandidate>> returnBook(){
	return bookHashMap;
}

public HashMap<Integer, ArrayList<Double>> returnRankedScores(){
	return rankedScoresHashMap;
}


}




