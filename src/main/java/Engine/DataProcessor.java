/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

//import net.sourceforge.tess4j.ITesseract;
//import net.sourceforge.tess4j.Tesseract;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.rendering.ImageType;
//import org.apache.pdfbox.rendering.PDFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *
 * @author joey
 */
public class DataProcessor extends Task<Void> {
    
    
    
    
    List<File> filepaths;
    String type_of_operation;
    AnchorPane websearch;
    AnchorPane documentalsearch;
    AnchorPane reportcreation;
    
  
    MFXProgressSpinner msp;
    MFXProgressSpinner bsp;
    MFXProgressSpinner csp;
    
    
    
    public DataProcessor(List<File> filepaths,String type_of_operation,AnchorPane websearch,AnchorPane documentalsearch,AnchorPane reportcreation,MFXProgressSpinner msp,MFXProgressSpinner bsp,MFXProgressSpinner csp)
    {
        
        
        this.filepaths=filepaths;
        this.type_of_operation = type_of_operation;
        
        this.websearch=websearch;
        this.documentalsearch=documentalsearch;
        this.reportcreation=reportcreation;
        this.msp=msp;
        this.csp=csp;
        this.bsp =bsp;
        
        if(type_of_operation.equals("websearch"))
            Properties.type = ReportType.WEB;
        else if(type_of_operation.equals("documental"))
            Properties.type=ReportType.INTERDOC;
        else
            Properties.type=ReportType.COMPLETE;
        
        
        Properties.plagpercentage = new String[2][filepaths.size()];
        Properties.documents = new String[filepaths.size()];
        Properties.total_words_per_file = new int[filepaths.size()];
       
        System.out.println(filepaths.size());
        System.out.println(Properties.plagpercentage.length);
        
        int count = 0;
        
        for(File file:filepaths)
        {
            System.out.println("Counter "+count);
        Properties.plagpercentage[0][count] = file.getName();
        Properties.plagpercentage[1][count] = "";
        Properties.documents[count] = "";
        Properties.total_words_per_file[count] = 0;
        count++;
       
        }
        
        
        
        
    }
    
