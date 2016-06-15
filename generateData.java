package pagenumber;
import pagenumber.ExtractFeaturesBook;
import pagenumber.ExtractFeaturesBook.PageNumberCandidate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class generateData {
String line="";

HashMap<Integer,String> truthDataMap=new HashMap<Integer,String>();
String token;
	public void generateTrainingData(String truthData,List<List<PageNumberCandidate>> book) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(truthData));
		
				line=br.readLine();
			     
			while(line!=null)
			{
				String[] splitted=line.split(" ");
				
				
				truthDataMap.put(Integer.parseInt(splitted[1].trim()), splitted[0].trim());
				line=br.readLine();
			}
		   br.close();
			
		for(List<PageNumberCandidate> b: book){
			
			for( PageNumberCandidate PNC : b){
				
				
				try{
					if(truthDataMap.get(new Integer(PNC.imageNumber())).equals(Integer.parseInt(PNC.text))){
						token="+1";
					}else{
						token="-1";
					}
					
					
				}catch(NullPointerException e){
					System.out.println("nullpointer");
					token="-1";
				}
			
			
			System.out.println(token+" "+"1:"+PNC.features.get("x-fraction")+" "+"2:"+PNC.features.get("y-fraction")+" "+"3:"+PNC.features.get("% thru line")+" "+"4:"+PNC.features.get("% thru page")+" "+"5:"+PNC.features.get("sequence") +"# Page_Image= "+PNC.imageNumber()+"Actual_Page= "+PNC.text);
		}
		}
	}
	public void generateTestData(List<List<PageNumberCandidate>> book){
for(List<PageNumberCandidate> b: book){
			
			for( PageNumberCandidate PNC : b){

			System.out.println(0+" "+"1:"+PNC.features.get("x-fraction")+" "+"2:"+PNC.features.get("y-fraction")+" "+"3:"+PNC.features.get("% thru line")+" "+"4:"+PNC.features.get("% thru page")+" "+"5:"+PNC.features.get("sequence") +"# Page_Image= "+PNC.imageNumber()+"Actual_Page= "+PNC.text);
		}
	}
	
	}
	
}
