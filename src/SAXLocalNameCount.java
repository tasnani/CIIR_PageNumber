package pagenumber;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;

public class SAXLocalNameCount extends DefaultHandler {
	 private Hashtable tags;
	 private Writer out;
	
	 List<String> paramList=new ArrayList<String>();
	 List<Integer> lineList=new ArrayList<Integer>();
	 List<Integer> lineHeader=new ArrayList<Integer>();
	 List<Integer> lineFooter=new ArrayList<Integer>();
	 List<Integer> lineHeaderEnds=new ArrayList<Integer>();
	 List<Integer> wordsList=new ArrayList<Integer>();
	 List<String> attributesList=new ArrayList<String>();
	
	 ArrayList<String> pagenumberpossList=new ArrayList<String>();
	 ArrayList<String> linetagnumberList=new ArrayList<String>();
	 ArrayList<String> wordtagnumberList=new ArrayList<String>();
	 ArrayList<String> pagesequenceList=new ArrayList<String>();
	 ArrayList<Integer> numbersperpageimageList=new ArrayList<Integer>();
	 ArrayList<Integer> totalwordsperlineList =new ArrayList<Integer>();
	 
	 
	
	
	 String loc="";
	 int c=0;
	 int lineTagNumber=0;
	 int wordTagNumber=0;
	 int wordTagsAtLine=0;
	 int templineCounter=0;
	 int wordTagsAtPrevLine=0;
	 int e=0;
	 int exceptions=0;
	 int min=Integer.MAX_VALUE;
	 int type=1;
	    int possibility=0;
	    String tempValue="";
	    
	 int linesperPage=0;
	 int linesperPage2=0;
	 String pageImage="";
	 

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException{
		
		
		SAXLocalNameCount SLNC=new SAXLocalNameCount(); 
		String filename = "jack00shergoog_djvu.xml";
		  if (filename == null) {
		        usage(); 
		    } 
		  SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
		    SAXParser saxParser = spf.newSAXParser();
		    XMLReader xmlReader = saxParser.getXMLReader();
		    xmlReader.setContentHandler(new SAXLocalNameCount());
		    xmlReader.parse(filename);
		   //. SLNC.pageNumber();
		   
		    
	}
	
