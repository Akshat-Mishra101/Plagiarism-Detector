package no.fxplagiarismchecker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

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
        this.stage.setMinHeight(620);
        this.stage.setMinWidth(600);
        App.stage.getIcons().add(new Image("Images/qis.png"));
        this.stage.show();
        
    
        
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