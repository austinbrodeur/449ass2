import java.util.ArrayList;
import java.util.Queue;

public class Parser
{
  private Node root = new Node(null, null);

  public Parser(ArrayList<Lexer.Token> tokens)
  {
    Parse(tokens);
  }

  public void Parse(ArrayList<Lexer.Token> tokens) // unfinished. Still needs to handle all types
  {
    int tokensSize = (tokens.size()-1);
    Lexer.Token cToken;
    Node cNode = root;
    System.out.println(tokens);

    for (int i = 0; i <= tokensSize; i++){
      cToken = tokens.get(i);

      if (cToken.type.name().equals("ERROR")) {
        System.err.println("Lex error. Unrecognized symbol \"" + cToken.data + "\"");
        System.exit(0);
      }
      if (cToken.type.name().equals("LPAR")) {
        cNode.createChild(null);
        cNode = cNode.getChild();
        continue;
      }
      if (cToken.type.name().equals("ID")) {
        cNode.setToken(cToken);
        cNode.createChild(null);
        cNode = cNode.getChild();
        continue;
      }
      if (cToken.type.name().equals("INTEGER") || cToken.type.name().equals("STRING")) {
        if (cNode.getToken() != null) {
          cNode.createChild(null);
          cNode = cNode.getChild();
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
  }

  // Production rules


}
