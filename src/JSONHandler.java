class SerialHandler {
    private ReplacementRule[] rules;


    public SerialHandler(String[] regexList, String[] replacementStrings) throws Exception
    {
        
        if(regexList.length != replacementStrings.length)
        {throw new Exception("mismatching rule list lenghts!");}
        
        rules = new ReplacementRule[regexList.length];
        for(int index = 0; index < regexList.length; index++)
        {rules[index] = new ReplacementRule(regexList[index], replacementStrings[index]);}
    }

    public String applyRules(String input)
    {
        String buffer = input;
        String output = "";

        for(int index = 0; index < rules.length; index++)
        {buffer = rules[index].execute(buffer);}
        
        output = buffer;
        return(output);
    }
}



public class JSONHandler
{
    private SerialHandler serialize;
    private SerialHandler deserialize;


    public JSONHandler()
    {
        String[] serialRules = {""};
        String[] serialReplacements = {""};
        String[] deserialRules = {"\\\"", "\\\n", "\\\t", "\\/"};
        String[] deserialReplacements = {"\"", "\n", "\t", "/"};
        try
        {
            serialize = new SerialHandler(serialRules, serialReplacements);
            deserialize = new SerialHandler(deserialRules, deserialReplacements);
        }
        catch(Exception e)
        {
            System.err.println(e);
            System.out.println("IMPOSSIBLE!");
        }  //this exception shouldn't be reachable unless the embedded constants are wrong.
        
    }


    public String serialize(String input)  {return(serialize.applyRules(input));}
    public String deserialize(String input)  {return(deserialize.applyRules(input));}
}



class ReplacementRule
{
    private String matchingRegex; 
    private String replacementConstant;    

    public ReplacementRule(String matchOn, String replaceWith)
    {
        matchingRegex = matchOn;
        replacementConstant = replaceWith;
    }

    public String execute(String input)  {return(input.replaceAll(matchingRegex, replacementConstant));}
}