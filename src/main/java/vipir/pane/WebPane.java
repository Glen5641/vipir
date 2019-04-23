//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Sun Feb 24 18:17:01 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190203 [weaver]:	Original file.
// 20190220 [weaver]:	Adapted from swingmvc to fxmvc.
//
//******************************************************************************
//
//******************************************************************************

package vipir.pane;

//import java.lang.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import vipir.Controller;

//******************************************************************************

/**
 * The <CODE>WebPane</CODE> class.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class WebPane extends AbstractPane
{
	//**********************************************************************
	// Private Class Members
	//**********************************************************************

	private static final String	NAME = "Web";
	private static final String	HINT = "IMDB Movie Page";

	//**********************************************************************
	// Private Class Members (Data)
	//**********************************************************************

	private static final String	URL =
		"https://www.youtube.com/watch?v=Mfmtra55s-U,_DIU2PvHs5U";

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Layout
	private StackPane				base;
	private WebView				webView;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public WebPane(Controller controller)
	{
		super(controller, NAME, HINT);

		setBase(buildPane());
	}

	//**********************************************************************
	// Public Methods (Controller)
	//**********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void	initialize()
	{
		webView.getEngine().load(URL);
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void	terminate()
	{
		webView.getEngine().load(null);
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void	update(String key, Object value)
	{
	}

	//**********************************************************************
	// Private Methods (Layout)
	//**********************************************************************

	private Pane	buildPane()
	{
		webView = new WebView();
		webView.setZoom(100.0);
		base = new StackPane(webView);

		return base;
	}
}

//******************************************************************************
