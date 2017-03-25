import java.util.ArrayList;
import java.util.Queue;

public class Parser
{
  private Node root = new Node(null, null);
  private String iString;
  private ReflectionsHandler handler;
  private Lexer aLex;
  private ArrayList<Lexer.Token> result = new ArrayList<Lexer.Token>();
  private ArrayList<Lexer.Token> evalList = new ArrayList<Lexer.Token>();

  public Parser(ReflectionsHandler aHandler, String input)
  {
    iString = input;
    Lexer aLex = new Lexer();
    handler = aHandler;   
    Parse(aLex.lex(input));
	  
  
  }

  // One of the rules is still broken when testing multi-embedded functions
  public void Parse(ArrayList<Lexer.Token> tokens)
  {
    int tokensSize = (tokens.size()-1);
    Lexer.Token cToken;
    Node cNode = root;
    //System.out.println(tokens);#####################

    for (int i = 0; i <= tokensSize; i++){
      cToken = tokens.get(i);
      if (cToken.type.name().equals("ERROR")) { //
        System.err.println("Unexpected character found");
        continue;
      }
      if (cToken.type.name().equals("LPAR")) {
        if (cNode.getChild() != null) {
          Node temp = cNode.getChild();
          cNode.createChild(temp.getToken());
          cNode.getChild().setChild(temp);
          cNode = cNode.getChild();
        }
        else {
          cNode.createChild(null);
        }
        cNode = cNode.getChild();
        continue;
      }
      if (cToken.type.name().equals("ID")) {
        cNode.setToken(cToken);
        cNode.createChild(null);
        cNode = cNode.getChild();
        continue;
      }
      if (cToken.type.name().equals("INTEGER") || cToken.type.name().equals("STRING") || cToken.type.name().equals("FLOAT")) {
        if (cNode.getToken() != null) {
          if (cNode.getChild() != null) {
            Node temp = cNode.getChild();
            cNode.createChild(null);
            cNode.getChild().setChild(temp);
            cNode = cNode.getChild();
          }
          else {
            cNode.createChild(null);
            cNode = cNode.getChild();
          }
        }
        cNode.setToken(cToken);
        continue;
      }
      if (cToken.type.name().equals("RPAR")) {
        cNode = cNode.getParent();
        continue;
      }
    }
    traverseTree();
    //printParse();#############################
    try {
      System.out.println(evaluateTree(evalList));
    }
    catch (Exception e)
    {
      System.err.println("Error calling evaluateTree");
    }
  }

  public void traverseTree()
  {
    Node cNode = root;

    do {
      if (cNode.getToken() != null)
      {
        result.add(cNode.getToken());
        evalList.add(cNode.getToken());
      }
      if (cNode.getChild() == null) {
        break;
      }
      else {
        cNode = cNode.getChild();
      }
    } while (true);
  }

  public String evaluateTree(ArrayList<Lexer.Token> evalList) throws NoSuchMethodException
  {
    ArrayList<Lexer.Token> exprTemp = new ArrayList<Lexer.Token>();
    ArrayList<String> strTemp;
    int treeSize = (evalList.size()-1);
    int funcPos;
    int numArgs;
    Lexer.Token temptoken;
    Lexer.Token temp;
    Lexer.Token tempPar;

    System.out.println(evalList);
    if (hasFunc(result) == false) {
      return iString;
    }

    for (int i = 0; i <= treeSize; i++)
    {
      temp = result.get(i);
      if (temp.type.name().equals("ID")) { // Needs to be able to know if there are more function calls in the rest of the input list. If yes, call this. If no, call some other block of code.
        try {
          handler.findMethod(temp.data);
        }
        catch (NoSuchMethodException e) {
          System.err.println("Matching function for \"" + temp.data + "\" (offset " + temp.offset + ") not found.");
        }
        numArgs = handler.paramCount(temp.data);
        if (hasFuncParams(i, numArgs, evalList) == false) {
          strTemp = makeStringlist(evalList);
          return (handler.evaluate(strTemp)).toString();
        }
        else if(hasFuncParams(i, numArgs, evalList) == true) {
          System.out.println("Evaluating with params..");
          return(evaluateTree(makeTokList(funcAt(i, numArgs, evalList), numArgs, evalList)));
        }
      }
    }
    return "Error evaluating given expression";
  }

  public boolean hasFunc(ArrayList<Lexer.Token> list)
  {
    int treeSize = (list.size()-1);
    Lexer.Token temp;

    for (int i = 0; i <= treeSize; i++)
    {
      temp = list.get(i);
      if (temp.type.name().equals("ID"))
        return true;
    }
    return false;
  }


  // Generates subexpression for a function that has no functions as arguments, so it can be directly evaluated.
  public ArrayList<String> makeStringlist(ArrayList<Lexer.Token> tlist)
  {
    ArrayList<String> aList = new ArrayList<String>();
    int size = tlist.size();
    String temp;

    for (int i = 0; i < size; i++)
    {
      temp = tlist.get(i).data;
      aList.add(temp);
    }
    return aList;
  }

  // Generates subexpression for a function that has a function call as an argument. This is recursively called by evaluateTree.
  public ArrayList<Lexer.Token> makeTokList(int index, int numParams, ArrayList<Lexer.Token> tlist)
  {
    ArrayList<Lexer.Token> aList = new ArrayList<Lexer.Token>();
    int size = tlist.size();
    Lexer.Token temp;

    for (int i = index; i <= (index + numParams); i++)
    {
      temp = tlist.get(i);
      aList.add(temp);
    }
    return aList;
  }

  public boolean hasFuncParams(int index, int numParams, ArrayList<Lexer.Token> list)
  {
    Lexer.Token temp;
    for (int i = index+1; i <= (index + numParams); i++)
    {
      temp = list.get(i);
      if (temp.type.name().equals("ID")) {
        return true;
      }
    }
    return false;
  }

  public int funcAt(int index, int numParams, ArrayList<Lexer.Token> list)
  {
    Lexer.Token temp;
    for (int i = index+1; i<= (index + numParams); i++)
    {
      temp = list.get(i);
      if (temp.type.name().equals("ID")) {
        return i;
      }
    }
    return -1;
  }

  // For debugging parse traversal
  public void printParse()
  {
    Node cNode = root;

    do {
      if (cNode.getToken() != null) {
        System.out.println(cNode.getToken().data + " in node " + cNode);
      }
      else {
        System.out.println("Node " + cNode + " is empty.");
      }
      if (cNode.getChild() == null) {
        System.out.println("Node " + cNode + " has no child. Terminating traversal..");
        break;
      }
      cNode = cNode.getChild();
    } while (true);
  }


}
