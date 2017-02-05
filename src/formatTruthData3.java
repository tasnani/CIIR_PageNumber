package pagenumber;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class formatTruthData3 {
	String file;
	String line;
	String[] temp=new String[2];
	int c=0;
	ArrayList<String> actualPageNumber=new ArrayList<String>();
	ArrayList<String> pageImageNumber=new ArrayList<String>();
	public void openFile() throws IOException{
	
		file="truth_data/leanti-annotated.txt";
		
		   BufferedReader BR=new BufferedReader(new FileReader(file));
			
			line=BR.readLine();
			while(line!=null){
				 StringTokenizer st = new StringTokenizer(line);
					while(st.hasMoreTokens()){
						temp[c]=st.nextToken();
						c++;
					}	
					c=0;
					System.out.println(temp[0]);
					actualPageNumber.add(temp[0]);
					pageImageNumber.add(temp[1]);
					
					line=BR.readLine();
		
			}
			BR.close();
			/*
			for(int i=0;i<pageImageNumber.size();i++){
				System.out.println(actualPageNumber.get(i)+" "+pageImageNumber.get(i));
				}
				*/
		
			
	}
	
	public void filter(){
		String currAP;
		String currPI;
		boolean repeated=false;
		
		for(int i=0;i<actualPageNumber.size();i++){
			if( actualPageNumber.get(i).contains("blank") || actualPageNumber.get(i).contains("cont"))
			{
				if(i-1>=0 && Character.isDigit(actualPageNumber.get(i-1).charAt(0))){
					
				System.out.println(actualPageNumber.get(i-1) +" "+ pageImageNumber.get(i-1));}
				repeated=false; 
				currAP=actualPageNumber.get(i); currPI=pageImageNumber.get(i);
				System.out.println(currAP +" "+ currPI);
				repeated=true;
				
				
				if(!repeated && i+1<=actualPageNumber.size()-1 && Character.isDigit(actualPageNumber.get(i+1).charAt(0))){
					System.out.println(actualPageNumber.get(i+1)+" "+ pageImageNumber.get(i+1));
				}
				}
			}
		System.out.println("-------------------------------------------------------------");
	}
	
	public void uniqueExpand(){
		
		
		int c=389;
		int index=409;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		for(int i=423;i<=444;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		/*
		actualPageNumber.add(index, "blank");
		pageImageNumber.add(index, "201");
		
		actualPageNumber.add(++index, "blank");
		pageImageNumber.add(index, "202");
		
		
	
		index=index+1;
		
		c=181;
		for(int i=204;i<=231;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		/*
*/ /*
		index=index+5;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=79;
		for(int i=86;i<=96;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
       
		index=index+5;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=95;
		for(int i=102;i<=152;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		
		
		index=index+5;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=151;
		for(int i=158;i<=163;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+4;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=161;
		for(int i=168;i<=182;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=179;
		for(int i=186;i<=220;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		/*
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=258;
		for(int i=290;i<=302;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++; }
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=272;
		for(int i=306;i<=314;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=282;
		for(int i=318;i<=340;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=306;
		for(int i=344;i<=392;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=356;
		for(int i=396;i<=469;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+4;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=432;
		for(int i=474;i<=528;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=488;
		for(int i=532;i<=564;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=522;
		for(int i=568;i<=584;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		/*
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=134;
		for(int i=141;i<=147;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=143;
		for(int i=150;i<=151;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		/*
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=53;
		for(int i=61;i<=81;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=76;
		for(int i=84;i<=111;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=106;
		for(int i=114;i<=121;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=115;
		for(int i=125;i<=138;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=131;
		for(int i=141;i<=171;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=164;
		for(int i=174;i<=201;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+1;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=183;
		for(int i=203;i<=209;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=191;
		for(int i=213;i<=221;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=202;
		for(int i=224;i<=247;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=226;
		for(int i=248;i<=253;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=232;
		for(int i=256;i<=292;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=271;
		for(int i=295;i<=333;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=312;
		for(int i=336;i<=372;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+3;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=351;
		for(int i=375;i<=391;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=361;
		for(int i=395;i<=420;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		index=index+2;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		c=389;
		for(int i=423;i<=444;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		/*
		actualPageNumber.add(index,"306");
		
		actualPageNumber.add(++index,"307");
		
		actualPageNumber.add(++index,"308");
		
		actualPageNumber.add(++index,"309" );
		
		actualPageNumber.add(++index, "310");
		
		index++;
		c=311;
		actualPageNumber.remove(index);
		pageImageNumber.remove(index);
		for(int i=322;i<=504;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		*/
		
		/*
		 * for cairng-annotated.txt
		actualPageNumber.remove(1);
		pageImageNumber.remove(1);
		int c=3;
		int index=1;
		for(int i=13;i<=130;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		System.out.println(index+ actualPageNumber.get(index));
		actualPageNumber.set(index, "121");
		actualPageNumber.remove(121);
		pageImageNumber.remove(121);
		index=121;
		c=3;
		for(int i=133;i<=149;i++){
			pageImageNumber.add(index,new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString());
			index++;
			c++;
		}
		
		*/
		
		/* 
		 * 
		 * for annual-annotated.txt
		actualPageNumber.remove(1);
		pageImageNumber.remove(1);
		int c=5;
		for(int i=6;i<=213;i++){
			pageImageNumber.add(new Integer(i).toString());
			actualPageNumber.add(new Integer(c).toString());
			c++;
		}
		
		for(int i=0;i<pageImageNumber.size();i++){
			System.out.println(actualPageNumber.get(i)+" "+pageImageNumber.get(i));
			}
		*/
		/*
		 * 
		 * for belfry-annotated.txt
		actualPageNumber.remove(1);
		pageImageNumber.remove(1);
		int c=4;
		int index=1;
		for(int i=12;i<=120;i++){
			pageImageNumber.add(index, new Integer(i).toString());
			actualPageNumber.add(index, new Integer(c).toString());
			c++;
			index++;
		}
		
		
		
		actualPageNumber.set(110, new Integer(c).toString());
		actualPageNumber.set(111, new Integer(c+1).toString());
		
		actualPageNumber.set(113,"116");
		index=114;
		c=116;
		for(int i=124;i<=252;i++){
			pageImageNumber.add(index, new Integer(i).toString());
			actualPageNumber.add(index,new Integer(c).toString() );
			c++;
			index++;
		}
		
		
		actualPageNumber.set(index, new Integer(c).toString());
		actualPageNumber.set(++index, new Integer(c+1).toString());
	index=index+2;
		actualPageNumber.set(index,"248");
		pageImageNumber.set(index, "256");
		//actualPageNumber.remove(++index);
		//pageImageNumber.remove(++index);
		
		index++;
	  
	    c=249;
	    for(int i=256;i<=340;i++){
	    	pageImageNumber.add(index, new Integer(i).toString());
	    	actualPageNumber.add(index, new Integer(c+1).toString());
	    	c++;
			index++;
	    }
	*/
		
		
		
		
		
		
		
		
		
		
		for(int i=0;i<pageImageNumber.size();i++){
			System.out.println(actualPageNumber.get(i)+" "+pageImageNumber.get(i));
			}
		System.out.println((index)+ actualPageNumber.get(index)); 
	
	}
	
	public static void main(String args[]) throws IOException{
		
		
		
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
	
		formatTruthData3 f=new formatTruthData3();
		f.openFile();
		//f.filter();
		//f.uniqueExpand();
	}
}
