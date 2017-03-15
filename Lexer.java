// Used the lexer from http://giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html as a reference

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer
{

  public static class Token
  {
    public TokenType type;
    public String data;

    public Token(TokenType type, String data)
    {
      this.type = type;
      this.data = data;
    }

    public String toString()
    {
      return String.format("(%s %s)", type.name(), data);
    }
  }

  public static enum TokenType
  {
    WHITESPACE("[ \t]+"), LPAR("[(]"), RPAR("[)]"), PLUS("[+]"),
    MINUS("[-]"), DOT("[.]"), INTEGER("\\d+"), STRING("\"([^\"]*)\""), ID("[^\\s]+"), ERROR(".+");

    public final String pattern;

    private TokenType(String pattern)
    {
      this.pattern = pattern;
    }
  }

  public static ArrayList<Token> lex(String input)
  {
    ArrayList<Token> tokens = new ArrayList<Token>();

    StringBuffer tokenPatternsBuffer = new StringBuffer();
    for (TokenType tokenType : TokenType.values())
      tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find())
    {
      for (TokenType tk : TokenType.values())
      {
        if (matcher.group(TokenType.WHITESPACE.name()) != null)
          continue;
        else if (matcher.group(tk.name()) != null)
        {
          tokens.add(new Token(tk, matcher.group(tk.name())));
          continue;
        }
      }
    }
    return tokens;
  }
}
