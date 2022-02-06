/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.fxplagiarismchecker;

import Engine.Properties;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 * The Controller for Report Previewer
 * @author joey
 */
public class ReportCreator implements Initializable {
    /**
     * State Variables
     */
    //will contain as many entries as the number of files
    static String documents[];
    
    //will contain the metadata of files, seperated by the delimeter '::'
    static String data[];
    String type;
    
    
    int current_position = 0;
    int array_length = 0;
    
    @FXML
    MFXProgressSpinner plagiarism_percentage;
    @FXML
    ListView sources;
    @FXML
    Text report_title;
    @FXML
    Button left;
    @FXML
    Button right;
    @FXML
    StyleClassedTextArea stc;
    @FXML
    Label current_document_name;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     stc.setParagraphGraphicFactory(LineNumberFactory.get(stc));
     
     setup();
     
      
    }
    
    @FXML
    public void Next_Document(ActionEvent e)
    {
        System.out.println(e.getSource());
            stc.setStyle("-fx-background-radius: 10; -fx-background-color: #f7f7f7;");
            stc.setStyleClass(0,5,"low");
            if(e.getSource().toString().contains("right"))
            {
                current_position++;
                if(current_position > array_length)
                    current_position = 0;
               
                
                String document_name = new Scanner(documents[current_position]).nextLine().trim();
           
                String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                        .filter(line -> !line.trim().equals(document_name.trim()))
                        .collect(Collectors.joining()).trim();
                    
                
               
                
                stc.replaceText(document);
                current_document_name.setText(document_name);
                String values[] = data[current_position].split("`");
                plagiarism_percentage.setProgress(Double.parseDouble(values[1])/100.0);
                hydrate(values[2]);
            }
            else{
                current_position--;
                if(current_position < 0)
                    current_position  = 0;
                
                String document_name = new Scanner(documents[current_position]).nextLine().trim();
           
                String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                        .filter(line -> !line.trim().equals(document_name.trim()))
                        .collect(Collectors.joining()).trim();
                
                stc.replaceText(document);
                current_document_name.setText(document_name);
                String values[] = data[current_position].split("`");
                plagiarism_percentage.setProgress(Double.parseDouble(values[1])/100.0);
                hydrate(values[2]);
            }
           
        
        
    }
    public String translate(String predicate){
        String splits[]  = predicate.split("<=>");
        String sentence = "";
        int count = 0;
        for(String token:splits)
        {
            
          sentence += token.substring(token.lastIndexOf("[")+1,token.length()-1) + (count == 0?" And ":"");
          count++;
        }
        sentence += " have '"+splits[0].substring(0,splits[0].lastIndexOf("["))+"'";
    return sentence;
    }
    public void hydrate(String source_maps)
    {
     

     sources.getItems().clear();
     source_maps = source_maps.substring(1,source_maps.length()-3);
     if(type.contains("WebSearch"))
     Arrays.stream(source_maps.split("::")).forEach(item -> sources.getItems().add(item.substring(item.lastIndexOf(">")+1)));
    
     else if(type.contains("Interdocument Search")){
         
     Arrays.stream(source_maps.split("::"))
             .filter(item -> item.contains(current_document_name.getText()))
             .forEach(item ->sources.getItems().add(translate(item)));
     
     System.out.println("InterDocument"+source_maps);
     
     //Arrays.stream(source_maps.split("::")).forEach(item -> sources.getItems().add(item.substring(item.lastIndexOf(">")+1)));
     }
     else{
    // Arrays.stream(source_maps.split("::")).forEach(item -> sources.getItems().add(item.substring(item.lastIndexOf(">")+1)));
     }
     
    
    }
    
    @FXML
    public void sentence_tracer(MouseEvent e){
        
        if(e.getClickCount()==3){
        String url=sources.getFocusModel().getFocusedItem().toString();
        
        try {
            if(url.contains("http"))
            Desktop.getDesktop().browse(new URL(url).toURI());
        }
        catch(Exception ed){
            
        }
       }
        //stc.getStylesheets().add(getClass().getResource("styles/main.css").toExternalForm());
        
        String source = sources.getFocusModel().getFocusedItem().toString();
        if(source!=null)
        {
            
            String value = data[current_position].split("`")[2];
            String relevant_sentences[] = Arrays
                    .stream(value.substring(1, value.length()-3)
                    .split("::"))
                    .filter(item -> item.contains(source)).collect(Collectors.joining("`"))
                    .trim()
                    .split("`");
            
            String document_name = new Scanner(documents[current_position]).nextLine().trim();
            String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                            .filter(line -> !line.trim().equals(document_name.trim()))
                            .collect(Collectors.joining()).trim();
            
            stc.setStyleClass(0,document.length()-11,"low");
        
         if(type.contains("WebSearch")){

            for(String sentence:relevant_sentences){

                int position = document.indexOf(sentence.substring(0, sentence.lastIndexOf("->")));
                System.out.println(position);
                System.out.println(sentence.substring(0, sentence.lastIndexOf("->")));
                if(position>-1) 
                    stc.setStyleClass(position,position+sentence.subSequence(0, sentence.lastIndexOf("->")).length(), "high");
        
            
            }
         }
         else if(type.contains("Interdocument Search"))
         {
            String sentence = source.substring(source.indexOf("'")+1, source.lastIndexOf("'"));
            int position = document.indexOf(sentence);
            System.out.println(sentence);
            
            if(position>-1) 
                    stc.setStyleClass(position,position+sentence.length(),"high");
            
         
         }
         else{
            
         }
        
        }
    
    }
    
   
    public void setup()
    {
        
       
            process();
            
            String document_name = new Scanner(documents[current_position]).nextLine();
           
                String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                        .filter(line -> !line.trim().equals(document_name.trim()))
                        .collect(Collectors.joining());
             System.out.println(document_name+" is the document name");  
             stc.replaceText(document.trim());
             current_document_name.setText(document_name);
            //loading an externally loaded document
            
            if(documents.length <= 1)
            {
                left.setManaged(false);
                right.setManaged(false);
                left.setVisible(false);
                right.setVisible(false);
            }
      
     
            
 
        
        
    
    }
    public String process(){
        String report_copy = Properties.plagReport;
        Scanner scanner = new Scanner(report_copy);
        


        //Extracting The Documents
        int document_count = 0;
        while(scanner.hasNext())
        {
           String line = scanner.nextLine();
           if(line.contains("filename:"))
           {
             document_count++;
           }
        
        }
        
        documents = new String[document_count];
        data = new String[document_count];
        
        scanner = new Scanner(report_copy);
        
         int pointer = 0;
         int pointer2 = 0;
        while(scanner.hasNext())
        {
            String line = scanner.nextLine();
            
            if(line.contains("filename:")&&line.substring(line.indexOf(":")+1).trim().length()>0)
            {
               
                String buffer = line.substring(line.indexOf(":")+1)+"\r\n";//extracts the text on the same line
                line = scanner.nextLine(); //moves to the next line
                
                //System.out.println("Started at "+buffer);
              //  System.out.println("looped for "+line);
                while(!(line.contains("<-->")))
                {
                    buffer += line+"\r\n";
                    line = scanner.nextLine();
                    
                    //System.out.println(line +" in the loop "+buffer +" is the buffer");
                
                }
                
                documents[pointer] = buffer.trim();
                //System.out.println("Document "+documents[pointer]+" END DOCUMENT||");
                
                pointer++;
               // System.out.println("Broken at "+line);
               // System.out.println("Buffer is "+buffer+" ___buffer_end");
                buffer = "";
            }
            
            if(line.contains("Title:"))
            {
                report_title.setText(line.substring(line.indexOf(":")+1));
            }
            if(line.contains("Report Info:"))
            {
               type = line.substring(line.indexOf(":")+1); 
            }
            if(line.contains("doc_name"))
            {
                while(scanner.hasNext())
                {
                    line = scanner.nextLine();
                    String values[] = line.split(Properties.global_delimeter);
                    String plag_percentage = values[1];
                    
                        if(pointer2 == 0)
                        {
                            System.out.println(Double.parseDouble(plag_percentage)+" here");
                            plagiarism_percentage.setProgress(Double.parseDouble(plag_percentage)/100.0);
                            hydrate(values[2].substring(1, values[2].length()-3));

                        }
                    data[pointer2] =  line;
                    pointer2++;
                    
                 
                }
            }
           //System.out.println("Current Line: - "+line+(line.substring(line.indexOf(":")+1).trim().length()>0)+line.contains("filename:"));
        
        }
       
        
        
        //Arrays.stream(documents).forEach(item -> System.out.println(item +" ________"));
        
        
    array_length = documents.length-1;
    return "";
    }
    
    
   
    
    
    
}
