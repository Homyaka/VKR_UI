package GenDesign;

import com.vkrui.vkr_ui.MainApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GDVisualizerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("GDVisualizer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("Application");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
