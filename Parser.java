import java.util.ArrayList;
import java.util.Stack;


public class Parser
{
  private Node root = new Node(null, null);
  private String iString;
  private ReflectionsHandler handler;
  private Lexer aLex;
  private ArrayList<Lexer.Token> result = new ArrayList<Lexer.Token>(); // initial traversal result
  private ArrayList<Lexer.Token> evalList = new ArrayList<Lexer.Token>(); // list that is actually evaluated

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
    int parCount = 0;
    Lexer.Token cToken;
    Node cNode = root;
    //System.out.println(tokens);#####################

    for (int i = 0; i <= tokensSize; i++){
      cToken = tokens.get(i);
      if (cToken.type.name().equals("ERROR")) { //
        System.out.println(iString);
        EArgs.unexpectedError(cToken.offset, cToken.data);
        return;
      }
      if (cToken.type.name().equals("LPAR")) {
        parCount++;
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
        parCount--;
        cNode = cNode.getParent();
        continue;
      }
    }
    if (parCount != 0) {
      System.err.println("Mismatched parenthesis");
      return;
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
    Stack<Lexer.Token> evalStack = buildStack();
    ArrayList<String> expr = new ArrayList<String>();
    String value = "";
    Lexer.Token temp;
    Lexer.Token newTok;

    do {
      temp = evalStack.pop();
      if (temp.type.name().equals("INTEGER") || temp.type.name().equals("STRING") || temp.type.name().equals("FLOAT"))
      {
        addtoFront(expr, temp.data);
      }
      else if (temp.type.name().equals("ID"))
      {
        addtoFront(expr, temp.data);
        value = handler.evaluate(expr).toString();
        newTok = aLex.lex(value).get(0);
        evalStack.push(newTok);
        expr = new ArrayList<String>();
      }
    } while (!evalStack.empty());
    return value;
  }

  public Stack buildStack()
  {
    Stack<Lexer.Token> st = new Stack();
    int size = result.size();
    Lexer.Token temp;

    for (int i = 0; i < size; i++)
    {
      temp = result.get(i);
      st.push(temp);
    }
    System.out.println(st);
    return st;
  }

  public void addtoFront(ArrayList<String> aList, String val)
  {
    int size = aList.size();
    String temp;
    if (size == 0)
    {
      aList.add(val);
    }
    else if (size != 0)
    {
      temp = aList.get(0);
      aList.add(0, val);
      for (int i = 1; i < (size - 1); i++)
      {
        aList.add(i, temp);
        temp = aList.get(i+1);
      }
      //System.out.println("Queue list: " + aList);
    }
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
