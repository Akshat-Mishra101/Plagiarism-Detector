package no.fxplagiarismchecker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        
        
        scene = new Scene(loadFXML("MAIN"), 1200, 640);
        //stage.setResizable(false);
        this.stage= stage;
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        this.stage.setMinHeight(600);
        this.stage.setMinWidth(600);
        
        
        
        this.stage.show();
        
       
      App.stage.heightProperty().addListener((obs, oldVal, newVal) -> {
    System.out.println(App.stage.getHeight());
});
        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}