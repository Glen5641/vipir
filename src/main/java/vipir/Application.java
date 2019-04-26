
package vipir;

import javafx.animation.*;

import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class Application extends javafx.application.Application {
	// **********************************************************************
	// Main
	// **********************************************************************
	 
	public static void main(String[] args) {
		javafx.application.Application.launch(args);
	}

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Master of the program, manager of the data, mediator of all updates
	private Controller controller;

	// Where the data lives; only one place.
	private Model model;

	// **********************************************************************
	// Override Methods (Application)
	// **********************************************************************

	public void init() {
		controller = new Controller();
		model = new Model(controller);
		controller.setModel(model);
	}

	public void start(Stage stage) {
		Text text = new Text("Vipir");
		text.setFill(Color.TEAL);
		
		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: rgb(33, 33, 33)");
		
		root.getChildren().add(text);

		Scene scene = new Scene(root, 800, 600);
		
		ProgressIndicator pb = new ProgressIndicator();
		pb.setStyle(" -fx-progress-color: aquamarine;");		
		
		root.getChildren().add(pb);
	
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
		
		// Animation: scale text to fill stage over 1.0 seconds
		Duration sd = new Duration(500);
		ScaleTransition st = new ScaleTransition(sd, text);
		st.setFromX(0.01);
		st.setFromY(0.01);
		st.setToX(5.0);
		st.setToY(5.0);

		st.play();

		// Animation: Hide stage after 1.5 seconds
		Timeline timeline = new Timeline();
		Duration duration = new Duration(7500);
		KeyFrame endframe = new KeyFrame(duration, new EndSplash(stage));

		timeline.getKeyFrames().add(endframe);
		timeline.play();
	}

	public void stop() throws Exception {
	}

	// **********************************************************************
	// Override Methods (EventHandler<ActionEvent>)
	// **********************************************************************

	public final class EndSplash implements EventHandler<ActionEvent> {
		private final Stage stage;

		public EndSplash(Stage stage) {
			this.stage = stage;
		}

		public void handle(ActionEvent e) {
			View view1 = new View(controller, model, "View 1", 40, 40);
			controller.addView(view1);
			stage.hide();
		}
	}
}

//******************************************************************************
