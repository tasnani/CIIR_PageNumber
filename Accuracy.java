package pagenumber;
import pagenumber.ExtractFeaturesBook.PageNumberCandidate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Accuracy {
	String line="";
	String line2="";
	double right=0;
	int lineNumber=0;
	
	
 public void checkAccuracy(String file, String file2) throws IOException{
	 
	
	 BufferedReader br=new BufferedReader(new FileReader(file));
	 BufferedReader br2 =new BufferedReader(new FileReader(file2));
	 line=br.readLine();
	 line2=br.readLine();
	 while(line!=null){
		 if( line.charAt(0)=='-' && line2.charAt(0)==line.charAt(0)){
			 right++;
		 }
		 if(Character.isDigit(line.charAt(0)) && line2.charAt(0)=='+'){
			 right++;
		 }
		 line=br.readLine();
		 line2=br.readLine();
		 lineNumber++;
	 }
	 System.out.println("Right predictions:"+right);
	 
	 double pctgs=(right/lineNumber)*100;
	 System.out.println("Predicted percentage:"+pctgs + " %");
	
 }
}
