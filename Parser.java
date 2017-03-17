import java.util.ArrayList;
import java.util.Queue;

public class Parser
{
  private Node root = new Node(null, null);

  public Parser(ArrayList<Lexer.Token> tokens)
  {
    Parse(tokens);
  }

  // One of the rules is still broken when testing multi-embedded functions
  public void Parse(ArrayList<Lexer.Token> tokens)
  {
    int tokensSize = (tokens.size()-1);
    Lexer.Token cToken;
    Node cNode = root;
    System.out.println(tokens);

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
        cNode = cNode.getParent();
        continue;
      }
      if (cToken.type.name().equals("RPAR")) {
        cNode = cNode.getParent();
        continue;
      }
    }
    printParse();
  }


  public void traverseTree()
  {
    Node cNode = root;
    ArrayList<String> result = new ArrayList<String>();

    do {
      if (cNode.getToken() == null)
      {

      }
    } while ();
  }

  public void evaluateTree()
  {
    
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
