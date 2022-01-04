/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.fxplagiarismchecker;

import Engine.DataProcessor;
import Engine.Properties;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import java.io.File;
import java.net.URL;
import java.util.List;
import static java.util.Locale.filter;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 *
 * @author joey
 */
public class MainPageController implements Initializable{
    @FXML
    Button savec;
    @FXML
    Button resetd;
    
    @FXML
    MFXRadioButton partial;
    @FXML
    MFXRadioButton complete;
    
    @FXML
    TextField sphrases;
    @FXML
    TextField slines;
    @FXML
    MFXCheckbox google;
    
    @FXML
    MFXCheckbox bing;
    
    @FXML
    MFXCheckbox duck;
    
    @FXML
    TextField maxresults;
    @FXML
    Button savereport;
    @FXML
    Button previewreport;
    
    @FXML
    MFXProgressSpinner msp;
            
    @FXML
    MFXProgressSpinner bsp;
    
    @FXML
    MFXProgressSpinner csp;
    
    
    @FXML
    MFXProgressBar mpb;
    @FXML
     AnchorPane notif;
    //here
    DataProcessor dp;
    Thread rtx;
    //here
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
    @FXML
    Text MainText;
    
    
    
    @FXML
    Button bt1;
    @FXML
    Button bt2;
    
    String defaults[][]={{"proxy","maxresults","engines","plagcheck","sphrase","slines"},{"","5","G","Partial","",""}};
    
    public void onEnter(Event e)
    {
        
    System.out.println(e.getSource());
        
    String result= e.getSource().toString();
    
     ScaleTransition tt=new ScaleTransition(Duration.millis(100));
    
    tt.setToX(1.05);
    tt.setToY(1.05);
    
   
    
    if(result.contains("bt1"))
    {
        bt1.setStyle("-fx-background-color:#fff");
       tt.setNode(bt1);
    }
    else if(result.contains("bt2")){
        bt2.setStyle("-fx-background-color:#fff");
    tt.setNode(bt2);
    }
    else if(result.contains("Save Changes"))
    {
       savec.setStyle("-fx-background-color:#fff");
       tt.setNode(savec);
    }
    else if(result.contains("Reset Defaults")){
    resetd.setStyle("-fx-background-color:#fff");
    tt.setNode(resetd);
    }
    tt.play();
    
   
    
   // ft.setNode(notif);
    }
    public void onExit(Event e){
      
       System.out.println(e.getSource());
        
    String result= e.getSource().toString();
    //FadeTransition ft = new FadeTransition(Duration.millis(500));
     ScaleTransition tt=new ScaleTransition(Duration.millis(100));
    
    tt.setToX(1);
    tt.setToY(1);
    
   
    
    if(result.contains("bt1"))
    {
        bt1.setStyle("-fx-background-color:#f1f1f1");
       tt.setNode(bt1);
    }
    else if(result.contains("bt2")){
        bt2.setStyle("-fx-background-color:#f1f1f1");
    tt.setNode(bt2);
    }
    else if(result.contains("Save Changes"))
    {
       savec.setStyle("-fx-background-color:#f1f1f1");
       tt.setNode(savec);
    }
    else if(result.contains("Reset Defaults")){
    resetd.setStyle("-fx-background-color:#f1f1f1");
       tt.setNode(resetd);
    }
    
    
    
    tt.play();
    
        
    }
    
    
    
    
    @FXML
    public void close_process()
    {
        FadeTransition fd= new FadeTransition();
        fd.setNode(mpb);
        fd.setFromValue(1);
        fd.setToValue(0);
        
        fd.setDelay(Duration.millis(200));
        
        
        
        FadeTransition fca= new FadeTransition();
        fca.setNode(notif);
        fca.setFromValue(1);
        fca.setToValue(0);
        
        fca.setDelay(Duration.millis(200));
        
        
        
        ParallelTransition pt=new ParallelTransition(fd,fca);
        pt.play();
        
        try{
       rtx.stop();
        }
        catch(Exception e){}
    }
    @FXML
     AnchorPane wind;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      buttonhoverpane.setOpacity(0);
//      timeout.setStyle("-fx-background-color: -fx-control-inner-background;");
    //  settings_pane.setOpacity(0);
      t1.setOpacity(0);
      t2.setOpacity(0);
      t3.setOpacity(0);
      notif.setOpacity(0);
      
