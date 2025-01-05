import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class LoadData {    
    private Parser pLink;
    private HtmlRenderer hLink;
    private JSONHandler sLink;

    public LoadData()
    {
        pLink = Parser.builder().build();
        hLink = HtmlRenderer.builder().build();
        sLink = new JSONHandler();        
    }


    public CardStack loadCards(String targetFolder) throws Exception
    {//TODO: need to add additional processing to make sure this only handles json files, and nothing else!
        //search targetFolder        
        File directory = new File(targetFolder);
        String[] localFiles = directory.list();
        String[] normalCards = new String[localFiles.length - 1];
        String topicString = "";
        int tracker = 0;

        for(int index = 0; index < localFiles.length; index++)
        { //extract the topic file and seperate it from all the other files
            String element = localFiles[index];
            if(element.contains("topic.json"))
            {topicString = Main.loadFile(targetFolder.toString() + File.separator + element);}  //find some neat way to reuse this?
            else
            {
                normalCards[tracker] = targetFolder.toString() + File.separator + element;
                tracker++;
            }
        }

        return(new CardStack(topicString, normalCards, pLink, hLink, sLink));
    }
}
