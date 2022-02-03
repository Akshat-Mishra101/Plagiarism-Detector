/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates The Report 
 * @author joey
 */
public class ReportCreator {
    
    static String text_preAddition = "";
    

    
    public static String CreateReport() throws IOException
    {   text_preAddition = "";
        int place = 0;
        for(String filename:Properties.plagpercentage[0])
        {
        
          text_preAddition += "filename:"+filename+"\r\n"+Properties.documents[place]+"<-->\r\n";
        
          place++;
          
        }
        
      
        /**
         * Report Style
         * 
         */
        
        if(Properties.type == ReportType.WEB){
        if(Properties.reportName==null)
        {
           //search through the documents
            Properties.reportName = "Report_"+Instant.now();
        }
        String reportTitle = Properties.reportName;
        String Type = Properties.plagpercentage[0].length>1?"Multi-Document, ":"Single Document, ";
        String mode = (Properties.type==ReportType.WEB)?"WebSearch ":(Properties.type==ReportType.INTERDOC)?"Interdocument Search ":"Complete Search ";
        String listheaders="doc_name,plagiarism_percentage,plagmap_source,total_words";
        
        String report=text_preAddition+"Title:"+reportTitle+"\r\n"+"Report Info:"+Type+mode+"\r\n"+listheaders;
        String list="";
        int position=0;
        for(String filename:Properties.plagpercentage[0])
        {
         list+=filename+Properties.global_delimeter;
         String sentences[]=Properties.plagpercentage[1][position].split("::");
         Arrays.stream(sentences).forEach(item->System.out.print(item+"|"));
         
         int wordcount = 0; //per sentence
         String textaddition="(";
         for(String sen:sentences)
         {
             try{
           
             String text = sen.substring(0,sen.lastIndexOf("|"));
             String result = sen.substring(sen.lastIndexOf("|")+1);
             System.out.println("TEXT := "+text);
             System.out.println("RESULT := "+result);
             
             //String source = ""; // this string would store the source of plagiarism for this 
            if(result.equals("false"))
            {
              wordcount += text.split(" ").length;
              textaddition+=text+"::";
              
              
              
            }
             }
             catch(Exception e)
             {
             
             System.out.println(sen+" is the reason inside report "+e);
             }
          
             
            
             
         }
         System.out.println(wordcount+"IS THE WORDCOUNT");
          textaddition+=")";
         double plagpercentage =(double)((double)wordcount/(double)Properties.total_words_per_file[position])*100.0d;
         plagpercentage = plagpercentage > 100? 100:plagpercentage; 
         
         list+=plagpercentage+Properties.global_delimeter+textaddition+Properties.global_delimeter+Properties.total_words_per_file[position];
         
         list+="\r\n";
         position++;
        
        }
        
        report += "\r\n"+list;
         Properties.plagReport = report;
                 
        
        // we create a text report
        
     return list;  
        }
        else if(Properties.type == ReportType.INTERDOC)
        {
        if(Properties.reportName==null)
        {
           
           //search through the documents
           Properties.reportName = "Report_"+Instant.now();
        
        }
           String reportTitle = Properties.reportName;
           String Type = Properties.plagpercentage[0].length>1?"Multi-Document, ":"Single Document, ";
           String mode = (Properties.type==ReportType.WEB)?"WebSearch ":(Properties.type==ReportType.INTERDOC)?"Interdocument Search ":"Complete Search ";
           String listheaders="doc_name,plagiarism_percentage,plagmap_source,total_words";
        
           String report=text_preAddition+"Title:"+reportTitle+"\r\n"+"Report Info:"+Type+mode+"\r\n"+listheaders;
           
           List<String> bank  = new ArrayList<>();
           
           String list="";
           int position = 0;
           for(String filename:Properties.plagpercentage[0])
           {
           int word_count = 0;    
           list+=filename+Properties.global_delimeter;
           
           String collected = "(";
           for(String map:Properties.source_mapping)
           {
               
              if(map.contains(filename))
              {
                 collected += map+"::";
                 
                
                 String splitter[] = map.split("<=>");
                 for(String split:splitter)
                 {
                 split = split.substring(0, split.lastIndexOf("["));
                 System.out.println(split+" is split"+ split.split(" ").length);
                  if(!bank.contains(split))
                  word_count += split.split(" ").length;
                  bank.add(split);
                 
                 }
              }
              
              
              
                
           }
           bank.clear();
           collected += ")";
           double plagpercentage =(double)((double)word_count/(double)Properties.total_words_per_file[position])*100.0d;
           System.out.println(Properties.total_words_per_file[position]);
           plagpercentage = plagpercentage > 100? 100:plagpercentage; 
            list+=plagpercentage+Properties.global_delimeter+collected+Properties.global_delimeter+Properties.total_words_per_file[position]+"\r\n";
           position++;
           }
           
        
        
          report += "\r\n"+list;
         Properties.plagReport = report;
        System.out.println(report);
        
        return report;
        }
        else{
            //this means it is a complete search
       if(Properties.reportName==null)
        {
           
           //search through the documents
           Properties.reportName = "Report_"+Instant.now();
        
        }
           String reportTitle = Properties.reportName;
           String Type = Properties.plagpercentage[0].length>1?"Multi-Document, ":"Single Document, ";
           String mode = (Properties.type==ReportType.WEB)?"WebSearch ":(Properties.type==ReportType.INTERDOC)?"Interdocument Search ":"Complete Search ";
           String listheaders="doc_name,plagiarism_percentage,plagmap_source,total_words";
        
           String report=text_preAddition+"Title:"+reportTitle+"\r\n"+"Report Info:"+Type+mode+"\r\n"+listheaders;
           
           List<String> bank  = new ArrayList<>();
           
           String list="";
           int position = 0;
           for(String filename:Properties.plagpercentage[0])
           {
           int word_count = 0;    
           list+=filename+Properties.global_delimeter;
           
           String collected = "(";
           for(String map:Properties.source_mapping)
           {
               
              if(map.contains(filename))
              {
                 try{
                 String left = map.substring(0,map.lastIndexOf("->"));
                 String source = map.substring(map.lastIndexOf("->"));
                 collected += left+source+"::";
                 String splitter[] = map.substring(0, map.lastIndexOf("->")).split("<=>");
                 
                 for(String split:splitter)
                 {
                 split = split.substring(0, split.lastIndexOf("["));
                 System.out.println(split+" is split"+ split.split(" ").length);
                  if(!bank.contains(split))
                  word_count += split.split(" ").length;
                  bank.add(split);
                 
                 }
                 }
                 catch(Exception d){
                 System.out.println(d+" REPORTER REPORTEF");
                 }
              }
              
              
              
                
           }
         
           bank.clear();
           collected += ")";
           double plagpercentage =(double)((double)word_count/(double)Properties.total_words_per_file[position])*100.0d;
           System.out.println(Properties.total_words_per_file[position]);
           plagpercentage = plagpercentage > 100? 100:plagpercentage; 
            list+=plagpercentage+Properties.global_delimeter+collected+Properties.global_delimeter+Properties.total_words_per_file[position]+"\r\n";
           position++;
           }
           
        
        
          report += "\r\n"+list;
         Properties.plagReport = report;
        System.out.println(report);
        
        return report;
        }
    }
    
    
    public void makeAHumanReadable(String path)
    {
      //makes a human readable version 
    }
    public static void SaveReport(String format)
    {
        if(format.equals("html"))
        {
          
        }
        else if(format.equals("pdf"))
        {
        
        }
        else if(format.equals("docx"))
        {
        }
        else
        {}
      //if format is equals to pdf, we create a pdf report, if it is equal to doc, we create a document report otherwise we create a text report
    }
}
