/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

//import com.programmologists.projectm.ProjectProperties;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author joey
 */
public class PLBOT {
    public static String dom = "";
   
    
    
    
    public static boolean search(String sentence,int timeout) throws IOException//true when not plagiarised
    {
   
     
    // encoding the string 
    sentence=sentence.trim();
    String newer=sentence.replace(" ", "+");
    newer=newer.replace("’","'");
    newer=newer.replace(",","%2C");
    newer=newer.replace(":","%3A");
    newer=newer.replace("\"", "");
    newer=newer.replace("”", "");

    newer=newer.replace("‘", "'");
    newer=newer.replace("/", "%2F");
 
    newer=newer.replace("—", "");
    newer=newer.replace("“", "");
    boolean flag = false;
  
   
   
    String query=Properties.proxyAPI.trim()+"https://www.google.com/search?q="+"\""+newer+"\"";//+"&hl=en";
    //"http://api.scraperapi.com?api_key="+ProjectProperties.getProperty("apikey")+"&url="+//
     System.out.println(query); 
          
     String gotaresult="no";
     int retry=0;
     while(!gotaresult.equals("yes"))
     {
     try{
     if(retry>0){System.out.println("Retry Count: "+retry);
     
     if(retry==15){
         flag = true;
     break;
     }
     
     }
     else{
     //   System.out.println("query sent");
     }
     Document doc=Jsoup.connect(query).timeout(timeout).get();
      
     
        Elements d=doc.getElementsByTag("b").parents();
            List l=d.eachText();
             Iterator itr=l.iterator();
         
           while(itr.hasNext())
           {
           String kds=itr.next().toString();
           //System.out.println(kds);
           if(kds.trim().contains("No results found for")||kds.trim().contains("did not match any documents"))
           {
               System.out.println("yes we found it");
               
           flag=true;
           break;
           }
           }
           
           if(flag==true)
           {
               
           break;
           }
           else
           {
               System.out.println("We are here");
           PLBOT.dom = doc.html();
           }
     
     
    
     
     
     
     
     
     
   
      String sect=doc.getElementById("result-stats").text();
      System.out.println("\r\nWE ARE HERE_______Result Stats: "+sect);
      if(sect!=null)
      {
          flag=sect.contains("About 0 results")?true:false;
          if(!flag)
          PLBOT.dom = doc.html();
          else
          PLBOT.dom = "";
          
      }
      else
      {
      
      //System.out.println(doc.text());
      
      }
     gotaresult="yes";    
     }
     catch(Exception e){
      gotaresult="no";
    // System.out.println(e);
     retry++;
     
         
     }
    
     }
    return flag;    
    } 
    
   
    public static List<String> getSources()      
    {
        //System.out.println(PLBOT.dom+" |||");
        Elements el= Jsoup.parse(PLBOT.dom).getElementsByClass("yuRUbf");
        List<String> array = new ArrayList<>();
         for(Element e: el){
          
         Document c = Jsoup.parse(e.html());
         c.getElementsByTag("a");
         array.add(e.getElementsByTag("a").attr("href"));
      }
    return array;
    }
    
    
}
