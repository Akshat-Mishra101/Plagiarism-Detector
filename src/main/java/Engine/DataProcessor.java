/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author joey
 */
public class DataProcessor extends Task<Void> {
    
    
    List<File> filepaths;
    String type_of_operation;
    
    public DataProcessor(List<File> filepaths,String type_of_operation)
    {
        this.filepaths=filepaths;
        this.type_of_operation = type_of_operation;
    }
    
    
    public void Process()
    {
        List<String> strings=new ArrayList();
        List<String> docname = new ArrayList();
        filepaths.forEach(file -> {
           updateProgress(-1,100);
            if(file.getName().contains(".txt")){
            try {
                updateMessage("Loading Files");
                Scanner sc=new Scanner(file);
               
                while(sc.hasNext())
                {
                  StringTokenizer st=new StringTokenizer(sc.nextLine(),".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                  strings.add(st.nextToken());
                  docname.add(file.getName());
                  }
                }
                
                 
            } catch (FileNotFoundException ex) {
              System.out.println("File Not Found");
            }
            
        }
        else if(file.getName().contains(".pdf"))
        {
        //apache pdfbox to parse
        }
        else if(file.getName().contains(".docx"))
        {
        // docx for j to parse
        }
        else if(file.getName().contains(".html"))
        {
        // jsoup to parse
        }
        }
                
           );
        strings.size();
        try {
            Document doc=Jsoup.connect("https://www.google.co.in//").get();
        } catch (IOException ex) {
           updateMessage("Unable To Connect");
        }
        
      Collections.sort(strings,Collections.reverseOrder());
     
      strings.forEach(str->System.out.println(str));
      System.out.println(strings);
      
        
       
    }

    @Override
    protected Void call() throws Exception {
        
        Process();
        
        
        
        
        return null;
        
    }
}
