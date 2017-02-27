import java.io.IOException;
import java.util.Scanner;

public class EArgs
{
  private Scanner s = new Scanner(System.in);

  private String helpText = "Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.";
  private String synoText = "\n\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\nmethods in <class-name>, and executes them, printing the result to sysout.";


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
    else if ((argsList.length == 1) || (argsList.length == 2))
    {
      if (!(argsList[0].endsWith(".jar"))) // First arg ends with .jar
      {
        System.out.println("This program requires a jar file as the first command line argument (after any qualifiers)." + synoText);
        System.err.println("EXIT -3");
        System.exit(-3);
      }
      else if ((argsList[0].equals("-h")) || (argsList[1].equals("-?")) || (argsList[1].equals("--help"))) // If first arg is for help
      {
        System.out.println(helpText + synoText);
      }
      else if (argsList[0].endsWith(".jar"))
      {
        // Check if .jar exists
        // If yes, start with that jar. If no, error
      }
      else if (argsList.length == 3) // Checks for qualifiers
      {
        if ((argsList[0].equals("-v")) || (argsList[0].equals("--verbose"))) // For verbose mode
        {
          System.out.println("Verbose mode"); // Code for verbose mode here
        }
      }
    }
  }

  public void Menu()
  {
    do
    {
      String choice;
      choice = s.next();

      switch(choice)
      {
        case "q":
        case "Q":
        break;

        case "v":
        case "V":
        break;

        case "f":
        case "F":
        break;

        case "?":
        break;

        default:
        break;
      }
    } while (True);
  }
}
