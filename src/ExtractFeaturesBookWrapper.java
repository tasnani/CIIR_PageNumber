package pagenumber;

//import pagenumber.generateDataWrapper;

import org.w3c.dom.Document;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pagenumber.ViterbiWrapper.ViterbiCandidate;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.Runtime;
public class ExtractFeaturesBookWrapper {
	 public static int lineNumber;
	 public static int wordNumber;
     public static int totalNumberOfLines;
     public static int repeat1=0;
     public static int repeat2=0;
   
     public static int pageImageNumber=1;
     public static int x=0;
     public static int y=0;
     static generateDataWrapper TD = new generateDataWrapper();
     static String[] publicArguments = new String[3];
     
  public static class PageInfo {
    String book;
    int imageNumber;
    int width;
    int height;
  }

  public static class PageNumberCandidate {
    public final String text;
    public final PageInfo page;
    public final HashMap<String, Double> features;
    public Integer number;
    public Integer nearlyNumber;
    int wordNumber;
    
    public PageNumberCandidate(String text, PageInfo page) {
      this.text = text;
      this.page = page;
      this.features = new HashMap<>();
      try {
    	  this.number = Integer.parseInt(text);
      } catch (NumberFormatException nfe) {
    	  this.number = null;
      }
      try {
    	  this.nearlyNumber = Integer.parseInt(takeOnlyDigits(text));
      } catch (NumberFormatException nfe) {
    	  this.nearlyNumber = null;
      }
    }
    // for: a page number "12," we would like to know its value is 12 for our sequence feature
    // this is dumb: "12.72" will become "1272"
    public String takeOnlyDigits(String input) {
    	StringBuilder digits = new StringBuilder();
    	for (char c : input.toCharArray()) {
    		if(Character.isDigit(c)) {
    			digits.append(c);
    		}
    	}
    	return digits.toString();
    }
    
    public double get(String name) {
    	Double value = features.get(name);
    	if(value == null) {
    		throw new NullPointerException("Bad feature: "+name+" for book="+page.book+" and page="+page.imageNumber);
    	}
    	return value;
    }

    public void setFeature(String name, double value) {
      features.put(name, value);
    }
    
    public String book() {
      return page.book;
    }
    public int imageNumber() {
      return page.imageNumber;
    }
  }

  /** Code that deals with XML library... */
  public static class XMLUtil {
	 
    public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static Map<String,String> getAttributes(Node xmlNode) {
      Map<String,String> output = new HashMap<>();
      NamedNodeMap attributes = xmlNode.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++) {
        Node item = attributes.item(i);
        assert item.getNodeType() == Node.ATTRIBUTE_NODE : "Attribute map should have attribute nodes!";
        output.put(item.getNodeName(), item.getTextContent());
      }
      return output;
    }

    /** Walk through the tree collecting all tags that match */
    public static List<Node> findTagsByName(Node start, String tag) {
      ArrayList<Node> matching = new ArrayList<>();
      findTagsByNameRecursive(start, tag, matching);
      return matching;
    }

