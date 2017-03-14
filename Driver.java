public class Driver
{
  public static void main(String args[])
  {
    //EArgs aArgs = new EArgs(args); test for the user interface in the EArgs class
    Lexer aLex = new Lexer(args[0]); // Instance of lexer
    Parser aParse = new Parser(aLex.lex(args[0])); // Pass tokens into parser
  }
}
