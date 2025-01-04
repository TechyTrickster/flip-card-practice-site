import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class CardStack extends JSONBasedObjectReader{    
    private String title;
    private String description;
    private Card[] collection;
    private int cardCount;


    public CardStack(String topicData, String[] fileList, Parser pLink, HtmlRenderer hLink, JSONHandler sLink)
    {
        super(topicData, pLink, hLink, sLink);
        title = processPipeline("title");
        description = processPipeline("description");
        cardCount = fileList.length;
        collection = new Card[cardCount];

        for(int index = 0; index < fileList.length; index++)
        {
            String element = fileList[index];

            try
            {
                
                String data = new String(Files.readAllBytes((new File(element)).toPath()), StandardCharsets.UTF_8);
                Card bufferCard = new Card(data, pLink, hLink, sLink);
                collection[index] = bufferCard;
            }
            catch(Exception e)
            {
                System.err.println(e);
                System.err.println("couldn't process file: " + element);
            }
            

        }        
        
    }

    
    public String getTitle()  {return title;}    
    public String getDescription()  {return description;}    
    public Card[] getCollection()  {return collection;}


    public void loadCardSet()
    {
        for(int index = 0; index < collection.length; index++)
        {collection[index].preprocess();}
    }

}
