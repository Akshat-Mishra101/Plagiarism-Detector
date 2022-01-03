/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author joey
 */
public class Properties {
    public static String proxyAPI;
    public static String numberOfResults;
    public static String searchEngine;
    
    
    public static String plagCheckType;
    
    
    public static String skippablePhrases;
    public static String deletablePhrases;
    
    public static void loadFiles()
    {
       
    }
    
    public static String getValue(String key)
    { 
        return "";
    }
    
    
    public static void Update(String key,String value)
    {
       if(key.equals(""))
       {
       
       }
    }
    
    public static void Save() throws Exception
    {
       String resultant = proxyAPI+"\r\n"+
                          numberOfResults+"\r\n"+
                          searchEngine+"\r\n"+
                          plagCheckType+"\r\n"+
                          skippablePhrases+"\r\n"+
                          deletablePhrases+"\r\n";
       FileWriter fw=new FileWriter(new File(".data"));
       fw.write(resultant);
       fw.close();
    }
}
