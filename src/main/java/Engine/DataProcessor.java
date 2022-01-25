/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import java.awt.image.BufferedImage;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
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
        Properties.total_words_per_file = new int[filepaths.size()];
        System.out.println(filepaths.size());
        System.out.println(Properties.plagpercentage.length);
        
        int count = 0;
        
        for(File file:filepaths)
        {
            System.out.println("Counter "+count);
        Properties.plagpercentage[0][count] = file.getName();
        Properties.plagpercentage[1][count] = "";
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
            int totalwords = 0;
           updateMessage("Reading File");
           updateProgress(-1,100);
            if(file.getName().contains(".txt")){
            try {
                updateMessage("Loading Files");
                Scanner sc=new Scanner(file);
                
                while(sc.hasNext())
                {
                    updateMessage("Extracting Text");
                  StringTokenizer st=new StringTokenizer(sc.nextLine(),".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                  String token=st.nextToken();
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                  
                  }
                }
                
                 
            } catch (FileNotFoundException ex) {
              System.out.println("File Not Found");
            }
            
        }
        else if(file.getName().contains(".pdf"))
        {
               try {
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
                   Scanner sd=new Scanner(result);
                while(sd.hasNext())
                {
                updateMessage("Extracting Text");
                StringTokenizer st=new StringTokenizer(sd.nextLine(),".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                  String token=st.nextToken();
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                  }
                
                }
                
                
                
                  }  
                   
                   //apache pdfbox to parse
               } catch (Exception ex) {
                   Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
               }
        }
        else if(file.getName().contains(".docx"))
        { // apache poi
               try {
                   XWPFDocument doc = new XWPFDocument(new FileInputStream(file));
                   
                  
                  List<XWPFParagraph> list = doc.getParagraphs();
            for (XWPFParagraph paragraph : list) {
                
                //from these paragraphs we extract the text
               // System.out.println(paragraph.getText()+"\n___________________________________\n");
                
                StringTokenizer sd = new StringTokenizer(paragraph.getText(),".!?");
                while(sd.hasMoreElements())
                {
                 String token=sd.nextToken();
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                }
                
            }
               
                 
               } catch (Exception ex) {
                   Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
               }
        }
        else if(file.getName().contains(".html"))
        {
               try {
                   // jsoup to parse
                   String entire_text = Jsoup.parse(file, "UTF-8").text();
                   Scanner sd=new Scanner(entire_text);
                while(sd.hasNext())
                {
                updateMessage("Extracting Text");
                StringTokenizer st=new StringTokenizer(sd.nextLine(),".!?");
                  // tokenize the Strings
                  while(st.hasMoreTokens()){
                   String token=st.nextToken();
                  strings.add(token+"|"+file.getName());
                  totalwords+=token.split(" ").length;
                 
                  }
                
                }
                   
                   
               } catch (Exception ex) {
                   Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
               }
            
        }
            Properties.total_words_per_file[Properties.array_pos]=totalwords;
         Properties.array_pos++;    
        }
             
           );
        Properties.array_pos =0;
        
        
         updateMessage("Bifurcating Data");
        String resultant[][]=Properties.plagpercentage;
        //
        int counter= 0;
        System.out.println("HERE");
        for(String filename:resultant[0])
        {
          System.out.print(filename+":");
        for(String str:strings)
        {
            
             try{
            
            if(str.substring(str.lastIndexOf("|")+1).equals(filename))
            {
            
            resultant[1][counter] += str.substring(0,str.lastIndexOf("|"))+"::";
            }
             }
             catch(Exception e){
             System.out.println(e);
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
            documentalsearch.setDisable(true);
          
            
            
            
            FadeTransition fd2=new FadeTransition(Duration.millis(500));
        fd2.setNode(documentalsearch);
        
        fd2.setFromValue(0);
        
        fd2.setToValue(1);
        
        
        FadeTransition fd3=new FadeTransition(Duration.millis(500));
        fd3.setNode(reportcreation);
        
        fd3.setFromValue(0);
        
        fd3.setToValue(1);
         fd2.setOnFinished(event->{
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
                      try{
                    System.out.println(ReportCreator.CreateReport());
                  
                    updateMessage("Report Complete");
                    }
                    catch(Exception e){
                    System.out.println(e+" EXCEPTION THROWN");
                    }
                    Properties.isReady = true;
                 return null;
             }
         
         };
         
         Thread task_doer = new Thread(report_creation);
         task_doer.start();
         
         });
         
         
         
          
        FadeTransition fd=new FadeTransition(Duration.millis(500));
        fd.setNode(websearch);
        
        fd.setFromValue(0);
        
        fd.setToValue(1);
        
        
        
        fd.play();
        fd.setOnFinished(event->{
            updateProgress(50,100);
            Task<Void> T= new Task<>(){
                @Override
                protected Void call() throws Exception {
                //here we check for plagairism 
                    int row=0;
            for(String filename:resultant[0])
            {
            double progress=(double)((double)(row+1)/(double)resultant[0].length)*(double)100.0;
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
           T.setOnSucceeded(e ->{
               
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
                updateMessage("Creating Report");
                Properties.isReady = false;
                ReportCreator.CreateReport();
                Properties.isReady = true;
                updateMessage("Report Created");
                
                return null;
            }
        };
        
        
        Thread creator = new Thread(terrible);
        creator.setDaemon(true);
        creator.start();
        });
        
        
        
        
        fd.setOnFinished(event->{
        
        fd2.play();
        });
        
        //a source map contains a lot of information
        //sentence[file1]<->sentence[file2]<->....sentence[fileN] -> source_url
        Properties.source_mapping = new ArrayList<>();
        fd2.setOnFinished(event->{
            //traverse through the other documents to search for plag
            Task<Void> documentSearch = new Task<>(){
                @Override
                protected Void call() throws Exception {
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
            Thread rtx=new Thread(documentSearch);
            rtx.start();
            
        fd3.play();
        });
        
        
        
                fd.play();

        }
        else
        {
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
               String results[] = resultset.split(",");
               
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
           
        fd2.play();
        });
        
        
        
        fd2.setOnFinished(event->{
            
            Task<Void> source_mapping = new Task<>(){
                @Override
                protected Void call() throws Exception {
                  //create source maps;
                  
                  
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
                             String leftportion = sentence.substring(0,sentence.lastIndexOf("->"));
                             
                             String rightportion = sentence.substring(sentence.lastIndexOf("->"));
                             
                             for(String compared_sentence:comparative_sentences)
                             {
                                     String left_com = compared_sentence.substring(0,compared_sentence.lastIndexOf("->"));
                                     String right_com = compared_sentence.substring(compared_sentence.lastIndexOf("->"));
                                     if(leftportion.equals(left_com))
                                     {
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
                  
                  return null;
                }
            };
            Thread mapper = new Thread(source_mapping);
            mapper.setDaemon(true);
            mapper.start();
        fd3.play();
        });
        
        
        fd3.setOnFinished(event->{
        
                 Task<Void> terra = new Task<>(){
                 @Override
                 protected Void call() throws Exception {
                      Properties.isReady = false;
                      try{
                      ReportCreator.CreateReport();
                      }
                      catch(Exception e){
                      System.out.println(e);
                      }
                      Properties.isReady = true;
                      System.out.println("Is READY");
                      return null;
                }
                 
                 };
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
}
    
   