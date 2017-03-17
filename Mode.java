public class Mode
{
  public static boolean verbose = false;

  public static void toggle()
  {
    if (verbose == false) {
      verbose = true;
      System.out.println("Verbose mode toggled to ON");
    }
    else if (verbose == true) {
      verbose = false;
      System.out.println("Verbose mode toggled to OFF");
    }
  }
}
