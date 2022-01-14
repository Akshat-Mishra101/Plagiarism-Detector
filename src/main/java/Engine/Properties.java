/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joey
 */
public class Properties {
    
    public static volatile boolean isReady = false;
    public static volatile List<String> names= null;
    
    public static String [][]plagpercentage = null;
    public static int[]total_words_per_file = null;
    public static volatile ReportType type;
    public static String proxyAPI;
    public static String numberOfResults;
    public static String searchEngine;
    
    
    public static String plagCheckType;
    
    
    public static String skippablePhrases;
    public static String skippableLines;
    
    public static String plagReport;
    public static String reportName;
    public static void loadFiles()
    {
        try {
            Scanner sc=new Scanner(new File(".data"));
            while(sc.hasNext())
            {
              String line = sc.nextLine();
              String key=line.substring(0,line.indexOf(":")+1); 
              String value=line.substring(line.indexOf(":")+1, line.length());
              if(key.equals("proxy:"))
              {
                proxyAPI = value;
              }
              if(key.equals("maxresults:"))
              {
                numberOfResults = value;
              }
              if(key.equals("engines:"))
              {
                  searchEngine = value;
              }
              if(key.equals("plagcheck:"))
              {
                  plagCheckType = value;
              }
              if(key.equals("sphrase:"))
              {
                 skippablePhrases = value;
              }
              if(key.equals("slines:"))
              {
                 skippableLines = value;
              }
            
            }
        } catch (Exception ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getValue(String key)
    { 
        
              if(key.equals("proxy"))
              {
                return proxyAPI;
              }
              else if(key.equals("maxresults"))
              {
               return numberOfResults;
              }
              else if(key.equals("engines"))
              {
                 return searchEngine;
              }
              else if(key.equals("plagcheck"))
              {
                  return plagCheckType;
              }
              else if(key.equals("sphrase"))
              {
                 return skippablePhrases;
              }
              else if(key.equals("slines"))
              {
                 return skippableLines;
              }
              else 
              {
              return "Invalid Key";
              }
            
       
    }
    
    
    public static void Update(String key,String value)
    {
       if(key.equals("proxy"))
              {
                proxyAPI=value;
              }
              else if(key.equals("maxresults"))
              {
               numberOfResults=value;
              }
              else if(key.equals("engines"))
              {
                searchEngine=value;
              }
              else if(key.equals("plagcheck"))
              {
                  plagCheckType=value;
              }
              else if(key.equals("sphrase"))
              {
                 skippablePhrases=value;
              }
              else if(key.equals("slines"))
              {
                 skippableLines=value;
              }
              
    }
    
    public static void Save() throws Exception
    {
       String resultant = "proxy:"+proxyAPI+"\r\n"+
                          "maxresults:"+numberOfResults+"\r\n"+
                          "engines:"+searchEngine+"\r\n"+
                          "plagcheck:"+plagCheckType+"\r\n"+
                          "sphrase:"+skippablePhrases+"\r\n"+
                          "slines:"+skippableLines+"\r\n";
       FileWriter fw=new FileWriter(new File(".data"));
       fw.write(resultant);
       fw.close();
    }
}
