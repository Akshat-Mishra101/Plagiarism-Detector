/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.fxplagiarismchecker;

import Engine.Properties;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 *
 * @author joey
 */
public class ReportCreator implements Initializable {
    @FXML
    Button left;
    @FXML
    Button right;
    @FXML
    StyleClassedTextArea stc;
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      System.out.println("Hello Report");
      String text = "\"hello\\n\\ssadnsa\""
              + "fsfdssdf"
              + "dsf"
              + "sdf"
              + "sf\r\n"
              + "dsf"
              + "dsf"
              + "sd";
      
      
      stc.replaceText(text);
      if(!(Properties.plagpercentage==null))
      if(Properties.plagpercentage[0].length<0)
      {
          left.setManaged(false);
          right.setManaged(false);
          left.setVisible(false);
          right.setVisible(false);
      }
      
      
      else if((Properties.plagpercentage==null))
       {
            left.setManaged(false);
          right.setManaged(false);
          left.setVisible(false);
          right.setVisible(false);
       }
      else{
      
      }
    }
    
    public static String process(){
    return "";
    }
    
    
   
    
    
    
}
