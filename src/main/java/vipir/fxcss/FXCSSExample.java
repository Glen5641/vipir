
package vipir.fxcss;

import java.net.URL;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class FXCSSExample extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	private static TextField userField;
	private static Text text;

	public void start(Stage stage) throws Exception {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Text scenetitle = new Text("Welcome");
		scenetitle.setId("welcome-text");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		Label userName = new Label("Your Name:");
		Label message = new Label("Message:");
		userField = new TextField();
		PasswordField messageField = new PasswordField();
		grid.add(scenetitle, 0, 0, 2, 1);
		grid.add(userName, 0, 1);
		grid.add(userField, 1, 1);
		grid.add(message, 0, 2);
		grid.add(messageField, 1, 2);
		Button button = new Button("Send");
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(button);
		grid.add(hbox, 1, 4);
		text = new Text();
		text.setId("text-target");
		grid.add(text, 1, 6);
		ActionHandler handler = new ActionHandler();
		button.setOnAction(handler);
		Scene scene = new Scene(grid, 350, 275);
		URL url = FXCSSExample.class.getResource("Example.css");
		String surl = url.toExternalForm();
		scene.getStylesheets().add(surl);
		stage.setTitle("FXCSS Example");
		stage.setScene(scene);
		stage.show();
	}

	public static final class ActionHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			String name = userField.getText();
			text.setFill(Color.YELLOW);
			text.setText("Thanks " + name + ", received.");
		}
	}
}