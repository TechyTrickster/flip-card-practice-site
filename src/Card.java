import org.json.JSONObject;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


public class Card extends JSONBasedObjectReader{
    private String front;
    private String back;    
    private boolean hasBeenProcessed;    
    

    public Card(String rawText, Parser pLink, HtmlRenderer hLink, JSONHandler sLink)
    {
        super(rawText, pLink, hLink, sLink);        
        hasBeenProcessed = false;        
    }


    public void preprocess()
    {
        if(!hasBeenProcessed)
        {                        
            front = processPipeline("front");
            back = processPipeline("back");
            hasBeenProcessed = true;
        }    
    }


    public String getFront()  {preprocess();  return(front);}    
    public String getBack()  {preprocess();  return(back);}    
}