      websearch.setOpacity(0);
      documentalsearch.setOpacity(0);
       reportcreation.setOpacity(0);
      Properties.loadFiles();
       load();
      
      
    
    }
    public void load()
    {
    
       google.setSelected(false);
       bing.setSelected(false);
       duck.setSelected(false);
        API.setText(Properties.getValue(defaults[0][0]));
        maxresults.setText(Properties.getValue(defaults[0][1]));
        
        
        String values[] = Properties.getValue(defaults[0][2]).split(",");
        for(String value:values){
            if(value.equals("G"))
                google.setSelected(true);
            else if(value.equals("B"))
                bing.setSelected(true);
            else if(value.equals("D"))
                duck.setSelected(true);
                
        
        }
        
        sphrases.setText(Properties.getValue(defaults[0][4]));
        slines.setText(Properties.getValue(defaults[0][5]));
        
        
        if(Properties.getValue(defaults[0][3]).equals("Partial"))
        {
        partial.setSelected(true);
        }
        else if(Properties.getValue(defaults[0][3]).equals("Complete"))
        {
        complete.setSelected(true);
        }
        
    }
    
    @FXML
    public void resetDefaults() throws Exception
    {
       
        for(int i = 0;i<6;i++)
        {
        Properties.Update(defaults[0][i], defaults[1][i]);
        }
        Properties.Save();
        load();
        
    }
    
    @FXML
    public void Savechanges() throws Exception
    {  String engines="";
        if(google.isSelected())
        {
        engines+="G,";
        }
        if(bing.isSelected())
        {
        engines+="B,";
        }
        if(duck.isSelected())
        {
        engines+="D,";
        }
        
        String updatesValues[][]={{"proxy","maxresults","engines","plagcheck","sphrase","slines"},{API.getText(),maxresults.getText(),engines,partial.isSelected()?"Partial":"Complete",sphrases.getText(),slines.getText()}};;
        for(int i = 0;i<6;i++)
        {
        Properties.Update(updatesValues[0][i], updatesValues[1][i]);
        }
        Properties.Save();
        
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
       
    }
    boolean hasentered=false;
    
    
    @FXML
    AnchorPane websearch;
    @FXML
    AnchorPane documentalsearch;
    @FXML
    AnchorPane reportcreation;
    
    @FXML
    public void clicked_menu(Event e)
    {   
      App.stage.widthProperty().addListener((obs, oldVal, newVal) -> {
    float width = (float) App.stage.getWidth();
    
    if(width<1120)
    {
       
      
    notif.setManaged(false);
      notif.setVisible(false);
      
    }
    else{
        
             

        notif.setManaged(true);
         notif.setVisible(true);

    }
});
      
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
      String source =  e.getSource().toString();
     
      
      String ben="";
      if(source.contains("internet_search_btno"))
      {
         source = "internet_search_btno";
         ben="websearch";
      }
      else if(source.contains("compare_docs_btno"))
      {
           source = "compare_docs_btno";
           ben="documental";
      }
      
      else{
          
          ben="all";
        source  = "search_several_articles_btno";
      }
     
      
       
      
      
      
      //Web Search 
      
      
      //Search Among Documents
      
      //Search Between The Documents And On The Internet
       
    
       
     if((list_of_files=fc.showOpenMultipleDialog(settings_btn.getScene().getWindow()))!=null)
     {
  
        FadeTransition fd= new FadeTransition();
        fd.setNode(mpb);
        fd.setFromValue(0);
        fd.setToValue(1);
        
        fd.setDelay(Duration.millis(200));
        
        
        
        FadeTransition fca= new FadeTransition();
        fca.setNode(notif);
        fca.setFromValue(0);
        fca.setToValue(1);
        
        fca.setDelay(Duration.millis(200));
        
        
        
        ParallelTransition pt=new ParallelTransition(fd,fca);
        pt.play();
        
        
        
        
        
        
         
        dp = new DataProcessor(list_of_files, ben,websearch,documentalsearch,reportcreation,msp,bsp,csp);
        
        mpb.progressProperty().bind(dp.progressProperty());
        MainText.textProperty().bind(dp.messageProperty());
        
        
        
        
        
       rtx = new Thread(dp);
        rtx.setDaemon(true);
       pt.setOnFinished(event->{rtx.start();});
       
        

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
        tt.setToX(335);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.play();
        
       
       
       
    
    }
    
    @FXML 
    public void close_button()
    {
     
        TranslateTransition tt=new TranslateTransition(Duration.millis(300));
        tt.setNode(settings_pane);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setToX(-331);
        tt.play();
        
       
    }
    @FXML
    public void buttonAnimations(Event E)
    {
    
    String source=E.getSource().toString();
   
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