    public void reset(){
    
    }
    public void Process() throws IOException
    {
        
        List<String> strings=new ArrayList();//stores the text along with the filenames
       
        
        filepaths.forEach(file -> {
            
             try{
            
            
            int totalwords = 0;
           updateMessage("Reading File");
           updateProgress(-1,100);
            if(file.getName().contains(".txt")){
          
                updateMessage("Loading Files");
                Scanner sc=new Scanner(file);
                
                while(sc.hasNext())
                {
                    updateMessage("Extracting Text");
                    String line = sc.nextLine();
                  Properties.documents[Properties.counter] += line+"\r\n";
                  StringTokenizer st=new StringTokenizer(line,".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                  String token=st.nextToken();
                  if(!isASkippableLine(token)&&token.trim().split(" ").length>6){
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                  }
                  
                  }
                }
                
                 
           
            
        }
        else if(file.getName().contains(".pdf"))
        {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            
            
            /**
                   PDDocument document = PDDocument.load(file);
                   PDFRenderer pdfRenderer = new PDFRenderer(document);
                   
                   
                   int totalPages = document.getNumberOfPages();
                   System.out.println(totalPages+" are these many ");
                  
                   
                   for(int i = 0; i<totalPages; i++){
                   
                   BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
                   
                   // Create a temp image file
                   
                   File tempFile = new File("tempfile_"+i+".png");
                   ImageIO.write(bufferedImage, "png", tempFile);
                   
                   ITesseract _tesseract = new Tesseract();
                   _tesseract.setDatapath("tessdata");
                   _tesseract.setLanguage("eng"); // choose your language
                   
                   String result = _tesseract.doOCR(tempFile);
                   tempFile.delete();
                   * */
                   Scanner sd=new Scanner(text);
                while(sd.hasNext())
                {
                    
                     String line = sd.nextLine();
                    Properties.documents[Properties.counter] += line+"\r\n";
                    
                    updateMessage("Extracting Text");
                    StringTokenizer st=new StringTokenizer(line,".!?");
                    // tokenize the Strings
                    while(st.hasMoreTokens()){
                        String token=st.nextToken();
                        if(!isASkippableLine(token)&&token.trim().split(" ").length>6){
                        strings.add(token+"|"+file.getName());
                        totalwords+=token.split(" ").length;
                    }
                  }
                
                }
                
                
                
                    
                   
                  
               
        }
        else if(file.getName().contains(".docx"))
        { // apache poi
       
                   XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
                   
                  
                  List<XWPFParagraph> list = doc.getParagraphs();
            for (XWPFParagraph paragraph : list) {
                
                //from these paragraphs we extract the text
               // System.out.println(paragraph.getText()+"\n___________________________________\n");
               
                  Properties.documents[Properties.counter] += paragraph.getText()+"\r\n";
               
               
                
                StringTokenizer sd = new StringTokenizer(paragraph.getText(),".!?");
                while(sd.hasMoreElements())
                {
                 String token=sd.nextToken();
                 if(!isASkippableLine(token)&&token.trim().split(" ").length>6){
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                 }
                }
                
            }
               
                 
              
        }
        else if(file.getName().contains(".html"))
        {
            
                   // jsoup to parse
                   String entire_text = Jsoup.parse(file, "UTF-8").text();
                   Scanner sd=new Scanner(entire_text);
                while(sd.hasNext())
                {
                  String line = sd.nextLine();
                  Properties.documents[Properties.counter] += line+"\r\n";
                    
                updateMessage("Extracting Text");
                StringTokenizer st=new StringTokenizer(line,".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                   String token=st.nextToken();
                   if(!isASkippableLine(token)&&token.trim().split(" ").length>6){
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                   }
                 
                  }
                
                }
                   
                   
              
            
        }
            Properties.total_words_per_file[Properties.array_pos]=totalwords;
         Properties.array_pos++;    
         Properties.counter++;
        } catch(Exception e){}
        
        
        });
        
        
        
        Properties.array_pos =0;
        Properties.counter = 0;
        
        //
         updateMessage("Bifurcating Data");
        String resultant[][]=Properties.plagpercentage;
       
        int counter= 0;
        System.out.println("HERE");
        for(String filename:resultant[0])
        {
          System.out.print(filename+":");
          
         
          
        for(String str:strings)
        {
            
        
            
            if(str.substring(str.lastIndexOf("|")+1).equals(filename))
            {
               
            resultant[1][counter] += str.substring(0,str.lastIndexOf("|"))+"::";
            }
            
            
            
           
        }
        counter++;
    
        }
        
        updateMessage("Bifurcation Complete");
        
        
        //Now Perform The Plag Check As Defined
        
        
        
        System.out.println("HERE ALSO");
       
        
       
        updateProgress(25,100);
        
        
        
       
           updateMessage("Bifurcation Complete");
           
           //printing the results
           
            
           
           
           
           //result print over
           
        
        
       
       // strings.size();
        //searching the web
        
        //se
        updateMessage("Text Extraction Complete");

        if(type_of_operation.equals("websearch"))
        {  
         
            FadeTransition fd2=new FadeTransition(Duration.millis(500));
            fd2.setNode(documentalsearch);
        
            fd2.setFromValue(0);
        
            fd2.setToValue(1);
        
        
            FadeTransition fd3=new FadeTransition(Duration.millis(500));
            fd3.setNode(reportcreation);
        
            fd3.setFromValue(0);
        
            fd3.setToValue(1);
            fd2.setOnFinished(event->{
                updateMessage("Preparing Report");
                bsp.setProgress(100);
                updateProgress(75,100);
                fd3.play();
            });
       
         fd3.setOnFinished(event->{
             
             updateProgress(100,100);
         //Create The Plagiarism Report
         Task<Void> report_creation = new Task(){
             @Override
             protected Object call() throws Exception {
                 Properties.isReady = false;
                    updateMessage("Creating Report"); 
                    updateProgress(0,100);
                      try{
                    System.out.println(ReportCreator.CreateReport());
                    updateProgress(-1,100);
                    updateMessage("Report Complete");
                    }
                    catch(Exception e){
                    System.out.println(e+" EXCEPTION THROWN");
                    }
                    Properties.isReady = true;
                    updateProgress(100,100);
                 return null;
             }
         
         };
         csp.progressProperty().bind(report_creation.progressProperty());
         Thread task_doer = new Thread(report_creation);
         task_doer.start();
         updateMessage("Report Complete");
         });
         
         
         
          
        FadeTransition fd=new FadeTransition(Duration.millis(500));
        fd.setNode(websearch);
        
        fd.setFromValue(0);
        
        fd.setToValue(1);
        
        
        
        fd.play();
        fd.setOnFinished(event->{
            updateMessage("Checking For Plagiarism");
            updateProgress(50,100);
            Task<Void> T= new Task<>(){
            @Override
            protected Void call() throws Exception {
                //here we check for plagairism 
            int row=0;
            for(String filename:resultant[0])
            {
            double progress=(double)((double)(row-1)/(double)resultant[0].length)*(double)100.0;
            System.out.println(progress+"%");
            
           updateProgress(progress,100);
            
           System.out.println(filename+" : "+resultant[1][row]);
          
           
           String resultset = resultant[1][row];
           if(resultset.trim().length() <= 2)
           {
              //insufficient data
              //updateMessage();
           }
           else{
              
            
               List<String> list = new ArrayList<>();
               String results[] = resultset.split("::");
               
               for(String individual:results)
               {
                  list.add(individual);
                  
                  
               }
               
               
               Collections.sort(list);
               
               
               int count = 0;
               String processed = "";
             for(String individual_strings:list)
             {
                
               if(Properties.getValue("plagcheck").equals("Partial")) {       
               if(individual_strings.length()>3)
               {
                 
                   //perform a plag check
                  
                  boolean result = PLBOT.search(individual_strings, 250000);
                  List<String> sources = PLBOT.getSources();
                  String sorcerrer = "";
                  for(String source:sources){
                      sorcerrer = source;
                      break;
                  
                  }
                 
                      
                  String formulated = individual_strings+"->"+sorcerrer+"|"+result;
                  
                 processed += formulated + "::";
                  
                  
                  count++;
                  if(count == 3)
                  {
                  break;
                  }
               
               }
               else
               System.out.println(individual_strings+" Omitted Because The String Is Too Small");
               }
               
               else{ //full plag check
                   
                    boolean result = PLBOT.search(individual_strings, 250000);
                    if(Properties.getValue("proxy").trim().length()<=1)
                    Thread.sleep(1000);
                    List<String> sources = PLBOT.getSources();
                    String sorcerrer = "";
                  for(String source:sources){
                      sorcerrer = source;
                      break;
                  
                  }
                  String formulated = individual_strings+"->"+sorcerrer+"|"+result;
                  
                  processed += formulated + "::";
                  
                  
                 
               
               }
               
              }
              resultant[1][row] = processed; 
               
           }
           
               
               
           
           row++;
           }
           
                updateMessage("Web Search Complete");
                Properties.plagpercentage=resultant;
                
                
               
                for(int i=0;i<Properties.plagpercentage[0].length;i++)
                {
                    System.out.println(Properties.plagpercentage[0][i]+" : "+Properties.plagpercentage[1][i]);
                }
                
                
                    return null;
                }
            };
            msp.progressProperty().bind(T.progressProperty());
            Thread rtx=new Thread(T);
            rtx.start();
           T.setOnSucceeded(e ->{
               msp.progressProperty().unbind();
               msp.setProgress(100);
       fd2.play();
           });
        
        });
        
        
        
        
        
       
        
        
        
                

        
        
        
        
        
        }
        else if(type_of_operation.equals("documental")){
           FadeTransition fd=new FadeTransition(Duration.millis(200));
        fd.setNode(websearch);
        
        fd.setFromValue(0);
        
        fd.setToValue(1);
        
        
        websearch.setDisable(true);
        
        
        
        FadeTransition fd2=new FadeTransition(Duration.millis(200));
        fd2.setNode(documentalsearch);
        
        fd2.setFromValue(0);
        
        fd2.setToValue(1);
        
        
        FadeTransition fd3=new FadeTransition(Duration.millis(200));
        fd3.setNode(reportcreation);
        
        fd3.setFromValue(0);
        
        fd3.setToValue(1);
        
        
        fd3.setOnFinished(event ->{
        Task<Void> terrible =new Task<>(){
            @Override
            protected Void call() throws Exception {
                updateProgress(-1,100);
                updateMessage("Creating Report");
                Properties.isReady = false;
                ReportCreator.CreateReport();
                Properties.isReady = true;
                updateMessage("Report Created");
                updateProgress(100,100);
                
                return null;
            }
        };
        
        csp.progressProperty().bind(terrible.progressProperty());
        Thread creator = new Thread(terrible);
        creator.setDaemon(true);
        creator.start();
        terrible.setOnSucceeded(e->{
        updateProgress(100,100);
        });
        });
        
        
        
        
        fd.setOnFinished(event->{
         msp.setProgress(100);
        fd2.play();
        });
        
        //a source map contains a lot of information
        //sentence[file1]<->sentence[file2]<->....sentence[fileN] -> source_url
        Properties.source_mapping = new ArrayList<>();
        fd2.setOnFinished(event->{
            updateProgress(50,100);
            //traverse through the other documents to search for plag
            Task<Void> documentSearch = new Task<>(){
                @Override
                protected Void call() throws Exception {
                    double progress_counter = 0.0;
                    if(resultant[0].length > 1)
                    {
                       String file = resultant[0][0];
                       
                       
                       String sentences[] = resultant[1][0].split("::");
                       
                       //finds all interplagiarised sentences
                       //we start from the second element
                       for(int i=1;i<resultant[0].length;i++)
                       {
                        
                          
                         String comparative_sentences[] = resultant[1][i].split("::");  
                         
                         for(String sentence:sentences)
                         {
                                double progress= ((progress_counter++)/(double)resultant[0].length)*100.0; 
                                System.out.println(progress+" is the progress");
                                updateProgress(progress,100);
                             for(String compared_sentence:comparative_sentences)
                             {
                                     if(sentence.equals(compared_sentence))
                                     {
                                         Properties.source_mapping.add(sentence+"["+file+"]<=>"+sentence+"["+resultant[0][i]+"]");
                                     }
                             }
                               
                         }
                       
                       }
                       System.out.println("____________________________");
                       Properties.source_mapping.forEach(str->{
                       System.out.println(str);
                       });
                    
                    }
                    else{
                    System.out.println("You Must Select Multiple Documents To Conduct The Search");
                    }
                    
               return null;
                }
            };
            bsp.progressProperty().bind(documentSearch.progressProperty());
            Thread rtx=new Thread(documentSearch);
            rtx.start();
            
        fd3.play();
        });
        
        
        
                fd.play();

        }
        else
        {
        Properties.source_mapping = new ArrayList<>();
        FadeTransition fd=new FadeTransition(Duration.millis(200));
        fd.setNode(websearch);
        
        fd.setFromValue(0);
        
        fd.setToValue(1);
        
        
        
        
        FadeTransition fd2=new FadeTransition(Duration.millis(200));
        fd2.setNode(documentalsearch);
        
        fd2.setFromValue(0);
        
        fd2.setToValue(1);
        
        
        FadeTransition fd3=new FadeTransition(Duration.millis(200));
        fd3.setNode(reportcreation);
        
        fd3.setFromValue(0);
        
        fd3.setToValue(1);
        
        fd.setOnFinished(event->{
           
            Task<Void> T= new Task<>(){
                @Override
            protected Void call() throws Exception {
                   //check for common points 
                //here we check for plagairism 
             int row=0;
            for(String filename:resultant[0])
            {
            double progress=(double)((row+1)/resultant[0].length)*100;
            System.out.println(progress+"%");
           updateProgress(progress,100);
            
           System.out.println(filename+" : "+resultant[1][row]);
          
           
           String resultset = resultant[1][row];
           if(resultset.trim().length() <= 2)
           {
              //insufficient data
              //updateMessage();
           }
           else{
              
            
               List<String> list = new ArrayList<>();
               String results[] = resultset.split("::");
               
               for(String individual:results)
               {
                  list.add(individual);
                  
                  
               }
               
               
               Collections.sort(list);
               
               
               int count = 0;
               String processed = "";
             for(String individual_strings:list)
             {
                
               if(Properties.getValue("plagcheck").equals("Partial")) {       
               if(individual_strings.length()>3)
               {
                 
                   //perform a plag check
                  
                  boolean result = PLBOT.search(individual_strings, 250000);
                   List<String> sources = PLBOT.getSources();
                  String sorcerrer = "";
                  for(String source:sources){
                      sorcerrer = source;
                      break;
                  
                  }
                 
                      
                  String formulated = individual_strings+"->"+sorcerrer+"|"+result;
                 
                 processed += formulated + "::";
                  
                  
                  count++;
                  if(count == 3)
                  {
                  break;
                  }
               
               }
               else
               System.out.println(individual_strings+" Omitted Because The String Is Too Small");
               }
               
               else{ //full plag check
                   
                    boolean result = PLBOT.search(individual_strings, 250000);
                  List<String> sources = PLBOT.getSources();
                  String sorcerrer = "";
                  for(String source:sources){
                      sorcerrer = source;
                      break;
                  
                  }
                 
                      
                  String formulated = individual_strings+"->"+sorcerrer+"|"+result;
                  
                 processed += formulated + "::";
                  
                  
                 
               
               }
               
              }
              resultant[1][row] = processed; 
               
           }
           
               
               
           
           row++;
           }
           
                updateMessage("Web Search Complete");
                Properties.plagpercentage=resultant;
                
               
                for(int i=0;i<Properties.plagpercentage[0].length;i++)
                {
                    System.out.println(Properties.plagpercentage[0][i]+" : "+Properties.plagpercentage[1][i]);
                }
                
                
                
                
                
                
                
                    return null;
                }
            };
            msp.progressProperty().bind(T.progressProperty());
            Thread rtx=new Thread(T);
            rtx.start();
           
          T.setOnSucceeded(eh->{
          fd2.play();
          });
        });
        
        
        
        fd2.setOnFinished(event->{
            
            Task<Void> source_mapping = new Task<>(){
                @Override
                protected Void call() throws Exception {
                  //create source maps;
                  double progress_counter = 0;
                  try{
                  if(resultant[0].length > 1)
                    {
                       double progress = (progress_counter++/(double)resultant[0].length)*100.0; 
                       updateProgress(progress,100);
                       String file = resultant[0][0];
                    
                       String sentences[] = resultant[1][0].split("::");
                       System.out.println(sentences.length+" IS THE LENGTH");
                       System.out.println(sentences[0]);
                       System.out.println(sentences[1]);
                       //finds all interplagiarised sentences
                       //we start from the second element
                       for(int i=1;i<resultant[0].length;i++)
                       {
                          
                         String comparative_sentences[] = resultant[1][i].split("::");  
                         
                         for(String sentence:sentences)
                         {
                             String leftportion = sentence.substring(0,sentence.lastIndexOf("->"));
                             
                             String rightportion = sentence.substring(sentence.lastIndexOf("->"));
                             
                             for(String compared_sentence:comparative_sentences)
                             {
                                     String left_com = compared_sentence.substring(0,compared_sentence.lastIndexOf("->"));
                                     String right_com = compared_sentence.substring(compared_sentence.lastIndexOf("->"));
                                     if(leftportion.equals(left_com))
                                     {
                                         System.out.println(left_com+"["+file+"]<=>"+left_com+"["+resultant[0][i]+"]"+rightportion);
                                         Properties.source_mapping.add(left_com+"["+file+"]<=>"+left_com+"["+resultant[0][i]+"]"+rightportion);
                                     }
                             }
                               
                         }
                       
                       }
                       System.out.println("____________________________");
                       Properties.source_mapping.forEach(str->{
                       System.out.println(str);
                       });
                    
                    }
                  
                 
                    else{
                    System.out.println("You Must Select Multiple Documents To Conduct The Search");
                    }
                  }
                   catch(Exception e){
                   System.out.println(e+" IS THE EXCE");
                   }
                  System.out.println("Reached Here");
                  return null;
                }
            };
            bsp.progressProperty().bind(source_mapping.progressProperty());
            Thread mapper = new Thread(source_mapping);
            mapper.setDaemon(true);
            mapper.start();
            source_mapping.setOnSucceeded(eh->{
                 fd3.play();
            });
            
       
        });
        
        
        fd3.setOnFinished(event->{
        
                 Task<Void> terra = new Task<>(){
                 @Override
                 protected Void call() throws Exception {
                      Properties.isReady = false;
                      updateProgress(-1,100);
                      try{
                      ReportCreator.CreateReport();
                      }
                      catch(Exception e){
                      System.out.println(e+" REPORT EXCEPTION");
                      }
                      updateProgress(100,100);
                      Properties.isReady = true;
                      System.out.println("Is READY");
                      return null;
                }
                 
                 };
                 csp.progressProperty().bind(terra.progressProperty());
                 Thread rtx = new Thread(terra);
                 rtx.start();
                 });
        
        
                fd.play();

        
        
        
        
        }
        
        
        
        
   
        
     
      
        
       
    }

    @Override
    protected Void call() throws Exception {
        
        Process();
        
        
        
        
        return null;
        
    }
    
    public boolean isASkippableLine(String line){
     boolean result = false;
     String lines[] = Properties.getValue("slines").split(",");
    
     for(String value:lines)
     {
         
      if(value.trim().length()>0&&line.trim().contains(value.trim()))
      {
        System.out.println("We have to skip "+line);
        result = true;
        break;
      }
     }
     return result;
    }
}
    
   