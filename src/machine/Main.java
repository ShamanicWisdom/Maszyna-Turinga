/*
 * Lingwistyka Matematyczna Zadanie 3 - Maszyna Turinga - zwiekszanie liczby binarnej o 3.
 * Szymon Zawadzki 221515.
 */

package machine;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Szaman
 */
public class Main extends Application {
    
    Stage primaryStage = new Stage();
    
    public AnchorPane root;
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        this.primaryStage = stage;
        initiallizeMainWindow();
    }
    
    //Inicjalizacja glownego okna.
    public void initiallizeMainWindow() 
    {
        try //Proba wczytania pliku, stworzenia obiektu AnchorPane, wskazania kontrolera do FXML i ustanowienie pliku jako glowne okno aplikacji.
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("FXMLDocument.fxml"));
            root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            FXMLDocumentController controller;
            controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Maszyna Turinga");
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }
    
}
