
import java.util.jar.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.lang.reflect.*;

//This class represents a handler for the runtime method calls using the Reflection API.
public class ReflectionsHandler {

	//Variable declarations.
	private Class<?> objClass;
	private String objName;
	private Class method;
	private Method[] methods;
	//private classFinder finder = new classFinder();

	//CONSTRUCTOR
	public ReflectionsHandler(String jarName, String className) {
		URLClassLoader classLoader = null;
		try {

			URL[] urls = { new URL("jar:file:" + jarName + "!/") };
			classLoader = URLClassLoader.newInstance(urls);

			new JarFile(jarName);


		} catch (Exception e) {
			System.err.println("Could not load jar file: <" + jarName + ">");
			System.exit(-5);
		}

		try {
			method = classLoader.loadClass(className);
			methods = method.getDeclaredMethods();
		} catch(ClassNotFoundException e) {
			System.err.println("Could not find class: <" + className + ">");
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
	/*
	public static void main(String[] args) {
		try {
            //SiberianWarLlama is a test class. You can use any class to test.
			ReflectionsHandler handler = new ReflectionsHandler("SiberianWarLlama");
			handler.functionList();

			ArrayList<String> testList = new ArrayList<String>(4);
			testList.add("add");
			testList.add("1");
			testList.add("2");
			System.out.println("\n" + handler.evaluate(testList).toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
