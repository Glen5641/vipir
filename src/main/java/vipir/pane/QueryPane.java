
//******************************************************************************
//Copyright (C) 2019 University of Oklahoma Board of Trustees.

//******************************************************************************
//Last modified: Sun Feb 24 19:56:48 2019 by Chris Weaver
//******************************************************************************
//Major Modification History:
//
//20190203 [weaver]:	Original file.
//20190220 [weaver]:	Adapted from swingmvc to fxmvc.
//
//******************************************************************************
//
//******************************************************************************

package vipir.pane;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import vipir.Controller;
import vipir.Model;
import vipir.Video;

public final class QueryPane extends AbstractPane {
	// **********************************************************************
	// Private Class Members
	// **********************************************************************

	private static final String NAME = "Vipir";
	private static final String HINT = "Table Layout for Images";

	// **********************************************************************
	// Private Class Members (Layout)
	// **********************************************************************

	private static final double W = 32; // Item icon width
	private static final double H = W * 1.5; // Item icon height

	// **********************************************************************
	// Private Class Members (Effects)
	// **********************************************************************

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Layout
	private BorderPane base;
	private FlowPane lay;
	private BorderPane queryPane;
	private BorderPane viewPane;

	private WebView webView;

	private boolean second = false;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public QueryPane(Controller controller, Model model) {
		super(controller, model, NAME, HINT);

		setBase(buildPane());
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
	// Private Methods (Layout)
	// **********************************************************************

	private Pane buildPane() {
		buildQuery();
		buildVidView();
		lay = new FlowPane(queryPane, viewPane);
		lay.setStyle("-fx-background-color: rgb(33,33,33);");
		ScrollPane scroll = new ScrollPane(lay);
		scroll.setFitToWidth(true);
		scroll.setFitToHeight(true);
	    base = new BorderPane(scroll, null, null, null, null);

		viewPane.prefWidthProperty().bind(base.widthProperty());
		queryPane.prefWidthProperty().bind(base.widthProperty());
		lay.prefWidthProperty().bind(base.widthProperty());
		viewPane.prefHeightProperty().bind(base.heightProperty());
		queryPane.setVisible(true);
		viewPane.setVisible(false);
		return base;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO: 	Build Query Pane for Use of Mason's HBOXES or VBOXES. Simply build good title and menu based off
	//			groupme picture and then set as (border pane where pane that holds title and menu is at top) and
	//			(Accordion "maybe" as the holder of the Array of V or H Boxes that Mason Supplies)
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void buildQuery() {


		final TextField searchField = new TextField();
		searchField.setPromptText("Search movies here."); //search <genre> movies here.
		searchField.setAlignment(Pos.BOTTOM_RIGHT);
		searchField.setPrefWidth(200);
		searchField.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

		//add combo box to searchBox that can add and remove the default categories
		final ComboBox cb = new ComboBox();
		cb.setPromptText("All");
		cb.getItems().add("Comedy");
		cb.getItems().add("Scifi");
		cb.getItems().add("Action");
		cb.getItems().add("Horror");
		cb.getItems().add("Anime");
		cb.setPrefWidth(150);

		//Box to pass in text to be searched
		HBox searchBox = new HBox();
		searchBox.setStyle("-fx-background-color: rgb(0,128,128);");
		searchBox.setAlignment(Pos.BOTTOM_RIGHT);
		searchBox.setPadding(new Insets(10.0, 20.0, 10.0, 10.0));
		searchBox.getChildren().addAll(cb, searchField);
		searchBox.setSpacing(20);
       // searchBox.getChildren().add(cb);

		HBox.setHgrow(searchBox, Priority.ALWAYS);

		final VBox movieCategories = new VBox();
		movieCategories.getChildren().addAll(buildMoviesBox("Action"), buildMoviesBox("Comedy"));
		movieCategories.setStyle("-fx-background-color: rgb(33,33,33);");

		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String text = searchField.getText();
				movieCategories.getChildren().add(0, buildMoviesBox(text));
				searchField.clear();

			}
		};

		EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String genre = (String) cb.getValue();
				boolean add = true;
				for(int i = 0; i < movieCategories.getChildren().size(); i++) {
					String title = ((Label)((VBox)(movieCategories.getChildren().get(i))).getChildren().get(0)).getText();
					if(genre.equals(title)) {
						movieCategories.getChildren().remove(i);
						i--;
						add = false;
					}
				}
				if(add) {
					movieCategories.getChildren().add(buildMoviesBox(genre));
				}
			}

		};

		cb.setOnAction(event2);
		searchField.setOnAction(event);

		Label title = new Label("Vipir");
		title.setFont(new Font("Arial", 50));
		title.setStyle("-fx-text-fill: rgb(33,33,33);");
		title.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
		title.setAlignment(Pos.CENTER_LEFT);

		HBox titleBar = new HBox();
		titleBar.getChildren().addAll(title, searchBox);
		titleBar.setStyle("-fx-background-color: rgb(0,128,128);");

		queryPane = new BorderPane(movieCategories, titleBar, null, null, null);
		queryPane.setStyle("-fx-background-color: rgb(33,33,33);");
	}

	private void buildVidView() {
		webView = new WebView();
		Button b = new Button("Back To HOME LAYOUT");
		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				setViewURL(null);
				viewPane.setVisible(false);
				queryPane.setVisible(true);
			}
		};
		b.setOnAction(event);
		VBox box = new VBox(b);
		viewPane = new BorderPane(webView, box, null, null, null);
		box.prefWidthProperty().bind(viewPane.widthProperty());
		box.prefHeightProperty().bind(viewPane.heightProperty().multiply(0.05));
		box.setAlignment(Pos.TOP_CENTER);
	}

	public void setViewURL(String url) {
		webView.getEngine().load(url);
		webView.getEngine().setUserStyleSheetLocation("file:src/main/java/vipir/webContent.css");
		webView.setZoom(2.216);

	}

	public void removeViewURL() {
		webView.getEngine().load(null);
	}

	// **********************************************************************
	// Inner Classes (Record Object)
	// **********************************************************************

	public static final class Record {
		private final SimpleStringProperty name;
		private final SimpleStringProperty icon;

		public Record(String name, String icon) {
			this.name = new SimpleStringProperty(name);
			this.icon = new SimpleStringProperty(icon);
		}

		public String getName() {
			return name.get();
		}

		public void setName(String v) {
			this.name.set(v);
		}

		public String getIcon() {
			return icon.get();
		}

		public void setIcon(String v) {
			this.icon.set(v);
		}
	}

	// **********************************************************************
	// Inner Classes (Cell Factories)
	// **********************************************************************

	// Builds a VBox with results from the search term, if it is one of the preset categories from the model we use those instead of searching.
	private VBox buildMoviesBox(String searchTerm)
	{

		//get the data
		ArrayList<Video> data = getMovieType(searchTerm);
		int width = 150;
		int height = 120;

		//Generate the thumbnails and text and add listeners to link to videos
		//This loops three times for reasons
		Group all = new Group();
		for(int i = 0; i < 3; i++)
		{
			for (final Video item : data)
			{

				//Image and text
				ImageView	icon = new ImageView(new Image(item.getPicUrl(), width, height, false, true));
				Label		label = new Label(item.getTitle(), icon);

				label.setTextFill(Color.WHITE);
				label.setContentDisplay(ContentDisplay.TOP);
				label.setPrefWidth(100);
				label.setStyle("-fx-background-color: rgb(33,33,33);");
				label.setPadding(new Insets(0, W * 0.1, 0, W * 0.1));

				//Adds the click event to open the video
				label.addEventHandler(MouseEvent.MOUSE_CLICKED,
						new EventHandler<MouseEvent>() {
						    @Override
						    public void handle(MouseEvent event) {
								setViewURL(item.getVideoUrl());
								queryPane.setVisible(false);
								viewPane.setVisible(true);
						    }
				});

				// Add a rounded rectangular border around each item
				Rectangle	shape = new Rectangle(width, height);

				shape.setArcWidth(4.0);
				shape.setArcHeight(4.0);

				shape.setFill(Color.BLACK);

				shape.setStroke(Color.BLACK);
				shape.setStrokeType(StrokeType.OUTSIDE);
				shape.setStrokeWidth(2.0);
				shape.setStyle("-fx-background-color: rgb(33,33,33);");

				// Put the border and label together, and add it to the set of items
				StackPane one = new StackPane(shape, label);
				one.setStyle("-fx-background-color: rgb(33,33,33);");

				all.getChildren().add(one);
			}
		}

		//Box to store the movie entries
		HBox imageBox = new HBox();
		imageBox.setSpacing(10);
		imageBox.getChildren().addAll(all.getChildren());
		imageBox.setStyle("-fx-background-color: rgb(33,33,33);");


		//Scroll pane to scroll left and right on the entries
		final ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(imageBox);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHvalue(0.5);
		scrollPane.setStyle("-fx-background-color: rgb(33,33,33);");
		scrollPane.setFitToWidth(true);


		//Stack pane to overlay the anchor pane(the arrows)
		StackPane stackPane = new StackPane();
		AnchorPane anchorButtons = scrollButtons(scrollPane, data.size());

		anchorButtons.setPickOnBounds(false);
		stackPane.getChildren().addAll(scrollPane, anchorButtons);
		stackPane.setStyle("-fx-background-color: rgb(33,33,33);");
		stackPane.setPickOnBounds(true);
		stackPane.setPadding(new Insets(10, 10, 10, 10));

		//VBox to add the category title and the movies
		VBox movies = new VBox();
		Label category = new Label(searchTerm);
		category.setTextFill(Color.TEAL);
		category.setFont(new Font("Arial", 24));
		movies.getChildren().addAll(category, stackPane);
		movies.setAlignment(Pos.CENTER_LEFT);
		movies.setSpacing(10);
		movies.setStyle("-fx-background-color: rgb(33,33,33); -fx-border-color: TEAL;");
		movies.setPadding(new Insets(10, 10, 10, 10));
		VBox.setMargin(movies, new Insets(10, 10, 10, 10));

		return movies;
	}

	//Anchor pane for the horizontal scrolling arrows
	private AnchorPane scrollButtons(final ScrollPane scrollPane,final int dataSize)
	{
		final double scrollSpeed = 1.15/(double)(dataSize*3);
		Button rightArrow = new Button();
		rightArrow.setGraphic(createFXIcon("arrowright.png", W, H));
		rightArrow.setStyle("-fx-focus-color: transparent; -fx-background-color: transparent;");
		rightArrow.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					System.out.println(scrollPane.getHvalue());
					if(scrollPane.getHvalue() >= 0.98)
					{
						scrollPane.setHvalue(scrollPane.getHvalue() - (scrollSpeed*((dataSize-1))));
					}
					scrollPane.setHvalue(scrollPane.getHvalue() + scrollSpeed);

				}
		});

		Button leftArrow = new Button();
		leftArrow.setGraphic(createFXIcon("arrowleft.png", W, H));
		leftArrow.setStyle("-fx-focus-color: transparent; -fx-background-color: transparent;");
		leftArrow.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					if(scrollPane.getHvalue() <= 0.02)
					{
						scrollPane.setHvalue(scrollPane.getHvalue() + (scrollSpeed*((dataSize-1))));
					}
					scrollPane.setHvalue(scrollPane.getHvalue() - scrollSpeed);
				}
		});

		AnchorPane anchorPane = new AnchorPane();
		AnchorPane.setLeftAnchor(leftArrow, 1.0);
		AnchorPane.setRightAnchor(rightArrow, 1.0);
		anchorPane.getChildren().addAll(leftArrow, rightArrow);
		return anchorPane;

	}

	//method to deal with picking categories or searching if none were selected
	private ArrayList<Video> getMovieType(String movieType)
	{
		if(movieType.equals("Action"))
			return model.getAction();
		else if (movieType.equals("Romance"))
			return model.getRomance();
		else if (movieType.equals("Anime"))
			return model.getAnime();
		else if (movieType.equals("Comedy"))
			return model.getComedy();
		else if (movieType.equals("Documentary"))
			return model.getDocumentary();
		else if (movieType.equals("Drama"))
			return model.getDrama();
		else if (movieType.equals("Fantasy"))
			return model.getFantasy();
		else if (movieType.equals("Games"))
			return model.getGames();
		else if (movieType.equals("Horror"))
			return model.getHorror();
		else if (movieType.equals("Scifi"))
			return model.getScifi();
		else
		{
			model.setSearch(movieType);
			return model.getVideosList();
		}
	}
}

//******************************************************************************
