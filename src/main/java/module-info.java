/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module FXPlagiarismChecker {
    requires java.base;
    requires java.desktop;
    requires poi;
    
    requires org.jsoup;
    requires MaterialFX;
    requires javafx.controls;
    requires javafx.graphics;
    
    requires javafx.fxml;
    requires javafx.base;
    
    
    requires org.fxmisc.richtext;
    requires org.fxmisc.undo;
    

    requires org.apache.pdfbox;
    

    opens no.fxplagiarismchecker to javafx.fxml;
    exports no.fxplagiarismchecker;
}
