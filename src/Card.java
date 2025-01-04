import org.json.JSONObject;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


public class Card {
    private String front;
    private String back;
    private String rawForm;
    private boolean hasBeenProcessed;
    private Parser parser;
    private HtmlRenderer renderer;
    private JSONHandler streamer;
    

    public Card(String rawText, Parser pLink, HtmlRenderer hLink, JSONHandler sLink)
    {
        rawForm = rawText;
        hasBeenProcessed = false;
        parser = pLink;
        renderer = hLink;
        streamer = sLink;
    }


    private String processPipeline(String key, JSONObject input)
    {        
        String markdownSerialized = input.getString(key);
        String markdownDeserialized = streamer.deserialize(markdownSerialized);
        Node document = parser.parse(markdownDeserialized);
        String output = renderer.render(document);
        return(output);
    }


    public void preprocess()
    {
        if(!hasBeenProcessed)
        {            
            JSONObject input = new JSONObject(rawForm);            
            front = processPipeline("front", input);
            back = processPipeline("back", input);
        }    
    }


    public String getFront()  {preprocess();  return(front);}    
    public String getBack()  {preprocess();  return(back);}    
}
