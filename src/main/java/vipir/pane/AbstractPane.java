//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Mon Feb 25 10:50:07 2019 by Chris Weaver
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
import java.util.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import vipir.Controller;
import vipir.Model;
import vipir.resources.Resources;
import javafx.scene.layout.*;

public abstract class AbstractPane {
	// **********************************************************************
	// Public Class Members
	// **********************************************************************

	public static final String RSRC = "vipir/resources/";

	public static final String SWING_ICON = RSRC + "example/swing/icon/";
	public static final String FX_ICON = RSRC + "example/fx/icon/";

	public static final String FX_TEXT = "example/fx/text/";

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Master of the program, manager of the data, mediator of all updates
	protected final Controller controller;
	protected Model model;
	protected final String name;
	protected final String hint;

	// Provided when the subclass constructor calls setBase()
	protected Node base;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	protected AbstractPane(Controller controller, Model model, String name, String hint) {
		this.model = model;
		this.controller = controller;
		this.name = name;
		this.hint = hint;
	}

	// **********************************************************************
	// Getters and Setters
	// **********************************************************************

	public AbstractPane(Controller controller2, String name2, String hint2) {
		this.controller = controller2;
		this.name = name2;
		this.hint = hint2;
	}

	public String getName() {
		return name;
	}

	public String getHint() {
		return hint;
	}

	public Node getBase() {
		return base;
	}

	// Called by the subclass constructor to provide its scene subgraph.
	protected void setBase(Node base) {
		this.base = base;
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************

	// Convenience method to put the base node into a tab. Warning: Don't
	// attempt to layout both the base node and tabs containing it! (Each
	// node can be included only once in a scene graph.)
	public Tab createTab() {
		return createFixedTab(base, name, hint);
	}

	// **********************************************************************
	// Public Methods (Controller)
	// **********************************************************************

	// The controller calls this method when it adds a view.
	// Set up the nodes in the view with data accessed through the controller.
	public void initialize() {
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void terminate() {
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void update(String key, Object value) {
	}

	// **********************************************************************
	// Public Class Methods (Resources)
	// **********************************************************************

	// Convenience method to create a node for an image located in resources
	// relative to the SWING_ICON package. See static member definitions above.
	public static ImageView createSwingIcon(String url, int size) {
		Image image = new Image(SWING_ICON + url, size, size, false, false);

		return new ImageView(image);
	}

	// Convenience method to create a node for an image located in resources
	// relative to the FX_ICON package. See static member definitions above.
	public static ImageView createFXIcon(String url, double w, double h) {
		Image image = new Image(FX_ICON + url, w, h, false, true);

		return new ImageView(image);
	}

	public static List<List<String>> loadFXData(String url) {
		ArrayList<String> lines = Resources.getLines(FX_TEXT + url);
		List<List<String>> data = new ArrayList<List<String>>();

		for (String item : lines) {
			StringTokenizer st = new StringTokenizer(item, ",");
			ArrayList<String> record = new ArrayList<String>();

			while (st.hasMoreTokens())
				record.add(st.nextToken());

			data.add(record);
		}

		return data;
	}

	// **********************************************************************
	// Public Class Methods (Layout)
	// **********************************************************************

	// Convenience method to create an unclosable tab with a tooltip.
	public static Tab createFixedTab(Node node, String title, String text) {
		Tab tab = new Tab(title, node);

		tab.setClosable(false);
		tab.setTooltip(new Tooltip(text));

		return tab;
	}

	// Convenience method to lookup nodes inside others in the scene graph.
	public static Node getDescendant(Node node, int... index) {
		for (int i = 0; i < index.length; i++) {
			if (!(node instanceof Parent))
				throw new IllegalArgumentException("Ancestor is not a Parent");

			node = ((Parent) node).getChildrenUnmodifiable().get(index[i]);
		}

		return node;
	}

	// JavaFX doesn't have a Swing-style TitledBorder, so use a TitledPane.
	// TitledPanes usually go in Accordians, but can be used independently.
	public static Pane createTitledPane(Region region, String title) {
		StackPane stackPane = new StackPane(region);

		stackPane.setId("gallery-pane");

		TitledPane titledPane = new TitledPane(title, stackPane);

		titledPane.setCollapsible(false); // Usually true, in Accordians

		return new StackPane(titledPane);
	}

	// This is the procedural version. The declarative CSS version would look
	// like, e.g. region.setStyle("-fs-background-color: white;")
	public static void setBackgroundColor(Region region, Color color) {
		BackgroundFill bf = new BackgroundFill(color, null, null);
		Background bg = new Background(bf);

		region.setBackground(bg);
	}
}

//******************************************************************************
