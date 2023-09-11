package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent fxmlLoader = FXMLLoader.load(MainApp.class.getResource("SistemaRedeEletrica.fxml"));
        Scene scene = new Scene(fxmlLoader);
        stage.setTitle("Sistema de Rede Eletrica da UFRPE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

