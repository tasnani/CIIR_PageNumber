package pagenumber;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractFeaturesBook {
	 public static int lineNumber;
	 public static int wordNumber;
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
    
   
    public PageNumberCandidate(String text, PageInfo page) {
      this.text = text;
      this.page = page;
      this.features = new HashMap<>();
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

  /** Returns true if any letter is a number */
  public static boolean isMaybeNumber(String word) {
	boolean status=true;
    for (int i = 0; i < word.length(); i++) {
      if (!Character.isDigit(word.charAt(i))) {
        status=false;
      }
    }
    return status;
  }

  private static void extractFromPage(PageInfo page, List<Node> lines, ArrayList<PageNumberCandidate> candidates) {
      int totalNumberOfLines=lines.size();
      
	  
	  for (Node line : lines) {
    	lineNumber++;
      List<Node> words = XMLUtil.findTagsByName(line, "WORD");
      //System.out.println("\tLine: " + words.size());
      int totalNumberOfWords=words.size();
      for (Node word : words) {
    	wordNumber++;
        String text = word.getTextContent();
        Map<String, String> attr = XMLUtil.getAttributes(word);

        String[] coords = attr.get("coords").split(",");
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        if(isMaybeNumber(text)) {
          System.out.println("Candidate: "+text);
          PageNumberCandidate instance = new PageNumberCandidate(text, page);
          
          instance.setFeature("x-fraction", x / (double) page.width);
          instance.setFeature("y-fraction", y / (double) page.height);
          instance.setFeature("% thru line", (double)wordNumber/totalNumberOfWords);
          instance.setFeature("% thru word", (double)lineNumber/totalNumberOfLines);

          candidates.add(instance);
        }
      }
    }
  }

  public static void main(String[] args) {
    Map<String, Integer> featureNumbers = new HashMap<>();
    featureNumbers.put("x-fraction", 1);
    featureNumbers.put("sequence", 2);
    featureNumbers.put("y-fraction", 3);
    featureNumbers.put("% thru line", 4);
    featureNumbers.put("%  thru word", 5);

    List<List<PageNumberCandidate>> extracted = extract("jack00shergoog_djvu.xml");

    // TODO: calculate sequence here and set it on all candidates.
    boolean status=true;
    for(int i=0;i<extracted.size();i++){
    	for(int j=0;j<extracted.get(i).size();j++){
    		
    	}
    	
    }
    
    
    int total = 0;
    for (List<PageNumberCandidate> pageNumberCandidates : extracted) {
      for (PageNumberCandidate candidate : pageNumberCandidates) {
        total++;
      }
    }
    System.out.println("Extracted "+total+" candidates on "+extracted.size()+" pages.");


  }
}