    /** Recursive step through the tree collecting all tags that match */
    private static void findTagsByNameRecursive(Node start, String tag, List<Node> found) {
      NodeList children = start.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node item = children.item(i);
        if(tag.equals(item.getNodeName())) {
          found.add(item);
        } else {
          findTagsByNameRecursive(item, tag, found);
        }
      }
    }
  }

  /** Open a file and extract all candidates, {@link #extractFromPage} */
  public static List<List<PageNumberCandidate>> extract(String book) {
    ArrayList<List<PageNumberCandidate>> candidatesPerPage = new ArrayList<>();
    try {
      Document bookXML = XMLUtil.factory.newDocumentBuilder().parse(new File(book));
      List<Node> pages = XMLUtil.findTagsByName(bookXML, "OBJECT");
      repeat1=0;
      repeat2=0;
      for (Node page : pages) {
        List<Node> lines = XMLUtil.findTagsByName(page, "LINE");
       
        Map<String, String> attributes = XMLUtil.getAttributes(page);
        String usemap = attributes.get("usemap");
        assert(usemap.endsWith(".djvu"));
        usemap = usemap.substring(0, usemap.length()-5);
        int underscore = usemap.lastIndexOf('_');
        if(underscore < 0) {
          throw new RuntimeException("Bad page information?: "+usemap+" "+attributes);
        }
        pageImageNumber++;
        String bookId = usemap.substring(0, underscore);
        String imageNumberText = usemap.substring(underscore+1);
        int imageNumber = Integer.parseInt(imageNumberText);
        //System.out.println("Page: "+lines.size()+" "+attributes);

        PageInfo info = new PageInfo();
        info.book = bookId;
        info.imageNumber = imageNumber;
        info.width = Integer.parseInt(attributes.get("width"));
        info.height = Integer.parseInt(attributes.get("height"));

        ArrayList<PageNumberCandidate> candidates = new ArrayList<>();
     
        extractFromPage(info, lines, candidates);
        candidatesPerPage.add(candidates);
       
      }
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    return candidatesPerPage;
  }
  public static boolean isRomanNumeral(String word){
	
	  
	  Pattern p=Pattern.compile("(?i)^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
	  Matcher m=p.matcher(word);
	 if(m.matches()) return true;
	 return false;
	  
  }
  
  public static boolean avoidAllCharacters(String word){
	  boolean status=true;
	  
	 
	  for(int i=0;i<word.length();i++){
		  if(Character.isDigit(word.charAt(i))){
			  status=false;
			  break;
		  }
	  }
	  return status;
  }
  /** Returns true if any letter is a number */
  public static boolean isMaybeNumber(String word) {
	boolean status=true;
    for (int i = 0; i < word.length(); i++) {
      if (!Character.isDigit(word.charAt(i))) {
        status=false;
        break;
      }
    }
    if(word.equals("0") || word.equals("00") || 
     word.equals("000") || word.equals("0000") || 
    word.equals("00000")) return false;
    return status;
  }
  
  public static boolean doesLookLikeANumber(String word){
	  boolean status=false;
	  char[] charArray=word.toCharArray();
	  int numberOfCharacters=0;
	  int numberOfDigits=0;
	  if(charArray.length<4){
	  for (int i = 0; i < charArray.length; i++) {
	     if(!Character.isDigit(charArray[i])){
	    	 numberOfCharacters++;
	     }else if(Character.isDigit(charArray[i])){
	    	 numberOfDigits++;
	     }
	    }
	  if(numberOfCharacters<numberOfDigits && numberOfDigits>=1 && numberOfCharacters>=1){
		  status=true;
	  }
	  }  
	  return status;
  }

  private static void extractFromPage(PageInfo page, List<Node> lines, ArrayList<PageNumberCandidate> candidates) {
     
	  totalNumberOfLines=lines.size();
	  
	  for (Node line : lines) {
    	lineNumber++;
      List<Node> words = XMLUtil.findTagsByName(line, "WORD");
      int totalNumberOfWords=words.size();
      for (Node word : words) {
    	wordNumber++;
        String text = word.getTextContent();
        Map<String, String> attr = XMLUtil.getAttributes(word);

        String[] coords = attr.get("coords").split(",");
         x = Integer.parseInt(coords[0]);
         y = Integer.parseInt(coords[1]);
      
          if(!avoidAllCharacters(text) || isRomanNumeral(text)){
            if(text.equals("1")) repeat1++;
            if(text.equals("I")) repeat2++;
            
        	if((repeat1<=1 || !text.equals("1")) && ( repeat2<=1 || !text.equals("I"))){
         //System.out.println("Candidate: "+text);
         
         text.replaceAll("\\p{Punct}|\\d", "");
         PageNumberCandidate instance = new PageNumberCandidate(text, page);
         // System.out.println(instance.book());
          if(isMaybeNumber(text)) 
        	  instance.setFeature("is a number", 1); 
          else
        	  instance.setFeature("is a number", 0);
          if(doesLookLikeANumber(text)){
        	  instance.setFeature("does look like a number", 1); //first condition is a 1
          }else{
        	  instance.setFeature("does look like a number", 0);
          }
         
          //PageNumberCandidate instance = new PageNumberCandidate(text, page);
        //instance.setFeature("x-fraction", 0);
         instance.setFeature("x-fraction", x / (double) page.width);
       // instance.setFeature("y-fraction", 0);
        //instance.setFeature("sequence", 0);
         instance.setFeature("y-fraction", y / (double) page.height);
         // instance.setFeature("downward % thru line", 0);
         instance.setFeature("downward % thru line", (double)wordNumber/totalNumberOfWords);
        // instance.setFeature("upward % thru line", 0);
         instance.setFeature("upward % thru line", (totalNumberOfWords-(double)wordNumber)/totalNumberOfWords);
        // instance.setFeature("downward % thru page",0);
          instance.setFeature("downward % thru page", (double)lineNumber/totalNumberOfLines);
         //instance.setFeature("upward % thru page", 0);

          instance.setFeature("upward % thru page", ((double)totalNumberOfLines-lineNumber)/totalNumberOfLines);

          candidates.add(instance);
        	
        	}
        }
      }
      wordNumber=0;
    }
	 lineNumber=0;
  }

  public static void main(String[] args) throws IOException {
    Map<String, Integer> featureNumbers = new HashMap<>();
    featureNumbers.put("is a number", 1);
    featureNumbers.put("does it look like a number", 2);
    featureNumbers.put("x-fraction", 3);
    featureNumbers.put("sequence", 4);
    featureNumbers.put("y-fraction", 5);
    featureNumbers.put("downward % thru line", 6);
    featureNumbers.put("upward % thru line",7);
    featureNumbers.put("downward %  thru page", 8);
    featureNumbers.put("upward % thru page", 9);
  
    
  
    List<List<ExtractFeaturesBookWrapper.PageNumberCandidate>> extracted = extract(args[0]);
    calculateSequence(extracted);	
    
    generateDataWrapper TD=new generateDataWrapper();
    // args[0] is "/Users/tanasn/Documents/workspace/CIIR_PageNumber/raw_djvu/gunnartaleofnors00boyerich_djvu.xml"
    //args[1] is "/Users/tanasn/Documents/workspace/CIIR_PageNumber/SVM_files/rankTestData.txt"
    //args[2] is "/Users/tanasn/Documents/modelFile.txt "
    publicArguments[0] = args[0];
    publicArguments[1] = args[1];
    publicArguments[2] = args[2];
    
    PrintWriter output = new PrintWriter(args[1]);
    PrintWriter qidOutput=new PrintWriter("/Users/tanasn/Documents/workspace/CIIR_PageNumber/SVM_files/query_ids.txt");
    //TD.generateTrainingData(args[1], extracted); 
    TD.generateTestData(extracted,qidOutput, output);
    //TD.generateRankData(output,qidOutput,extracted);
  
    output.close();
    qidOutput.close();
 
    callSVMRank();
    
    ViterbiWrapper VW = new ViterbiWrapper();
    HashMap<Integer, ArrayList<ViterbiCandidate>> oneBook = VW.initialize("/Users/tanasn/Documents/workspace/CIIR_PageNumber/SVM_files/rankPredictions.txt"
    		, "/Users/tanasn/Documents/workspace/CIIR_PageNumber/SVM_files/rankTestData.txt");
    VW.doViterbi(oneBook);
    
   
     //TD.sortRankPredictions2("SVM_files/rankPredictions.txt", "SVM_files/rankTestData.txt", "SVM_files/query_ids.txt" );
   
   
    
    
    
  
   
  
   //TD.accuracyofTest();
    
     
  }
    public static void calculateSequence(List<List<PageNumberCandidate>> extracted){
    	
   
    // TODO: calculate sequence here and set it on all candidates.
    	//System.out.println("Progressing calculating sequence");
    
    	for(int page=0;page<extracted.size();page++){ //moving in per page
    		boolean statusBefore=false;
    		boolean statusAfter=false;
    		boolean exceptionCaught=false;

    		List<PageNumberCandidate> currentPage = extracted.get(page);
    		for(int candidate=0;candidate<currentPage.size();candidate++){
    			//moving in per token within the page
    			PageNumberCandidate currentCandidate = currentPage.get(candidate);
    			currentCandidate.setFeature("sequence", 0);
    			if(page+1 >= extracted.size()) {
    				continue;
    			}
    			List<PageNumberCandidate> nextPage = extracted.get(page+1);

    			if(page==0){
    				for(int k=0;k<nextPage.size();k++){
    					try{ 
    						//System.out.println(nextPage.get(k).text);
    						if(Integer.parseInt(currentCandidate.text.toString())==Integer.parseInt(nextPage.get(k).text.toString())-1){
    							statusAfter=true;
    						}
    					} catch(NumberFormatException n){
    						//System.out.println("caught number format exception");
    						exceptionCaught=true;
    						currentCandidate.setFeature("sequence", 0);
    						break;
    					}

    				}
    				if(exceptionCaught==true){ 
    					//System.out.println("set the sequence feature to 0");	
    					currentCandidate.setFeature("sequence", 0);
    					//System.out.println(currentCandidate.features.get("sequence"));
    				} else{
    					if(statusBefore){
    						currentCandidate.setFeature("sequence", 1);}
    					else if(!statusBefore){
    						currentCandidate.setFeature("sequence", 0);}
    					else {
    						currentCandidate.setFeature("sequence", 0);}
    					//System.out.println(currentCandidate.features.get("sequence")==null);
    					//System.out.println(currentCandidate.features.get("sequence"));
    					exceptionCaught=false;
    				}
    			} else 
    				if(page==extracted.size()-1){
    					for(int k=0;k<extracted.get(page-1).size();k++){
    						try{
    							if(Integer.parseInt(currentCandidate.text.toString())==Integer.parseInt(extracted.get(page-1).get(k).text.toString())+1){
    								statusAfter=true;
    							}
    						}catch(NumberFormatException n){
    							exceptionCaught=true;
    							currentCandidate.setFeature("sequence", 0);
    							break;
    						}



    					}
    					if(exceptionCaught==true){ currentCandidate.setFeature("sequence", 0);} else{
    						if(statusAfter)
    							currentCandidate.setFeature("sequence", 1);
    						else if(!statusAfter)
    							currentCandidate.setFeature("sequence", 0);
    						else 
    							currentCandidate.setFeature("sequence", 0);
    						exceptionCaught=false;
    					}
    				} else{




    					for(int k=0;k<extracted.get(page-1).size();k++){
    						try{
    							if(Integer.parseInt(currentCandidate.text.toString())==Integer.parseInt(extracted.get(page-1).get(k).text.toString())+1){
    								statusBefore=true;
    							}

    						}catch(NumberFormatException n){
    							exceptionCaught=true;
    							currentCandidate.setFeature("sequence", 0);
    							break;
    						}


    					}

    					for(int k=0;k<nextPage.size();k++){
    						try{
    							if(Integer.parseInt(currentCandidate.text.toString())==Integer.parseInt(nextPage.get(k).text.toString())-1){
    								statusAfter=true;
    							}
    						}catch(NumberFormatException n){
    							exceptionCaught=true;
    							currentCandidate.setFeature("sequence", 0);
    							break;

    						}

    						if(exceptionCaught==true) {currentCandidate.setFeature("sequence", 0);} else{
    							if(statusBefore && statusAfter) {currentCandidate.setFeature("sequence", 1);
    							//System.out.println(extracted.get(i).get(j).text);
    							}else if(statusBefore && !statusAfter){
    								currentCandidate.setFeature("sequence", 1);
    							}else if(!statusAfter && statusBefore){
    								currentCandidate.setFeature("sequence", 1);
    							}
    							else if(!statusBefore && !statusAfter) {currentCandidate.setFeature("sequence", 0);}else{
    								currentCandidate.setFeature("sequence", 0);
    							}
    						}
    						exceptionCaught=false;
    					}
    				}
    			statusBefore=false;
    			statusAfter=false;
    			exceptionCaught=false;

    		}


    	}


    } 
    
    public static void callSVMRank() throws IOException{
    	Runtime runtime = Runtime.getRuntime();
    	Process process = runtime.exec("/Users/tanasn/Documents/svm_rank_classify"
    			+ publicArguments[1]
    			+ publicArguments[2]
    			+ "/Users/tanasn/Documents/workspace/CIIR_PageNumber/SVM_files/rankPredictions.txt");
    }
   
  
}
