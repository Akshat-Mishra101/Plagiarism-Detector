/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.fxplagiarismchecker;

import Engine.PLBOT;
import Engine.Properties;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author joey
 */
public class Starter {
    
    public static void main(String args[]) throws Exception
    {
        
        
       // Document doc=Jsoup.connect("https://www.google.com/search?q=\"A+progressive+industry+such+as+IT+shouldn't+be+held+back+by+paperwork\"").get();
        if(!new File(".data").exists())
        {
        String defaults[][]={{"proxy","maxresults","engines","plagcheck","sphrase","slines"},{"","5","G","Partial","",""}};
        for(int i = 0;i<6;i++)
        {
        Properties.Update(defaults[0][i], defaults[1][i]);
        }
        Properties.Save();
        
        System.out.println("Running For The First Time");
        
        for(int i = 0;i<6;i++)
        {
        
        System.out.println(Properties.getValue(defaults[0][i])+"_");
        }
        
        }
        
        
        
        /**
        try {
            PDDocument document = PDDocument.load(new File("C:\\Users\\joey\\Downloads\\Raj Mishra (cse026) practicals.pdf"));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int page=5;
            
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            
            // Create a temp image file
            
            File tempFile = new File("C:\\Users\\joey\\Desktop\\tempfile_1.png");
            ImageIO.write(bufferedImage, "png", tempFile);
            
            ITesseract _tesseract = new Tesseract();
	  _tesseract.setDatapath("tessdata");
	  _tesseract.setLanguage("eng"); // choose your language

            String result = _tesseract.doOCR(tempFile); 
            System.out.println("hello world");
              System.out.println(result);
              
        } catch (Exception ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
        * **/
       App.main(args);
      //Properties.loadFiles();
     // boolean result = PLBOT.search("As software development evolved, a group of independent thinkers in software development realized that all those traditional development methods just weren’t enough in a world full of innovative technology that requires original thinking", 15000);
      //System.out.println(result);
      
      //yuRUbf class
      /**
      Elements el = Jsoup.parse(new File("C:\\Users\\joey\\Downloads\\'Yes, speed matters. But we can’t focus too much on speed—otherwise there’s no time for reflection, and reflection is critical for learning.' - Google Search.html"), "UTF-8").getElementsByClass("yuRUbf");
      for(Element e: el){
          
         Document c = Jsoup.parse(e.html());
         c.getElementsByTag("a");
          System.out.println(e.getElementsByTag("a").attr("href")+"\r\n___________________________________\r\n");
      }
      * 
      *  
      Properties.loadFiles();
      boolean result = PLBOT.search("By 1828, Sequoyah was at the peak of his renown. His syllabary had been accepted by the Cherokee National Council at its national capital in New Echota, Georgia, in 1825.", 25000);
      System.out.println(result);
      List<String> arr = PLBOT.getSources();
      for(String a: arr)
          System.out.println(a);
       */
      
      
      
    }
}
