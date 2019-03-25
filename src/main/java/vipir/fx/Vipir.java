package vipir.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public final class Vipir extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		String remoteUrl = "https://i.ytimg.com/vi/-i0OIy5zSrs/default.jpg";
		ImageView view = new ImageView(remoteUrl);
		view.setFitHeight(300);
		view.setFitWidth(300);
		StackPane root = new StackPane();
		root.getChildren().add(view);
		Scene scene = new Scene(root, 300, 300);
		stage.setTitle("Hello World...");
		stage.setScene(scene);
		stage.show();
	}
}
