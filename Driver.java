
//The main class/driver for this program.
public Driver {
	
	private String[] helpInput = {"-?", "-h", "--h", "--he", "--hel", "--help"};
	private String[] verboseInput = {"-v", "--v", "--ve", "--ver", "--verb", "--verbo", "--verbos", "--verbose"};
	
	private String helpText = "Synopsis:\n  methods\n  methods { -h | -? | --help }+\n  methods {-v --verbose}* <jar-file> [<class-name>]\nArguments:\n  <jar-file>:   The .jar file that contains the class to load (see next line).\n  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\nQualifiers:\n  -v --verbose: Print out detailed errors, warning, and tracking.\n  -h -? --help: Print out a detailed help message.\nSingle-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.";
	private String synoText = "\n\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\nmethods in <class-name>, and executes them, printing the result to sysout.";
	private String menuText = "\nq           : Quit the program.\nv           : Toggle verbose mode (stack traces).\nf           : List all known functions.\n?           : Print this helpful text.\n<expression>: Evaluate the expression.\nExpressions can be integers, floats, strings (surrounded in double quotes) or function\ncalls of the form '(identifier {expression}*)'.";
	
	public static boolean verbose = false;		//global flag for verbose mode.
	
	public static void main (String[] args) {
		
		boolean run = true;
		
		Scanner keyIn = new Scanner(System.in);
		String className = init(args);
		ReflectionsHandler handler = new ReflectionsHandler(className);
		
		System.out.println(menuText);
		while(run) {
			System.out.println("> ");
			String input = keyIn.next();
			switch(input.toLowerCase()) {
				case "q": run = false;
						  break;
				case "v": (verbose == true ? false : true);
						  break;
				case "f": handler.functionList();
						  break;
				case "?": System.out.println(helpText + synoText);
						  break;
				default:  solve(input, handler);
						  break;	
			}
		}
		
		System.out.println("Bye!");
		System.exit(0); 
	}
	
	//THIS DOES ALL THE PARSING, SOLVING, whatever. Just make the lexer, parser, and do the reflection stuff here.
	private void solve(String input, ReflectionsHandler handler) {
		//THE PARSING AND SHIT
	}
	
	//Parses through the commandline arguments as required. Returns the classname. //UNFINISHED
	private String init(String[] args) {
		if (args.length > 3) {
			System.err.println("This program takes at most two command line arguments.\n" + helpText); 	// Too many args
			System.exit(-2);
			
		} else if (args.length == 0) {
			System.err.println(helpText);
			System.exit(0);
			
		} else {
			
			if (args.length == 1) {
				//Checks if any of the args are the help qualifiers. Prints and exits if so.
				if (isIn(arg, helpInput)) {
					System.out.println(helpText + synoText);
					System.exit(0);
				} 
				
				//Else checks if the only argument ends with jar. Errors if not.
				else if (!args[0].endsWith(".jar")) {
					System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).\n" + helpText);
					System.exit(-3);
				} 
				
				//GET THE CLASS NAME FROM JAR HERE
				else {
					
					//So write that code or function here.
					//MUST RETURN THE CLASS NAME AS A STRING.
				}
				
			} else if (args.length == 3) {
				//Checks if any of the args are the help qualifiers. Errors if so.
				for (String arg : args) {
					if (isIn(arg, helpInput)) {
						System.err.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.\n" + helpText);
						System.exit(-4);
					}
				}
				
				//Check the first argument for verbose. Toggles if it's right.
				if (!isIn(args[0], verboseInput)) {
					System.err.println("Unrecognized qualifer: " + args[0] + "\n" + helpText);
					System.exit(-4);
				} verbose = (verbose == true ? false : true);		//Toggle verbose.
				
				//Else checks if the second argument ends with jar. Errors if not.
				if (!args[1].endsWith(".jar")) {
					System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).\n" + helpText);
					System.exit(-3);
				}
				
				//If the first two arguments are correct, return the third argument.
				return args[2];
				
			} else {
				//Checks if any of the args are the help qualifiers. Errors if so.
				for (String arg : args) {
					if (isIn(arg, helpInput)) {
						System.err.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.\n" + helpText);
						System.exit(-4);
					}
				}
				
				//Checks if the first argument is the jar.
				if (args[0].endsWith(".jar")) {
					return args[1];					//If the first argument is a jar, the second must be the class name.
				} else {
					//Since first argument then must be verbose, check for verbose.
					if (!isIn(args[0], verboseInput)) {
						System.err.println("Unrecognized qualifer: " + args[0] + "\n" + helpText);
						System.exit(-4);
					} verbose = (verbose == true ? false : true);		//Toggle verbose.
					
					//Else if none of the arguments are jars, error. If it is a jar, get the class name from it.
					if (!args[1].endsWith(".jar")) {
						System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).\n" + helpText);
						System.exit(-3);
					} 
					
					//GET THE CLASS NAME FROM JAR HERE
					else {
						
						//So write that code or function here.
						//MUST RETURN THE CLASS NAME AS STRING
					}
				}
			}
		}
	}
	
	//Local use only, checks if a string is in the given list.
	private boolean isIn(String str, String[] list) {
		for (String item : list) {
			if (str.equals("-V") || str.equals("-H")) {
				return false;
			} else if (item.equals(str.toLowerCase())) {
				return true;
			}
		} return false;
	}
	
	//This function is called when a function name does not match any functions in the given class.
	//Displays the error message for non-existent functions, as well as the offset.
	private void matchingError(int offset, String expr) {
		System.out.println("Matching function for '" + expr + "not found at offset" + offset + "\n" + expr);
		for(int i=0; i < (offset-1); i++){
			System.out.println("-");
		} System.out.print("^\n");
	}
	
	//This function is called when a function name does not match any functions in the given class.
	//Displays an error message for invalid characters, as well as the offset.
	private void unexpectedError(int offset, String expr) {
		System.out.println("Unexpected character encountered at offset: " + offset + "\n" + expr);
		for(int i=0; i < (offset-1); i++){
			System.out.print("-");
		} System.out.print("^\n");
	}
	
	//Takes in an entire file name.
	//Returns true if jar exists, false if not.
	private boolean checkJar(String fileName) {
		if (fileName.endsWith(".jar")) {		
			File file = new File(fileName);
			if ((file.exists()) && (!file.isDirectory())) {
				return true;
			}
		} return false;
	}
}