	private static void usage() {
	    System.err.println("Usage: SAXLocalNameCount <file.xml>");
	    System.err.println("       -usage or -help = this message");
	    System.exit(1);
	}
	  public void startDocument() throws SAXException {
	        tags = new Hashtable();
	    }

public void startElement(String namespaceURI,
                         String localName,
                         String qName, 
                         Attributes atts)
    throws SAXException {
	String tempQName="";
	
	c++;
	
		
    String key = localName;
   
    Object value = tags.get(key);

    if (value == null) {
        tags.put(key, new Integer(1));
    } 
    else {
       int  count = ((Integer)value).intValue();
        count++;
        tags.put(key, new Integer(count));
    }
   
  

    
    
    if("PARAM".equals(qName)){
    	
    //	paramList.add(c);
    	
     
    	/*
          for(int i=0;i<atts.getLength();i++){
        	  
        	   
    	 	
    	 		tempValue=tempValue+atts.getValue(i);
    	 	
    	 	 
          }
          */
    	if(atts.getValue(atts.getIndex("name")).equals("PAGE")){
    		pageImage=atts.getValue(atts.getIndex("value"));
        }
          if(linesperPage>0){
    	  linesperPage2=linesperPage;}
    	
    	
    	linesperPage=0;
 
    		   
    	  


    }
    
    if("LINE".equals(qName)){
    	lineList.add(c);
    	lineTagNumber++;
    	wordTagsAtPrevLine=wordTagNumber;
    	wordTagsAtLine=wordTagNumber;
    	wordTagNumber=0;
    	wordTagsAtLine=0;
    	linesperPage++;
    	
    	
      
    }
    
    if("WORD".equals(qName)){
    	wordsList.add(c);
    	
    	 wordTagNumber++;
    	 attributesList.add(tempQName+"+"+tempValue);
    	     	
    }
    
    
   
  
    }

public void characters(char ch[], int start, int length)
	    throws SAXException {
	       boolean status=true;
	     // System.out.println("start characters : " +new  String(ch, start, length));
	     char number=' ';
	     String tempString=new String(ch,start,length);
	     
	     for (char c : tempString.toCharArray())
	     {
	         if (!Character.isDigit(c)){
	        	 status=false;
	        	
	         }
	     }
	     
	     if(status==true){
	    	 System.out.println("Page Number ? "+tempString +" Line Tag Number: "+ linesperPage+" Word Tag Number:"+ wordTagNumber+" Words in a line: "+ wordTagsAtPrevLine+"<PARAM> sequence : "+tempValue);
	    	 pagesequenceList.add(tempValue); 
	    	 tempValue="";
	    	pagenumberpossList.add(tempString);
	    	paramList.add(pageImage);
	    	String s=new String(Integer.toString(linesperPage));
	    	linetagnumberList.add(s);
	    	String t=new String(Integer.toString(wordTagNumber));
	    	wordtagnumberList.add(t);
	    	totalwordsperlineList.add(wordTagsAtPrevLine);
	   
	    	if(linesperPage2>0){
	    	numbersperpageimageList.add(linesperPage2);}
	    	
	    	
	    	
	    	
	     }
	 
	    } 

public void endElement(String uri, String localName, String qName)  
	      throws SAXException {  
	     
	     if (qName.equalsIgnoreCase("LINE") && c<100) {  
	      lineHeaderEnds.add(c);
	    
	     
	     }  
}

   
	    public void endDocument() throws SAXException {
	    	
	        Enumeration e = tags.keys();
	        while (e.hasMoreElements()) {
	            String tag = (String)e.nextElement();
	            int count = ((Integer)tags.get(tag)).intValue();
	            System.out.println("Local Name \"" + tag + "\" occurs " 
	                               + count + " times");
	        }  
	        System.out.println("------------------------------FEATURE 1: WHAT % OF THE PAGE IS THE POSSIBLE PAGE NUMBER FOUND IN? -------------------------------------");
		       int i=0;
		       try{
		       for(i=0;i<linetagnumberList.size();i++){
		    	   double pcgts=Double.parseDouble(linetagnumberList.get(i)) / numbersperpageimageList.get(i);
		    	  pcgts=pcgts*100;
		    	   System.out.println(pcgts +" %");
		    	   
		       }
		       }catch(ArithmeticException ae){
		    	   System.out.println(linetagnumberList.get(i) + "/" + numbersperpageimageList.get(i));
		       }
		       
	        System.out.println("------------------------------FEATURE 2: DOES THE POSSIBLE PAGE NUMBER N FOUND FALL IN N-1, N AND N+1 SEQUENCE OF PAGE NUMBERS BEFORE AND AFTER IT? -------------------------------------");
	       for(int k=1;k<=pagenumberpossList.size()-2;k++){
	    	   System.out.println(pagenumberpossList.get(k-1)+","+pagenumberpossList.get(k)+","+pagenumberpossList.get(k+1) );
	    	   if(Integer.parseInt(pagenumberpossList.get(k))==(Integer.parseInt(pagenumberpossList.get(k+1))-1) && Integer.parseInt(pagenumberpossList.get(k))==(Integer.parseInt(pagenumberpossList.get(k-1))+1)){
	    		   
	    		   
	    		   System.out.println("Number falls in sequence between n-1 and n+1 pages.");
	    	  
	    	      
	       }else if(Integer.parseInt(pagenumberpossList.get(k+1)) == Integer.parseInt(pagenumberpossList.get(k-1))-2 && pagenumberpossList.get(k).equals("")){
	    	   System.out.println("Missing number based on sequence is: "+String.valueOf(Integer.parseInt(pagenumberpossList.get(k+1))-1));
	       } else {
	    	   System.out.println("Number not in sequence.");
	       }
	     
	    }
	      System.out.println("------------------------------------ FEATURE 3: WHAT % THROUGH THE LINE IS THE POSSIBLE PAGE NUMBER FOUND IN?------------------------");
	       double pctgsLine=0.0;
	      for(int j=0;j<wordtagnumberList.size();j++){
	    	 pctgsLine= Double.parseDouble(wordtagnumberList.get(j)) / totalwordsperlineList.get(j);
	    	 pctgsLine=pctgsLine*100;
	    	 System.out.println(pctgsLine  +" %");
	      }
	      
	    
	      System.out.println("------------------------------------PAGE IMAGE NUMBER LIST----------------");
	    	   for(int k=0;k<paramList.size();k++){
	    		  
	    		   
	    		   int pos=paramList.get(k).indexOf("_");
	    		   int pos2=paramList.get(k).indexOf(".");
	    		   int found=0;
	    		 
	    		   for(int m=pos+1;m<=pos2;m++){
	    			   char currentChar=paramList.get(k).charAt(m);
	    			  
	    			   if(currentChar!='0'){ 
	    				   found=m;
	    				   break;
	    			   }
	    			   
	    		   }
	    		   paramList.set(k, paramList.get(k).substring(found,pos2));
	    		   System.out.println("PAGE_IMAGE: "+paramList.get(k));
	    	   }
	       System.out.println("Page Image Number List Size: "+paramList.size());
	       System.out.println("Page Number Candidate List Size: "+pagenumberpossList.size());
	    }
}