import java.lang.reflect.*;

//CODING IN PROGRESS
public class ReflectionsHandler {
	
	private Class<?> objClass;
	private Method[] methods;
	
	public ReflectionsHandler(String objName) {
		try {
			objClass = Class.forName(objName);
			methods = objClass.getMethods();
		} catch (Exception e) {
			System.err.println("Could not find class: " + objName);
			System.exit(0);
		}
	}
	
	//Prints out the list of functions and their parameters and returns.
	public void functionList() {
		for (Method method : methods) {
			//(add string string) : int
			System.out.print("(" + method.getName());
			for (Class param : method.getParameterTypes()) {
				System.out.print(" " + param.getSimpleName());
			} System.out.print(") : " + method.getReturnType().getSimpleName() + "\n");
		}
	}
	
	//NOT DONE
	//takes in a stack or something similar. the name of the method call is a string.
	public void evaluate() {
		//this method evaluates a given set of whatevers.
	}
}

