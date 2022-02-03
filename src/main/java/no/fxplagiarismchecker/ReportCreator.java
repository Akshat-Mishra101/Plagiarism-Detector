/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.fxplagiarismchecker;

import Engine.Properties;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import java.net.URL;
import java.util.Arrays;
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
        
            if(e.getSource().toString().contains("right"))
            {
                current_position++;
                if(current_position > array_length)
                    current_position = 0;
               
                
                String document_name = new Scanner(documents[current_position]).nextLine();
           
                String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                        .filter(line -> !line.trim().equals(document_name.trim()))
                        .collect(Collectors.joining());
                    
                
               
                
                stc.replaceText(document);
                current_document_name.setText(document_name);
            }
            else{
                current_position--;
                if(current_position < 0)
                    current_position  = 0;
                
                String document_name = new Scanner(documents[current_position]).nextLine();
           
                String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                        .filter(line -> !line.trim().equals(document_name.trim()))
                        .collect(Collectors.joining());
                
                stc.replaceText(document);
                current_document_name.setText(document_name);
            }
            System.out.println(data[current_position]);
        
        
    }
    
    public void setup()
    {
        
       
            process();
            
            String document_name = new Scanner(documents[current_position]).nextLine();
           
                String document  = Arrays.stream(documents[current_position].split("/\r\n|\n"))
                        .filter(line -> !line.trim().equals(document_name.trim()))
                        .collect(Collectors.joining());
                
             stc.replaceText(document);
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
                
                documents[pointer] = buffer;
                System.out.println("Document "+documents[pointer]+" END DOCUMENT||");
                
                pointer++;
               // System.out.println("Broken at "+line);
                System.out.println("Buffer is "+buffer+" ___buffer_end");
                buffer = "";
            }
            
            if(line.contains("Title:"))
            {
                report_title.setText(line.substring(line.indexOf(":")+1));
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
                            plagiarism_percentage.setProgress(Double.parseDouble(plag_percentage));

                        }
                    data[pointer2] =  line;
                    pointer2++;
                    
                 
                }
            }
           //System.out.println("Current Line: - "+line+(line.substring(line.indexOf(":")+1).trim().length()>0)+line.contains("filename:"));
        
        }
        for(String value:documents)
        {
        System.out.println(value+"\r\n"+"_________");
        }
        
        
        //Arrays.stream(documents).forEach(item -> System.out.println(item +" ________"));
        
        
    array_length = documents.length-1;
    return "";
    }
    
    
   
    
    
    
}
