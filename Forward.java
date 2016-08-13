package pagenumber;


import java.util.ArrayList;

import pagenumber.ExtractFeaturesBook.PageNumberCandidate;

public class Forward {
ArrayList<ArrayList<Double>> forward;
ArrayList<ArrayList<Double>> transitionProbabilities;
ArrayList<ArrayList<Double>> rankedScores;
ArrayList<ArrayList<PageNumberCandidate>> book;
int candidateSize=0;
public void calculateTotalCandidatesN(){
candidateSize=0;
for(int i=0;i<forward.size();i++){
	candidateSize=candidateSize+forward.get(i).size();
}
}

public Double findSUM(int page, int candidate, int lowerBound ,int upperBound, ArrayList<ArrayList<Double>> forward, ArrayList<ArrayList<Double>> transitionProbabilities , ArrayList<ArrayList<Double>> rankedScores){
	Double value=0.0;
	for(int i=lowerBound;i<=upperBound; i++){
		 value= value + forward.get(page-1).get(i) * transitionProbabilities.get(i).get(candidate) * rankedScores.get(page).get(candidate);
	}
 return value;
}
public Double calculateForward(){
for(int candidate=0;candidate<forward.get(0).size();candidate++){
	forward.get(0).set(candidate, transitionProbabilities.get(0).get(candidate)*
			rankedScores.get(0).get(candidate));
}

for(int page=1;page<forward.size();page++){
	for(int candidate=0;candidate<candidateSize;candidate++){
		forward.get(page).set(candidate, findSUM(page, candidate, 0,forward.get(page).size(),forward, transitionProbabilities, rankedScores));
	}
}
int lastPage=forward.size()-1;
int lastCandidate=forward.get(lastPage).size()-1;
forward.get(lastPage).set(lastCandidate, findSUM(lastPage,lastCandidate,0,candidateSize,forward,transitionProbabilities,rankedScores));
return forward.get(lastPage).get(lastCandidate);

}


}




