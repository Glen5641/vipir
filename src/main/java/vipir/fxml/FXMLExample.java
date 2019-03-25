
package vipir.fxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class FXMLExample extends Application {
	public static void main(String[] args) {
		Application.launch(FXMLExample.class, args);
	}

	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Example.fxml"));
		stage.setTitle("FXML Example");
		stage.setScene(new Scene(root, 300, 275));
		stage.show();
	}
}