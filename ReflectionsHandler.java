
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
			objClass = classLoader.loadClass(className);
			methods = objClass.getDeclaredMethods();
		} catch(ClassNotFoundException e) {
			System.err.println("Could not find class: <" + className + ">");
			System.exit(-6);
		}
	}

	//Returns the number of parameters for a given method.
	public int paramCount(String name) throws NoSuchMethodException {
		return locate(name).getParameterTypes().length;
	}

	//Returns the method from the given name from the list of methods.
	public Method locate(String name) throws NoSuchMethodException {
		for (Method method : methods) {
			if (method.getName().equals(name)) return method;
		} throw new NoSuchMethodException();
	}

	private Class<?>[] cleanParams(String name, Class<?>[] params) {
		for (Method method : methods) {
			Class[] temp = method.getParameterTypes();
			if (temp.length == params.length) {
				for (int i=0; i<temp.length; i++) {
					if (temp[i].equals(params[i])){
						continue;
					} else if ((temp[i].equals(Integer.class)) && (params[i].equals(int.class))) {
				//		System.out.println("lf: " + temp[i] + " --- got: " + params[i] + " WORKED");
						params[i] = Integer.class;
						continue;
					} else if ((temp[i].equals(Float.class) && (params[i].equals(float.class)))) {
			//			System.out.println("lf: " + temp[i] + " --- got: " + params[i] + " WORKED");
						params[i] = Float.class;
						continue;
					} else {
		//				System.out.println("lf: " + temp[i] + " --- got: " + params[i]);
						break;
					}
				}
			}
		} 
	/*	
		for(Class p : params) {
			System.out.println("EACH: -- " + p.getSimpleName());
		}
	*/
		return params;
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
	public Object evaluate(ArrayList<String> expression) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String methodName = expression.get(0);
		expression.remove(0);
		Method method = (Method) findActualMethod(methodName, expression);

		int num = expression.size();
		Class[] paramTypes = method.getParameterTypes();
		Object[] params = new Object[paramTypes.length];

		if (params.length == num) {
			for (int i=0; i<num; i++) {
				if (paramTypes[i].equals(String.class)) {
					params[i] = expression.get(i);
				} else if (paramTypes[i].equals(int.class) || paramTypes[i].equals(Integer.class)) {
					params[i] = Integer.parseInt(expression.get(i));
				} else if (paramTypes[i].equals(float.class) || paramTypes[i].equals(Float.class)) {
					params[i] = Float.parseFloat(expression.get(i));
				} else {
					throw new NoSuchMethodException();			//If the type is different from String, int, or float, error.
				}
			} return method.invoke(null, params);			//If all the parameters match the parses, then invokes method and returns.
		} throw new NoSuchMethodException();			//If the wrong number of arguments is passed, method dne.
	}

	//Returns the method from the given name from the list of methods.
	private Object findActualMethod(String methodName, ArrayList<String> parameters) throws NoSuchMethodException {
		Class[] paramTypes = new Class[parameters.size()];

		for (int i=0; i<paramTypes.length; i++) {
			try {
				Integer.parseInt(parameters.get(i));
				paramTypes[i] = int.class;
			} catch (NumberFormatException e) {
				try {
					Float.parseFloat(parameters.get(i));
					paramTypes[i] = float.class;
				} catch (NumberFormatException ee) {
					paramTypes[i] = String.class;
				}
			}
		}
		
		Method method = objClass.getMethod(methodName, cleanParams(methodName, paramTypes));
		
		} return method;
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
