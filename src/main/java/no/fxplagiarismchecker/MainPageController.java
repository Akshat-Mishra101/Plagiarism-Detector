/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.fxplagiarismchecker;

import Engine.DataProcessor;
import java.io.File;
import java.net.URL;
import java.util.List;
import static java.util.Locale.filter;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author joey
 */
public class MainPageController implements Initializable{
  
    @FXML
    Text t1;
    @FXML
    Text t2;
    @FXML 
    
    Text t3;
    
    @FXML
   AnchorPane settings_pane;
    
     @FXML
     TextField API;
    @FXML
    Spinner timeout;
    @FXML
    AnchorPane btnpane;
    @FXML
    AnchorPane buttonhoverpane;
    @FXML
    Button compare_docs_btno;
    @FXML
    Button internet_search_btno;
    @FXML
    Button search_several_articles_btno;
    @FXML
    Button settings_btn;
    @FXML
    Button close_button;
    static int place[][]={{1,0},{2,100},{3,100}};
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      buttonhoverpane.setOpacity(0);
      timeout.setStyle("-fx-background-color: -fx-control-inner-background;");
      t1.setOpacity(0);
      t2.setOpacity(0);
      t3.setOpacity(0);
    }
    
    @FXML
    public void reveal_and_hide(Event e)
    {
         FadeTransition fd=new FadeTransition(Duration.millis(200));
        System.out.println(e.getEventType());
        if(e.getEventType().toString().contains("EXITED"))
        {
        fd.setToValue(0);
        
        }
       
        else
        {
         fd.setToValue(1);
        }
        
        
     
        
        
        if(e.getSource().toString().contains("compare_docs"))
        {
           fd.setNode(t2);
        }
        
        if(e.getSource().toString().contains("internet_search"))
        {
              fd.setNode(t1);
        }
        if(e.getSource().toString().contains("search_several"))
        {
              fd.setNode(t3);
        }
        fd.play();
        System.out.println("Reveal");
    }
    boolean hasentered=false;
    
    
    @FXML
    public void clicked_menu(Event e)
    {
    FileChooser fc=new FileChooser();
    fc.setTitle("Select The Documents");
        FileChooser.ExtensionFilter filter1=new FileChooser.ExtensionFilter("Text Files (*.txt", "*.txt");
        FileChooser.ExtensionFilter filter2=new FileChooser.ExtensionFilter("PDF Files (*.pdf", "*.pdf");
        FileChooser.ExtensionFilter filter3=new FileChooser.ExtensionFilter("Docs Files (*.docx", "*.docx");
        FileChooser.ExtensionFilter filter4=new FileChooser.ExtensionFilter("HTML Files (*.html", "*.html");
       fc.getExtensionFilters().add(filter1);
       fc.getExtensionFilters().add(filter2);
       fc.getExtensionFilters().add(filter3);
       fc.getExtensionFilters().add(filter4);
       List<File> list_of_files=null;
    
     if((list_of_files=fc.showOpenMultipleDialog(API.getScene().getWindow()))!=null)
     {
    new DataProcessor(list_of_files).Process();
     }
       else
       {
       System.out.println("It is Null");
       }
       
    }
    @FXML
    public void setting_click(Event e)
    {
        
        TranslateTransition tt=new TranslateTransition(Duration.millis(300));
        tt.setNode(settings_pane);
        tt.setToX(350);
        
        tt.play();
    
    }
    
    @FXML 
    public void close_button()
    {
     
        TranslateTransition tt=new TranslateTransition(Duration.millis(300));
        tt.setNode(settings_pane);
        tt.setToX(-351);
        
        tt.play();
    }
    @FXML
    public void buttonAnimations(Event E)
    {
    
    String source=E.getSource().toString();
    System.out.println(source);
    if(source.contains("settings_btn"))
    {
     RotateTransition rt=new RotateTransition(Duration.millis(200));
    rt.setNode(settings_btn);
    rt.setFromAngle(0);
    rt.setToAngle(45);
    rt.play();
    
    }
    if(source.contains("btno")||source.contains("btnpane"))
    {
    reveal_and_hide(E);
    FadeTransition fd=new FadeTransition(Duration.millis(100));
    fd.setNode(buttonhoverpane);
    fd.setToValue(0.1);
    fd.play();
    
    System.out.println(source+" is this");
    
    if(source.contains("btno"))
    {
       
        int to=0;
        if(source.contains("compare_docs"))
        {
            to=163;
        }
        else if(source.contains("internet_search"))
        {
          to=0;
        }
        else if(source.contains("search_several"))
        {
        to=323;
        }
        TranslateTransition tt=new TranslateTransition(Duration.millis(120));
        tt.setNode(buttonhoverpane);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setToX(to);
        tt.play();
       
    }
    //  turn the opacity to 0.3 
    // trace the different buttons
    
    
    }
   
    }
    @FXML
    public void settings_leave(Event e){
     String source=e.getSource().toString();
  
     if(source.contains("settings_btn")){
     RotateTransition rt=new RotateTransition(Duration.millis(100));
    rt.setNode(settings_btn);
    rt.setFromAngle(45);
    rt.setToAngle(0);
    rt.play();
     }
     if(source.contains("btno")||source.contains("btnpane"))
    {
        
    FadeTransition fd=new FadeTransition(Duration.millis(100));
    fd.setNode(buttonhoverpane);
    fd.setToValue(0);
    fd.setInterpolator(Interpolator.EASE_OUT);
    fd.play();
    
    
    //  turn the opacity to 0.3 
    // trace the different buttons
    }
    // turn the opacity to 0
    }
    
    
}
