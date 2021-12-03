/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joey
 */
public class DataProcessor {
    List<File> filepaths;
    public DataProcessor(List<File> filepaths)
    {
        this.filepaths=filepaths;
        
    }
    
    
    public void Process()
    {
        List<String> strings=new ArrayList();
        
     
        filepaths.forEach(file -> {
          
            if(file.getName().contains(".txt")){
            try {
                Scanner sc=new Scanner(file);
               
                while(sc.hasNext())
                {
                  StringTokenizer st=new StringTokenizer(sc.nextLine(),".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                  strings.add(st.nextToken());
        
                  }
                }
                
                 
            } catch (FileNotFoundException ex) {
              System.out.println("File Not Found");
            }
            
        }
        else if(file.getName().contains(".pdf"))
        {
        
        }
        else if(file.getName().contains(".docx"))
        {
        
        }
        else if(file.getName().contains(".docx"))
        {
        
        }
        }
                
           );
        
     
        
      Collections.sort(strings,Collections.reverseOrder());
     
      strings.forEach(str->System.out.println(str));
      System.out.println(strings);
      
        
       
    }
}
