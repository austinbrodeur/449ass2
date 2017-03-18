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
   System.out.println(classes);
   for(i=0; i<classes.size(); i++){
     if(classes.get(i).equals(desiredClass)){
      
      //For some reason it can't find the tOURI() method
       URLClassLoader child = new URLClassLoader (jarName.toURI().toURL());
       Class objClass = Class.forName (desiredClass, true, child);
     }
   }
   return objClass;
}
}
