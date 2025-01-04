import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.json.JSONObject;
import org.commonmark.node.*;


public class JSONBasedObjectReader {
    public String rawForm;
    public Parser parser;
    public HtmlRenderer renderer;
    public JSONHandler streamer;
    public JSONObject jsonForm;


    public JSONBasedObjectReader(String rawText, Parser pLink, HtmlRenderer hLink, JSONHandler sLink)
    {        
        rawForm = rawText;
        parser = pLink;
        renderer = hLink;
        streamer = sLink;
        jsonForm = new JSONObject(rawForm);
    }


    public String processPipeline(String key)
    {        
        String markdownSerialized = jsonForm.getString(key);
        String markdownDeserialized = streamer.deserialize(markdownSerialized);
        Node document = parser.parse(markdownDeserialized);
        String output = renderer.render(document);
        return(output);
    }
}
