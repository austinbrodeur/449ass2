import java.util.ArrayList;
import java.util.Stack;
import java.lang.reflect.*;
import java.lang.Exception;

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
    Parse(removeQuotes(aLex.lex(input)));
  }

  // One of the rules is still broken when testing multi-embedded functions
  public void Parse(ArrayList<Lexer.Token> tokens)
  {
    int tokensSize = (tokens.size()-1);
    int parCount = 0;
    Lexer.Token cToken;
    Node cNode = root;
    for (int i = 0; i <= tokensSize; i++){
      cToken = tokens.get(i);
      try {
        if (cToken.type.name().equals("ERROR")) { //
          throw new ArithmeticException();
        }
      } catch (Exception e) {
        System.out.println(iString);
        EArgs.unexpectedError(cToken.offset, cToken.data);
        if (Mode.verbose == true)
          e.printStackTrace();
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
    try {
      if (parCount != 0) {
        throw new ArithmeticException();
      }
    }
    catch (Exception e)
    {
      System.out.println(iString);
      if (parCount < 0)
        EArgs.blankError(0);
      if (parCount > 0)
        EArgs.blankError(iString.length() - 1);
      System.err.println("Error: Mismatched parenthesis.");
      if (Mode.verbose == true)
        e.printStackTrace();
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

  public String evaluateTree(ArrayList<Lexer.Token> evalList) throws NoSuchMethodException, IllegalAccessException
  {
    Stack<Lexer.Token> evalStack = buildStack();
    ArrayList<String> expr = new ArrayList<String>();
    ArrayList<String> extraParams = new ArrayList<String>();
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
        try {
          addtoFront(expr, temp.data);
          if (handler.paramCount(temp.data) == (expr.size() - 1)) {
            value = handler.evaluate(expr).toString();
            newTok = aLex.lex(value).get(0);
            newTok.offset = temp.offset;
            evalStack.push(newTok);
            expr.clear();
          }
          else if (handler.paramCount(temp.data) != (expr.size() + 1)) {
            value = evalParams(expr, handler.paramCount(temp.data));
            newTok = aLex.lex(value).get(0);
            newTok.offset = temp.offset;
            evalStack.push(newTok);
          }
        }
        catch (Exception e)
        {
          System.out.println(iString);
          EArgs.blankError(temp.offset);
          System.out.println("Error evaluating \"" + temp.data + "\" at offset " + temp.offset);
          if (Mode.verbose == true)
            e.printStackTrace();
          break;
        }
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
    //System.out.println(st);
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

  public ArrayList<Lexer.Token> removeQuotes(ArrayList<Lexer.Token> list)
  {
    int size = list.size();
    Lexer.Token temp;

    for (int i = 0; i < size; i++)
    {
      temp = list.get(i);
      if (temp.type.name().equals("STRING"))
      {
        temp.data = temp.data.replaceAll("\"", "");
        list.remove(i);
        list.add(i, temp);
      }
    }
    return list;
  }

  public String evalParams(ArrayList<String> expr, int numParams) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException
  {
    ArrayList<String> temp = new ArrayList<String>();
    for (int i = 0; i <= numParams; i++)
    {
      temp.add(expr.get(i));
      expr.remove(i);
    }
    return handler.evaluate(temp).toString();
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
