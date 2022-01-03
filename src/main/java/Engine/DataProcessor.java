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
    }
    
    public void reset(){
    
    }
    public void Process()
    {
        
        List<String> strings=new ArrayList();
        List<String> docname = new ArrayList();
        filepaths.forEach(file -> {
            
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
                  strings.add(st.nextToken());
                  docname.add(file.getName());
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
                
                StringTokenizer sd = new StringTokenizer(paragraph.getText());
                while(sd.hasMoreElements())
                {
                strings.add(sd.nextToken());
                 docname.add(file.getName());
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
                  strings.add(st.nextToken());
                  docname.add(file.getName());
                  }
                
                }
                   
                   
               } catch (Exception ex) {
                   Logger.getLogger(DataProcessor.class.getName()).log(Level.SEVERE, null, ex);
               }
            
        }
            
        }
              
           );
       // strings.size();
        //searching the web
        
        //se
        updateMessage("Text Extraction Complete");
        
        try {
            Document doc=Jsoup.connect("https://www.google.co.in//").get();
        } catch (IOException ex) {
           updateMessage("Unable To Connect");
        }
        
      Collections.sort(strings,Collections.reverseOrder());
     
      strings.forEach(str->System.out.println(str));
      System.out.println(strings);
        
        
        
        
        
        
        
        if(type_of_operation.equals("websearch"))
        {
            documentalsearch.setDisable(true);
            documentalsearch.setStyle("-fx-background-color: #f9f9f9;");
            FadeTransition fd2=new FadeTransition(Duration.millis(500));
        fd2.setNode(documentalsearch);
        
        fd2.setFromValue(0);
        
        fd2.setToValue(1);
        
        
        FadeTransition fd3=new FadeTransition(Duration.millis(500));
        fd3.setNode(reportcreation);
        
        fd3.setFromValue(0);
        
        fd3.setToValue(1);
         fd2.setOnFinished(event->{
            System.out.println("Animations3");
        fd3.play();
        });
       
            System.out.println("Animations");
        FadeTransition fd=new FadeTransition(Duration.millis(500));
        fd.setNode(websearch);
        
        fd.setFromValue(0);
        
        fd.setToValue(1);
        
        
        
        fd.play();
        fd.setOnFinished(event->{
            
            Task<Void> T= new Task<>(){
                @Override
                protected Void call() throws Exception {
                    updateProgress(-1,100);
                    
                    
                    
                    PLBOT.search("", 25000);
                    
                   
                    
                    return null;
                }
            };
            msp.progressProperty().bind(T.progressProperty());
            Thread rtx=new Thread(T);
            rtx.start();
           T.setOnSucceeded(e ->{
               System.out.println("Animations2here2");
       fd2.play();
           });
        
        });
         System.out.println("Animationshere");
        
        
        
        
       
        
        
        
                

        
        
        
        
        
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
        
        fd.setOnFinished(event->{
        
        fd2.play();
        });
        
        
        
        fd2.setOnFinished(event->{
        fd3.play();
        });
        
        
        
                fd.play();

        }
        else{
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
        
        fd2.play();
        });
        
        
        
        fd2.setOnFinished(event->{
        fd3.play();
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
