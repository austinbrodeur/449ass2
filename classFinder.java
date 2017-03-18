import java.util.jar.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class classFinder{
 public static int i=0;
 public static Class<?> objClass;
 private classFinder() {}


 public static Object getClassNamesInPackage(String jarName, String desiredClass){
   ArrayList classes = new ArrayList ();
   try{
     JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
     JarEntry jarEntry;

     while(true) {
       jarEntry=jarFile.getNextJarEntry ();
       if(jarEntry == null){
         break;
       }
       if((jarEntry.getName ().endsWith (".class"))) {
         classes.add (jarEntry.getName().replaceAll("/", "\\."));
       }
     }
   }
   catch(Exception e){
     e.printStackTrace ();
   }
   for(i=0; i<classes.size(); i++){
     if(classes.get(i).equals(desiredClass)){
       File f = new File(jarName);
       URL[] jarURL = new URL[1];
       try{
         jarURL[0] = f.toURI().toURL();
       }catch(Exception e){
         System.out.println("Malformed URL");
       }
       URLClassLoader child = new URLClassLoader (jarURL);
       try{
         desiredClass.replace(".class", "");
         Class objClass = Class.forName (desiredClass.substring(0, desiredClass.length()-6), true, child);
       }catch(Exception e){
         System.out.println("Class not found");
       }
     }
   }
   return objClass;
}
}
