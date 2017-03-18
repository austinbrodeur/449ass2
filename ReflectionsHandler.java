import java.lang.reflect.*;
import java.util.ArrayList;

//This class represents a handler for the runtime method calls using the Reflection API.
public class ReflectionsHandler {
	
	//Variable declarations.
	private Class<?> objClass;
	private Method[] methods;
	
	//CONSTRUCTOR
	public ReflectionsHandler(String objName) {
		try {
			objClass = Class.forName(objName);
			methods = objClass.getMethods();
		} catch (Exception e) {
			System.err.println("Could not find class: " + objName);
			System.exit(-6);
		}
	}
	
	//Prints out the list of functions and their parameters and returns.
	public void functionList() {
		for (Method method : methods) {
			System.out.print("(" + method.getName());
			for (Class param : method.getParameterTypes()) {
				System.out.print(" " + param.getSimpleName());
			} System.out.print(") : " + method.getReturnType().getSimpleName() + "\n");
		}
	}
	
	//Evaluates the method and parameters given (given as an ArrayList<String>).
	public Object evaluate(ArrayList<String> expression) throws NoSuchMethodException {
		Method method = findMethod(expression.get(0));		//Gets the method.
		expression.remove(0);								//Removes the method name so it's just parameters left.
		Class[] paramTypes = method.getParameterTypes();	//Get the parameter types of the method called.
		Object[] params = new Object[paramTypes.length];	//Create a new Object array to store all the parsed and converted parameters.
		
		//Try block to create the parameters for the method invocation.
		try {
			//Checks if the parameters matches the parameter types and parses them appropriately.
			if (expression.size() == paramTypes.length) {
				for(int i=0; i<paramTypes.length; i++) {
					if (paramTypes[i].equals(String.class)) {
						params[i] = expression.get(i);
					} else if (paramTypes[i].equals(int.class) || paramTypes[i].equals(Integer.class)) {
						params[i] = Integer.parseInt(expression.get(i));
					} else if (paramTypes[i].equals(float.class) || paramTypes[i].equals(Float.class)) {
						params[i] = Float.parseFloat(expression.get(i));
					} else {
						throw new NoSuchMethodException();
					}
				} return method.invoke(null, params);	//If all the parameters match the parses, then invokes method and returns.
			} else {
				throw new NoSuchMethodException();		//If the wrong number of arguments is passed, throws NoSuchMethodException.
			}
		} catch (Exception e) {
			throw new NoSuchMethodException();			//If the argument given cannot be parsed properly, it is wrong.
		}
	}
	
	//Returns the method from the given name from the list of methods.
	//This method operates under the assumption there are no duplicate named methods.
	public Method findMethod(String name) throws NoSuchMethodException {
		for (Method method : methods) {
			if (method.getName().equals(name)) return method;
		} throw new NoSuchMethodException();
	}
	
	//Returns the number of parameters for a given method.
	public int paramCount(String name) throws NoSuchMethodException {
		return findMethod(name).getParameterTypes().length;
	}
	
	//Class test.
	public static void main(String[] args) {
		try {
            //SiberianWarLlama is a test class. You can use any class to test.
			ReflectionsHandler handler = new ReflectionsHandler("SiberianWarLlama");
			handler.functionList();
			
			ArrayList<String> testList = new ArrayList<String>(4);
			testList.add("llamaChillTheFuckOutDude");
			testList.add("8");
			testList.add("2.46");
			testList.add("danklistfamdab");
			System.out.println("\n" + handler.evaluate(testList).toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
