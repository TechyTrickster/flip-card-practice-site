//uses the code sample at https://docs.oracle.com/en/java/javase/17/docs/api/jdk.httpserver/com/sun/net/httpserver/package-summary.html as a base
//uses a suggested fix from BalusC at https://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
//TODO need to replicate the context aware root folder detection boiler plate that I created in python for java.  different java test tools seem to have different ideas about what the executing directory should actually be.
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main 
{
    static CardStack[] stacks = stackDiscovery("./data");
    public static void main(String[] args) throws Exception {
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 50);
        server.createContext("/home", new HomeHandler());
        server.createContext("/stack/", new StackHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }


    private static CardStack[] stackDiscovery(String rootFolder)
    {
        CardStack[] output;
        File location = null;
        try
        {
            System.out.println(System.getProperty("user.dir"));
            location = new File(rootFolder).getCanonicalFile();
            System.out.println(location.getCanonicalPath());
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.out.println("couldn't resolve data folder path");
        }
        
        String[] possibilities = location.list();
        ArrayList<CardStack> collection = new ArrayList<CardStack>();
        LoadData generator = new LoadData();

        for(String element : possibilities)
        {
            File bufferDir = new File(location.toString() + File.separator + element);
            if(bufferDir.isDirectory())  
            {
                String[] deepDirs = bufferDir.list();
                for(String strDir: deepDirs)
                {
                    if(strDir.contains("topic.json"))
                    {
                        try
                        {
                            String validStackPath = bufferDir.toString();
                            CardStack buffer = generator.loadCards(validStackPath);
                            buffer.loadCardSet(); //TODO should make this a background, non-halting process to improve startup time on larger collections.
                            collection.add(buffer);
                            break; //once the topic file is found, no other files in the current folder need examination
                        }
                        catch(Exception e)
                        {
                            System.out.println("couldn't load card stack: " + element);
                            System.out.println("fatal error!");
                            System.err.println(e);
                            System.exit(-1);
                        }
                        
                    }
                }
            }
        }
        
        output = collection.toArray(new CardStack[collection.size()]);
        return(output);
    }


    static class StackHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange t) throws IOException
        {
            System.out.println("stack");
            System.out.println(t.getRequestURI());
            String pagePath = "./src-web/html/quiz.html";
            String errorPath = "./src-web/html/notFound.html";
            String chosenStack = t.getRequestURI().toString().replace("/stack/", "");
            CardStack selected = findStack(chosenStack);
            String response = "";

            if(selected == null)
            {
                System.out.println("error!");
                response = loadFile(errorPath);
            }
            else
            {
                System.out.println("real page");
                response = loadFile(pagePath);
                String dataBlob = generateStackHTMLBlob(selected);
                response = response.replace("cardData", dataBlob);
            }

            
            
            t.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());            
            os.close();
        }
    }


    static class HomeHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange t) throws IOException
        {
            String pagePath = "./src-web/html/home.html";
            String response = loadFile(pagePath);
            t.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


    public static CardStack findStack(String name)
    {
        CardStack output = null;
        for(CardStack element : stacks)
        {
            System.out.println(element.getUrl());
            if(element.getUrl().equals(name))  {output = element; break;}
        }
        return(output);
    }


    public static String loadFile(String name) throws IOException
    {
        Path handle = new File(name).getCanonicalFile().toPath();
        String output = new String(Files.readAllBytes(handle), StandardCharsets.UTF_8);                        
        return(output);
    }


    public static String generateStackHTMLBlob(CardStack input)
    {
        String output = "";
        for(Card element : input.getCollection())
        {
            String buffer = element.getFront() + "\n" + element.getBack();
            output = output + "\n" + buffer;
        }
        return(output);
    }
}


