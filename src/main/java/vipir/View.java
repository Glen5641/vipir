
package vipir;

import java.util.ArrayList;
import java.net.URL;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.*;

public final class View
{
	private final Controller				controller;
	private final WindowHandler			windowHandler;
	private final ArrayList<AbstractPane>	panes;

	public View(Controller controller, String name, double x, double y)
	{
		this.controller = controller;
		windowHandler = new WindowHandler();
		panes = new ArrayList<AbstractPane>();
		panes.add(new TablePane(controller));
		panes.add(new MediaPane(controller));
		panes.add(new CyclePane(controller));
		panes.add(new FloatPane(controller));
		panes.add(new ImagePane(controller));
		panes.add(new WebPane(controller));
		panes.add(new GalleryPane(controller));

		TabPane	tabPane = new TabPane();

		for (AbstractPane pane : panes)
			tabPane.getTabs().add(pane.createTab());

		Scene		scene = new Scene(tabPane, 800, 600);
		URL		url = View.class.getResource("View.css");
		String		surl = url.toExternalForm();

		scene.getStylesheets().add(surl);

		Stage		stage = new Stage();

		stage.setOnHiding(windowHandler);
		stage.setScene(scene);
		stage.setTitle(name);
		stage.setX(x);
		stage.setY(y);
		stage.show();
	}

	//**********************************************************************
	// Public Methods (Controller)
	//**********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void	initialize()
	{
		for (AbstractPane pane : panes)
			pane.initialize();
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void	terminate()
	{
		for (AbstractPane pane : panes)
			pane.terminate();
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void	update(String key, Object value)
	{
		for (AbstractPane pane : panes)
			pane.update(key, value);
	}

	//**********************************************************************
	// Inner Classes (Event Handlers)
	//**********************************************************************

	private final class WindowHandler
		implements EventHandler<WindowEvent>
	{
		public void	handle(WindowEvent e)
		{
			if (e.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST)
				controller.removeView(View.this);
		}
	}
}

//******************************************************************************
