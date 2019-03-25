
package vipir.fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class FXMLController {
	@FXML
	private Text target;

	@FXML
	protected void handleAction(ActionEvent event) {
		target.setFill(Color.YELLOW);
		target.setText("Button pressed");
	}
}