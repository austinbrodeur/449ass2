import java.io.*;
import java.util.Scanner;

public class EArgs
{
  private Lexer aLex;
  private Parser aParse;

  private Scanner s = new Scanner(System.in);

  private String helpText = "Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.";
  private String synoText = "\n\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\nmethods in <class-name>, and executes them, printing the result to sysout.";
  private String menuText = "\nq           : Quit the program.\nv           : Toggle verbose mode (stack traces).\nf           : List all known functions.\n?           : Print this helpful text.\n<expression>: Evaluate the expression.\nExpressions can be integers, floats, strings (surrounded in double quotes) or function\ncalls of the form '(identifier {expression}*)'.";

  // Takes in list of command line arguments and performs error checking.
  public EArgs(String argsList[])
  {
    if (argsList.length > 3)
    {
      System.out.println("This program takes at most two command line arguments."); // Too many args
      System.exit(-2);
    }
    else if (argsList.length == 0)
    {
      System.out.println(helpText); // Prints program use and takes in no arguments
    }
    else if (argsList.length == 1)
    {
      if ((argsList[0].equals("-h")) || (argsList[0].equals("-?")) || (argsList[0].equals("--help") || argsList[0].equals("--HELP"))) // If first arg is for help
      {
        System.out.println(helpText + synoText);
      }
    }
    else if (argsList.length == 2)
    {
      if (argsList[0].equals("-h") || (argsList[0].equals("-?")) || (argsList[0].equals("--help") || argsList[0].equals("--HELP"))){
        System.err.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.\n" + helpText);
      }
      else if (!(argsList[0].endsWith(".jar"))) // First arg ends with .jar
      {
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers)." + helpText);
        System.exit(-3);
      }
      else if ((argsList[0].endsWith(".jar"))){
        if (checkJar(argsList[0])){
          Menu();
        }
        else{
          System.err.println("Could not load jar file: " + argsList[0]);
          System.exit(-5);
        }
      }
    }
    else if (argsList.length == 3) // Checks for qualifiers
    {
      if ((argsList[0].equals("-v")) || (argsList[0].equals("--verbose") || argsList[0].equals("--VERBOSE"))) // For verbose mode
      {
        System.out.println("Verbose mode"); // Code for verbose mode here
      }
    }
  }

  public Boolean checkJar(String fName)
  {
    File f = new File(fName);
    if (f.exists() && !f.isDirectory()){
      return true;
    }
    return false;
  }

  public void Menu()
  {
    System.out.println(menuText);
    do
    {
      System.out.print(">");
      String choice;
      choice = s.next();

      switch(choice)
      {
        case "q":
        case "Q":
        System.out.println("bye.");
        System.exit(0);
        break;

        case "v":
        case "V":
        System.out.println("Toggle verbose");
        break;

        case "f":
        case "F":
        System.out.println("Print list of all functions");
        break;

        case "?":
        System.out.println(helpText + synoText);
        break;

        default:
        aLex = new Lexer();
        aParse = new Parser(Lexer.lex(choice));
        break;
      }
    } while (true);
  }
  
  public void matchingError(int offset, String expr){
	System.out.println("Matching function for '" + expr + "not found at offset" + offset);
	System.out.println(expr);
	while(i=0; i<offset-1; i++;){
	      System.out.println("-");
	      }
	System.out.println("^");
}	
		
public void unexpectedError(int offset, String expr){
	System.out.println("Unexpected character encountered at offset" + offset);
	System.out.println(expr);
	while(i=0; i<offset-1; i++;){
		System.out.println("-");
	   	}
	System.out.println("^");
}
}
