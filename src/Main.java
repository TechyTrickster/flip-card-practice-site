//uses the code sample at https://docs.oracle.com/en/java/javase/17/docs/api/jdk.httpserver/com/sun/net/httpserver/package-summary.html as a base
//uses a suggested fix from BalusC at https://stackoverflow.com/questions/3732109/simple-http-server-in-java-using-only-java-se-api
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
    static CardStack[] stacks = stackDiscovery("../data");
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
        File location = new File(rootFolder);
        String[] possibilities = location.list();
        ArrayList<CardStack> collection = new ArrayList<CardStack>();
        LoadData generator = new LoadData();

        for(String element : possibilities)
        {
            File bufferDir = new File(element);
            if(bufferDir.isDirectory())  
            {
                String[] deepDirs = bufferDir.list();
                for(String strDir: deepDirs)
                {
                    if(strDir.contains("topic.json"))
                    {
                        try
                        {collection.add(generator.loadCards(strDir));}
                        catch(Exception e)
                        {
                            System.out.println("couldn't load card stack: " + element);
                            System.err.println(e);
                        }
                        
                    }
                }
            }
        }
        
        output = (CardStack[]) collection.toArray();
        return(output);
    }


    static class StackHandler implements HttpHandler
    {
        @Override
        public void handle(HttpExchange t) throws IOException
        {
            String pagePath = "../src-web/html/quiz.html";
            String errorPath = "../src-web/html/notFound.html";
            String chosenStack = t.getRequestURI().toString().replace("/stack/", "");
            CardStack selected = findStack(chosenStack);
            String response = "";

            if(selected == null)
            {
                response = loadFile(errorPath);
            }
            else
            {
                response = loadFile(pagePath);
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
            String pagePath = "../src-web/html/home.html";
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
        {if(element.getTitle() == name)  {output = element;}}
        return(output);
    }


    public static String loadFile(String name) throws IOException
    {
        Path handle = new File(name).getCanonicalFile().toPath();
        String output = new String(Files.readAllBytes(handle), StandardCharsets.UTF_8);                        
        return(output);
    }
}


