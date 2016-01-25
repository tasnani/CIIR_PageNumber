package pagenumber;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;

public class SAXLocalNameCount extends DefaultHandler {
	 private Hashtable tags;
	 private Writer out;
	 List<Integer> paramList=new ArrayList<Integer>();
	 List<Integer> lineList=new ArrayList<Integer>();
	 String loc="";
	 int c=0;
	 int min=Integer.MAX_VALUE;
	    int possibility=0;

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
   
   c++;
    
    
    
    if("PARAM".equals(qName)){
    	
    	paramList.add(c);
   for(int i=0;i<atts.getLength();i++){
	  
	   System.out.println("attribute:"+atts.getValue(i));
	   System.out.println("qName:"+atts.getQName(i));
	   
	  // System.out.println(atts.getLength());
	   
   }
    }
    
    if("LINE".equals(qName)){
    	lineList.add(c);
    	
    	
      
    }
  
   /*
   
   int y=0;
   for(int i=0;i<paramList.size();i++){
	   
	   y=paramList.get(i) +1;
	   
	   for(int k=0;k<atts.getLength();k++){
		   if( && (y-paramList.get(i))<=7){
			   System.out.println("word"+atts.getValue(y));
			   System.out.println("qName:"+atts.getQName(y));
		   } else{
			   y++;
		   }
	   }
   }
    }
    */
}
/*
public void pageNumber(){
	int min=Integer.MAX_VALUE;
	int paramPossibility=0;
	int linePossibility=0;
				for(int i=0;i<paramList.size();i++){
					for(int k=0;k<lineList.size();k++){
						if(lineList.get(k)>paramList.get(i) && lineList.get(k)-paramList.get(i)<min ){
							min=lineList.get(k)-paramList.get(i);
							paramPossibility=paramList.get(i);
							linePossibility=lineList.get(k);
						}
					
					}
					System.out.println("paramPossibility="+paramPossibility);
					System.out.println("linePossibility="+linePossibility);
				}
	
}

*/
	    public void endDocument() throws SAXException {
	    	/*
	    	for(int i=0;i<paramList.size();i++){
	    		System.out.println(paramList.get(i));
	    	}
	    	*/
	    	/*
	    	for(int i=0;i<qNameList.size();i++){
	    		if(qNameList.get(i).equals("PARAM")){
	    			System.out.println("true"); }
	    		else{
	    			System.out.println("false");
	    		}
	    			}
	    			*/
	    	int min=Integer.MAX_VALUE;
	    	int paramPossibility=0;
	    	int linePossibility=0;
	    				for(int i=0;i<paramList.size();i++){
	    					for(int k=0;k<lineList.size();k++){
	    						if(lineList.get(k)>paramList.get(i) && lineList.get(k)-paramList.get(i)<min ){
	    							min=lineList.get(k)-paramList.get(i);
	    							paramPossibility=paramList.get(i);
	    							linePossibility=lineList.get(k);
	    						}
	    					
	    					}
	    					min=Integer.MAX_VALUE;
	    					System.out.println("paramPossibility="+paramPossibility);
	    					System.out.println("linePossibility="+linePossibility);
	    				}
	        Enumeration e = tags.keys();
	        while (e.hasMoreElements()) {
	            String tag = (String)e.nextElement();
	            int count = ((Integer)tags.get(tag)).intValue();
	            System.out.println("Local Name \"" + tag + "\" occurs " 
	                               + count + " times");
	        }  
	        
}
	    /*
	    public void printHastable(){
	    	for(int i=0;i<tags.size();i++){
	    		System.out.println(tags.get(i));
	    	}
	    }
	    */
}