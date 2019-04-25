
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
//import java.lang.*;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import vipir.Controller;
import vipir.Model;
import vipir.Video;
import vipir.View;

//******************************************************************************

/**
 * The <CODE>TablePane</CODE> class.
 *
 * @author Chris Weaver
 * @version %I%, %G%
 */
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

	private static final Insets PADDING = new Insets(40.0, 20.0, 40.0, 20.0);

	// **********************************************************************
	// Private Class Members (Effects)
	// **********************************************************************

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// Data
	private List<List<String>> data;

	// Layout
	private BorderPane base;
	private StackPane lay;
	private TableView<Record> table;
	private SelectionModel<Record> smodel;

	private BorderPane queryPane;
	private BorderPane viewPane;

	private WebView webView;

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
		// smodel.selectedIndexProperty().addListener(this::changeIndex);

		// int index = (Integer) controller.get("itemIndex");

		// smodel.select(index);
	}

	// The controller calls this method when it removes a view.
	// Unregister event and property listeners for the nodes in the view.
	public void terminate() {
		// smodel.selectedIndexProperty().removeListener(this::changeIndex);
	}

	// The controller calls this method whenever something changes in the model.
	// Update the nodes in the view to reflect the change.
	public void update(String key, Object value) {
		// if ("itemIndex".equals(key)) {
		// int index = (Integer) value;

		// smodel.select(index);
		// }
	}

	// **********************************************************************
	// Private Methods (Layout)
	// **********************************************************************

	private Pane buildPane() {
		/*
		 * data = loadFXData("list-movies.txt");
		 * 
		 * // Transfer the data into an ObservableList to use as the table model
		 * ObservableList<Record> records = FXCollections.observableArrayList();
		 * 
		 * for (List<String> item : data) records.add(new Record(item.get(0),
		 * item.get(1)));
		 * 
		 * // Create a column for movie titles TableColumn<Record, String> nameColumn =
		 * new TableColumn<Record, String>("Title");
		 * 
		 * nameColumn.setEditable(false); nameColumn.setPrefWidth(250);
		 * nameColumn.setCellValueFactory(new PropertyValueFactory<Record,
		 * String>("name")); nameColumn.setCellFactory(new NameCellFactory());
		 * 
		 * // Create a column for movie posters TableColumn<Record, String> iconColumn =
		 * new TableColumn<Record, String>("Poster");
		 * 
		 * iconColumn.setEditable(false); iconColumn.setPrefWidth(W + 16.0);
		 * iconColumn.setCellValueFactory(new PropertyValueFactory<Record,
		 * String>("icon")); iconColumn.setCellFactory(new IconCellFactory());
		 * 
		 * // Create the table from the columns table = new TableView<Record>(); smodel
		 * = table.getSelectionModel();
		 * 
		 * table.setEditable(false); table.setPlaceholder(new Text("No Data!")); //
		 * table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		 * 
		 * table.getColumns().add(nameColumn); table.getColumns().add(iconColumn);
		 * 
		 * table.setItems(records);
		 * 
		 * // Create a split pane to share space between the cover pane and table
		 * SplitPane splitPane = new SplitPane();
		 * 
		 * splitPane.setOrientation(Orientation.VERTICAL);
		 * splitPane.setDividerPosition(0, 0.1); // Put divider at 50% T-to-B
		 * 
		 * Label tlabel = new Label("this space intentionally left blank"); Label llabel
		 * = new Label("put accordion here"); Label rlabel = new
		 * Label("put movieinfo here");
		 * 
		 * tlabel.setPadding(PADDING); llabel.setPadding(PADDING);
		 * rlabel.setPadding(PADDING);
		 * 
		 * splitPane.getItems().add(tlabel); splitPane.getItems().add(table);
		 * 
		 * StackPane lpane = new StackPane(llabel); StackPane rpane = new
		 * StackPane(rlabel);
		 * 
		 * // base = new BorderPane(table, null, rpane, null, lpane); base = new
		 * BorderPane(splitPane, null, rpane, null, lpane);
		 */
		buildQuery();
		buildVidView();
		lay = new StackPane(queryPane, viewPane);
		StackPane.setAlignment(queryPane, Pos.CENTER);
		StackPane.setAlignment(viewPane, Pos.CENTER);
		base = new BorderPane(null, lay, null, null, null);
		viewPane.prefWidthProperty().bind(base.widthProperty());
		queryPane.prefWidthProperty().bind(base.widthProperty());
		lay.prefWidthProperty().bind(base.widthProperty());
		viewPane.prefHeightProperty().bind(base.heightProperty());
		queryPane.setVisible(true);
		viewPane.setVisible(false);
		return base;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///TODO/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///TODO/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///TODO/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// TODO: 	Build Query Pane for Use of Mason's HBOXES or VBOXES. Simply build good title and menu based off 
	//			groupme picture and then set as (border pane where pane that holds title and menu is at top) and
	//			(Accordion "maybe" as the holder of the Array of V or H Boxes that Mason Supplies)
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void buildQuery() {
		Label title = new Label("Vipir");
		title.setFont(new Font("Arial", 100));
		title.setTextFill(Color.TEAL);
		title.setPadding(new Insets(40.0, 40.0, 40.0, 40.0));
		Button b = new Button("TO VIDEO LAYOUT");
		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				setViewURL("https://www.youtube.com/watch?v=Mfmtra55s-U,_DIU2PvHs5U");
				queryPane.setVisible(false);
				viewPane.setVisible(true);
			}
		};
		b.setOnAction(event);
		
		VBox movieCategories = new VBox();
		movieCategories.getChildren().addAll(buildMoviesBox("Action"), buildMoviesBox("Comedy"), buildMoviesBox("PewDiePie"));
		
		queryPane = new BorderPane(movieCategories, title, null, b, null);
		BorderPane.setAlignment(b, Pos.TOP_CENTER);
		queryPane.setStyle("-fx-background-color: rgb(33,33,33);");

	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///TODO/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///TODO/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///TODO/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
	// Private Methods (Change Handlers)
	// **********************************************************************

	private void changeIndex(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		int index = (Integer) newValue;

		controller.set("itemIndex", index);
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

	private static final class NameCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new NameCell();
		}
	}

	private static final class IconCellFactory
			implements Callback<TableColumn<Record, String>, TableCell<Record, String>> {
		public TableCell<Record, String> call(TableColumn<Record, String> v) {
			return new IconCell();
		}
	}

	private static final class NameCell extends TableCell<Record, String> {
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			setText(item);
			setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
		}
	}

	private static final class IconCell extends TableCell<Record, String> {
		protected void updateItem(String item, boolean empty) {
			super.updateItem(item, empty); // Prepare for setup

			if (empty || (item == null)) // Handle special cases
			{
				setText(null);
				setGraphic(null);

				return;
			}

			ImageView icon = createFXIcon(item, W, H);

			setText(null);
			setGraphic(icon);
		}
	}
	
	private VBox buildMoviesBox(String searchTerm)
	{
		ArrayList<Video> data = getMovieType(searchTerm);
		int width = 150;
		int height = 120;

		Group all = new Group();	
		for(int i = 0; i < 3; i++) 
		{
			for (Video item : data)
			{
				ImageView	icon = new ImageView(new Image(item.getPicUrl(), width, height, false, true));
				Label		label = new Label(item.getTitle(), icon);
	
				label.setTextFill(Color.WHITE);
				label.setContentDisplay(ContentDisplay.TOP);
				label.setPrefWidth(100);
				label.setStyle("-fx-background-color: rgb(33,33,33);");
	
				label.setPadding(new Insets(0, W * 0.1, 0, W * 0.1));
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
				Rectangle	shape = new Rectangle(width + 4.0, height + 24.0);
	
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

		HBox imageBox = new HBox();
		imageBox.setSpacing(10);
		imageBox.getChildren().addAll(all.getChildren());
		imageBox.setStyle("-fx-background-color: rgb(33,33,33);");

		final ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(imageBox);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHvalue(0.5);
		scrollPane.setStyle("-fx-background-color: rgb(33,33,33);");

		StackPane stackPane = new StackPane();
		AnchorPane anchorButtons = scrollButtons(scrollPane, data.size());

		anchorButtons.setPickOnBounds(false);
		stackPane.getChildren().addAll(scrollPane, anchorButtons);
		stackPane.setStyle("-fx-background-color: rgb(33,33,33);");
		stackPane.setPickOnBounds(true);
		stackPane.setPadding(new Insets(10, 10, 10, 10));

		//stackPane.setPadding(new Insets(W * 0.25, W * 0.25, W * 0.25, W * 0.1));
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
	
	private AnchorPane scrollButtons(final ScrollPane scrollPane,int dataSize)
	{
		double scrollSpeed = 1.15/(double)(dataSize*3);
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
