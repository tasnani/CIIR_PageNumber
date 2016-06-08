import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.*;
public class formatTruthData2 {
   String file;
	String line;

	String[] row=new String[2];
	ArrayList<String> actualPageNumber=new ArrayList<String>();
	ArrayList<String> pageImageNumber=new ArrayList<String>();
	//String[][] truthTokenData=new String[1000000000][1000000000];
	//HashMap<Integer,String> truthTokenHashMap=new HashMap<Integer,String>();

	String[] temp=new String[2];
	static int flag=0;
	 int c=0;
	public void openFile() throws IOException{
		file="alibra-annotated.txt";
		file="truth_data/"+file;
     BufferedReader BR=new BufferedReader(new FileReader(file));
		
		line=BR.readLine();
		while(line!=null){
			 StringTokenizer st = new StringTokenizer(line);
			while(st.hasMoreTokens()){
				temp[c]=st.nextToken();
				c++;
			}	
			c=0;
			
			actualPageNumber.add(temp[0]);
			pageImageNumber.add(temp[1]);
			
			line=BR.readLine();
		}
		
	
		/*
	for(int i=0;i<pageImageNumber.size();i++){
	System.out.println(actualPageNumber.get(i)+" "+pageImageNumber.get(i));
	}
	*/
	}
	public void expand(){
		boolean isDone=false;
		Integer changedValue =new Integer(0);
		
		for(int i=0;i<actualPageNumber.size();i++){
			if((i>0 && i< actualPageNumber.size()-1) && (actualPageNumber.get(i).equals("blank") ||actualPageNumber.get(i).equals("cont"))){
				if(Character.isDigit(actualPageNumber.get(i-1).charAt(0))){
					changedValue=Integer.parseInt(actualPageNumber.get(i-1))+1;
					if(actualPageNumber.get(i).equals("cont")) actualPageNumber.add(i, changedValue.toString()); else 
					actualPageNumber.set(i,changedValue.toString());
					isDone=true;
				}else 
				if(!isDone && Character.isDigit(actualPageNumber.get(i+1).charAt(0))){
					changedValue=Integer.parseInt(actualPageNumber.get(i+1))-1;
					if(actualPageNumber.get(i).equals("cont")) actualPageNumber.add(i, changedValue.toString()); else 
					actualPageNumber.set(i, changedValue.toString());
					isDone=true;
				}
			}
			
			else if(i==0 && actualPageNumber.get(i).equals("blank")){
				int found=0;
				loop:
				for(int k=1;k<actualPageNumber.size();k++){
					if(Character.isDigit(actualPageNumber.get(i).charAt(0))){
						found=k;
						break loop;
					}
				}
				if(found==1){
					if( Character.isDigit(actualPageNumber.get(found).charAt(0))){
						changedValue=Integer.parseInt(actualPageNumber.get(found))-1;
						actualPageNumber.set(i, changedValue.toString());
						
					}
				}else{
					Integer val=new Integer(Integer.parseInt(actualPageNumber.get(found)));
					for(int k=found-1;k>=0;k-- ){
						val=val-1;
						actualPageNumber.set(k, val.toString());
					}
				}
				
			}
				
				else if(i==actualPageNumber.size()-1 && actualPageNumber.get(i).equals("blank")){
					int found2=0;
					loop:
					for(int k=actualPageNumber.size()-2;k>=0;k--){
						if(Character.isDigit(actualPageNumber.get(i).charAt(0))){
							found2=k;
							break loop;
						}
					}
					if(found2==actualPageNumber.size()-2){
						if( Character.isDigit(actualPageNumber.get(found2).charAt(0))){
							changedValue=Integer.parseInt(actualPageNumber.get(found2))+1;
							actualPageNumber.set(i, changedValue.toString());
							
						}
					}else{
						Integer val2=new Integer(Integer.parseInt(actualPageNumber.get(found2)));
						for(int k=found2+1;k<=i;k++){
							val2=val2+1;
							actualPageNumber.add(k, val2.toString());
						}
					}
				
			}
			
				}
		System.out.println("RESULTS-----");
		for(int i=0;i<actualPageNumber.size();i++){
			System.out.println(actualPageNumber.get(i));
		}
		
		
			}
			
	
	
	
	public static void main(String args[]) throws IOException{
		//System.out.println("Enter truth data:");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	
		formatTruthData2 f=new formatTruthData2();
		f.openFile();
		//String k="cont	20";
		//System.out.println(k.contains("cont"));
		f.expand();
		
	}
}
