/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.time.Instant;

/**
 * Creates The Report 
 * @author joey
 */
public class ReportCreator {
    
    public static String CreateReport()
    {
        /**
         * Report Style
         * 
         */
        if(Properties.reportName==null)
        {
        
           //search through the documents
            Properties.reportName = "Report_"+Instant.now();
        }
        String reportTitle = Properties.reportName;
        String Type = Properties.plagpercentage[0].length>1?"Multi-Document, ":"Single Document, ";
        String mode = (Properties.type==ReportType.WEB)?"WebSearch ":(Properties.type==ReportType.INTERDOC)?"Interdocument Search ":"Complete Search ";
        String listheaders="doc_name,plagiarism_percentage,plagmap_source,similar_docs,total_words_plagiarised";
        
        String report="Title:"+reportTitle+"\r\n"+"Report Info:"+Type+mode+"\r\n"+listheaders;
        String list="";
        int position=0;
        for(String filename:Properties.plagpercentage[0])
        {
         list+=filename+",";
         String sentences[]=Properties.plagpercentage[1][position].split(",");
         
         int wordcount = 0; //per sentence
         String textaddition="(";
         for(String sen:sentences)
         {
             
             String text = sen.substring(0,sen.lastIndexOf("|"));
             String result = sen.substring(sen.lastIndexOf("|")+1);
             
             //String source = ""; // this string would store the source of plagiarism for this 
            if(result.equals("false"))
            {
              wordcount += text.split(" ").length;
              textaddition+=text+",";
              
              
            }
          
            
             
         }
          textaddition+=")";
         double plagpercentage =(double)((double)wordcount/(double)Properties.total_words_per_file[position])*100.0d;
         
         list+=plagpercentage+","+textaddition+",similar-docs,"+Properties.total_words_per_file[position];
         
         list+="\r\n";
         position++;
        
        }
        
        report += "\r\n"+list;
         Properties.plagReport = report;
                 
        
        // we create a text report
        
     return list;  
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
