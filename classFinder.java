import java.util.jar.*;
import java.util.*;
import java.io.*;

public class classFinder{

 private classFinder() {}

 public static List getClasseNamesInPackage(String jarName){
   ArrayList classes = new ArrayList ();
   try{
     JarInputStream jarFile = new JarInputStream
        (new FileInputStream (jarName));
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
   return classes;
}
