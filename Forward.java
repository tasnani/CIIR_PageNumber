package pagenumber;


import java.util.ArrayList;

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
ArrayList<ArrayList<Double>> forward=new ArrayList<ArrayList<Double>>();
ArrayList<ArrayList<Double>> rankedScores=new ArrayList<ArrayList<Double>>();
ArrayList<ArrayList<ViterbiCandidate>> book=new ArrayList<ArrayList<ViterbiCandidate>>();
ArrayList<ViterbiCandidate> allCandidates=new ArrayList<ViterbiCandidate>();

int candidateSize=0;
public void initialize(){

	allCandidates.add(new ViterbiCandidate("1",0,0.9));
	allCandidates.add(new ViterbiCandidate("e",0,0.4));
	allCandidates.add(new ViterbiCandidate("k",0,0.3));
	allCandidates.add(new ViterbiCandidate("f",1,0.5));
	allCandidates.add(new ViterbiCandidate("2",1,0.7));
	allCandidates.add(new ViterbiCandidate("c",1,0.3));
	allCandidates.add(new ViterbiCandidate("3",2,0.9));
	allCandidates.add(new ViterbiCandidate("err",2,0.2));
	allCandidates.add(new ViterbiCandidate("p",2,0.3));
	
	int index=0;
	for(int i=0;i<3;i++){
		book.add(new ArrayList<ViterbiCandidate>());
		forward.add(new ArrayList<Double>());
		rankedScores.add(new ArrayList<Double>());
		for (int j=0;j<3;j++){
			forward.get(i).add(j,0.0);
			book.get(i).add(j,allCandidates.get(index));
			rankedScores.get(i).add(j,allCandidates.get(index).rankedScores);
			index++;
			
		}
	}
	
	}

public void normalize(Double val){
	
}
public Double calculateTransitionProbabilities(ViterbiCandidate c1, ViterbiCandidate c2){
	return ((c1.rankedScores * c2.rankedScores)/c2.rankedScores);	
}
public Double findSUM(int page, int candidate, int lowerBound ,int upperBound, ArrayList<ArrayList<Double>> forward, ArrayList<ArrayList<Double>> rankedScores){
	Double value=0.0;
	for(int i=lowerBound;i<upperBound; i++){
		 value= value + forward.get(page-1).get(candidate) * calculateTransitionProbabilities(book.get(page).get(i),book.get(page).get(candidate)) * rankedScores.get(page).get(candidate);
	}
 return value;
}
public void calculateForward(){
      initialize();
for(int candidate=1;candidate<book.get(0).size();candidate++){
	forward.get(0).set(candidate, calculateTransitionProbabilities(book.get(0).get(0),book.get(0).get(candidate))*
			rankedScores.get(0).get(candidate));
}

for(int page=1;page<book.size();page++){
	for(int candidate=0;candidate<book.get(page).size();candidate++){
		forward.get(page).set(candidate, findSUM(page, candidate, 0,book.get(page).size(),forward,rankedScores));
	}
}
int lastPage=book.size()-1;
int lastCandidate=book.get(lastPage).size()-1;
forward.get(lastPage).set(lastCandidate, findSUM(lastPage,lastCandidate,0,book.get(lastPage).size(),forward,rankedScores));
 

}
public ArrayList<ArrayList<Double>> returnForward(){
	return forward;
}
public ArrayList<ArrayList<ViterbiCandidate>> returnBook(){
	return book;
}

public ArrayList<ArrayList<Double>> returnRankedScores(){
	return rankedScores;
}


}




