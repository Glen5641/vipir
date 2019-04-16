
package vipir;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.ThumbnailDetails;

import javafx.application.Platform;

/**
 * Model of the HCI Project GUI - This is the model portion of the MVC that
 * encapsulates the YouTube Structure and breaks the structure down as a simple
 * readable video object.
 * 
 * @author glen5641
 * @author https://developers.google.com/youtube/v3/code_samples/
 */
public final class Model {

	// Variables for opening the YouTube Object via API (Do not Change These)
	private static final String APPLICATION_NAME = "API Sample";
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/youtube-java-quickstart");
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
	private static final String PROPERTIES_FILENAME = "youtube.properties";
	private static final List<String> SCOPES = Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);
	private static YouTube.Search.List search;

	// Changeable Variable for Videos Returned
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

	// Actual YouTube Object
	private static YouTube youtube = null;

	// Controller in MVC
	private final Controller controller;

	// Properties of the Example MVC for changing
	private final HashMap<String, Object> properties;

	// Structures for Caching Most Recent Search
	private ArrayList<Video> videos = null;
	private HashMap<String, Video> videoMap = null;
	private String query = "";

	/**
	 * MODEL CONSTRUCTOR - This is the model portion of the MVC that encapsulates
	 * the YouTube Structure and breaks the structure down as a simple readable
	 * video object.
	 * 
	 * @param controller
	 */
	public Model(Controller controller) {

		// Set Controller
		this.controller = controller;

		// Set Properties
		properties = new HashMap<String, Object>();
		properties.put("lions", Boolean.FALSE);
		properties.put("tigers", Boolean.TRUE);
		properties.put("bears", Boolean.FALSE);
		properties.put("surprised", Boolean.FALSE);
		properties.put("flavor", "chocolate");
		properties.put("decimal", 5.0);
		properties.put("integer", 5);
		properties.put("comment", "I am buggy. Fix me!");
		properties.put("string", "123abc");
		properties.put("item", "Naptime,bed.png");
		properties.put("tool", "Book,bool.png");
		properties.put("itemIndex", 0);

		// Init Videos List and Map
		videos = new ArrayList<Video>();
		videoMap = new HashMap<String, Video>();

		// Init YouTube Object
		try {
			youtube = getYouTubeService();
		} catch (IOException e1) {
			System.err.println("Could not initiallize youtube.");
			System.exit(0);
		}
	}

	/**
	 * ACCESSOR METHOD - Returns the Value in Properties Associated with the Key.
	 * 
	 * @param key (Identifier of Object)
	 * @return value (Actual Object)
	 */
	public Object getValue(String key) {

		// Return the Property associated with the key
		return properties.get(key);
	}

	/**
	 * MUTATOR METHOD - Add New Object with Associated Key in Properties Map
	 * 
	 * @param key   (Identifier of Object)
	 * @param value (Actual Object)
	 */
	public void setValue(String key, Object value) {

		// If properties contains object, just return
		if (properties.containsKey(key) && properties.get(key).equals(value)) {
			System.out.println(" model: value not changed");
			return;
		}

		// Else Add Object
		Platform.runLater(new Updater(key, value));
	}

	/**
	 * TRIGGER METHOD - Method is accessed when model is not changed
	 * 
	 * @param name (Name of the object hitting the Method)
	 */
	public void trigger(String name) {

		// Print What is not functioning
		System.out.println(" model: (not!) calculating function: " + name);
	}

	/**
	 * Mutator Method - Update the Properties and Notify the Controller of Changes.
	 */
	private class Updater implements Runnable {

		// Variables for object
		private final String key;
		private final Object value;

		// Construct Object
		public Updater(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		// Update the properties and notify the Controller
		public void run() {
			properties.put(key, value);
			controller.update(key, value);
		}
	}

	/**
	 * MUTATOR METHOD - Set database model with a query string. This updates the
	 * cache with the newest search updates and builds the database.
	 * 
	 * @param queryTerm (Term used to search YouTube)
	 * @author https://developers.google.com/youtube/v3/code_samples/
	 */
	public void setSearch(String queryTerm) {

		// Try to Query YouTube Object
		try {

			// Access Channels List and Set User Name
			YouTube.Channels.List channelsListByUsernameRequest = youtube.channels()
					.list("snippet,contentDetails,statistics");
			channelsListByUsernameRequest.setForUsername("GoogleDevelopers");

			// Execute the Access
			ChannelListResponse response = channelsListByUsernameRequest.execute();

			// Get Google Dev Channel
			Channel channel = response.getItems().get(0);

			// Print the Channel to Ensure Connection
			System.out.printf("This channel's ID is %s. Its title is '%s', and it has %s views.\n", channel.getId(),
					channel.getSnippet().getTitle(), channel.getStatistics().getViewCount());

			// Create Local Properties for YouTube Object
			Properties properties = new Properties();

			// Load YouTube Properties in
			try {
				InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
				properties.load(in);

			} catch (IOException e) {

				// Throw Error if Properties is not there and exit
				System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : "
						+ e.getMessage());
				System.exit(1);
			}

			// Create Search Object for Querying
			search = youtube.search().list("id, snippet");

			// Set API KEY for Access
			String apiKey = properties.getProperty("youtube.apikey");
			search.setKey(apiKey);

			// If Query is empty, Search Live Videos
			if (queryTerm.length() < 1) {
				queryTerm = "YouTube Developers Live";
			}

			// Set Query and only Allow Videos
			search.setQ(queryTerm);
			this.query = queryTerm;
			search.setType("video");

			// Grab Certain Elements of JSON Search and Constrain
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

			// Execute Search
			SearchListResponse searchResponse = search.execute();

			// Get List from Search and Iterate Through Items
			List<SearchResult> searchList = searchResponse.getItems();
			Iterator<SearchResult> iteratorSearchResults = searchList.iterator();
			while (iteratorSearchResults.hasNext()) {

				// Grab Single Video and ID
				SearchResult singleVideo = iteratorSearchResults.next();
				ResourceId rId = singleVideo.getId();

				// If Video is Actually Video
				if (rId.getKind().equals("youtube#video")) {

					// Get Pic from Video
					ThumbnailDetails thumbnail = singleVideo.getSnippet().getThumbnails();

					// Create Video Object With Results
					Video video = new Video(rId.getVideoId(), singleVideo.getSnippet().getTitle(),
							thumbnail.getDefault().getUrl());

					// Add the Video to the List and the Map
					videos.add(video);
					videoMap.put(rId.getVideoId(), video);
				}
			}

			// If Search List is null, Everything should be null
			if (searchList != null) {
			}
		} catch (GoogleJsonResponseException e) {
			e.printStackTrace();
			System.err.println(
					"There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns the Initial Query that is Cached
	 * 
	 * @return query (String)
	 */
	public final String getQuery() {

		// Return the Query
		return this.query;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns the Videos as an ArrayList
	 * 
	 * @return Videos List (ArrayList<Video)
	 */
	public final ArrayList<Video> getVideosList() {

		// Return the Videos List
		return this.videos;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns the Videos as a HashMap with Key ID and
	 * Value Video
	 * 
	 * @return Videos Map (HashMap<String, Video>)
	 */
	public final HashMap<String, Video> getVideosMap() {

		// Return the Hashmap of Videos
		return this.videoMap;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns the Videos as a Primitive Array
	 * 
	 * @return Videos Array (Video[])
	 */
	public final Video[] getVideosAsArray() {

		Video[] vids = new Video[videos.size()];
		for (int i = 0; i < videos.size(); i++) {
			vids[i] = videos.get(i);
		}
		return vids;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns an ArrayList of Video IDs
	 * 
	 * @return ID List (ArrayList<String>)
	 */
	public final ArrayList<String> getVideoIDs() {

		// Return the ArrayList of Video IDs
		ArrayList<String> ids = new ArrayList<String>();
		for (int i = 0; i < videos.size(); i++) {
			ids.add(videos.get(i).getID());
		}
		return ids;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns the Video Associated with the ID
	 * 
	 * @param ID (Identifying String)
	 * @return Associated Video (Video)
	 */
	public final Video getVideoByID(String ID) {

		// Return a video by ID
		return videoMap.get(ID);
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns an ArrayList of Video Titles
	 * 
	 * @return Title List (<ArrayList<String>)
	 */
	public final ArrayList<String> getVideoTitles() {

		// Return the ArrayList of Video Titles
		ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < videos.size(); i++) {
			titles.add(videos.get(i).getTitle());
		}
		return titles;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns an ArrayList of Picture URLs
	 * 
	 * @return List of Picture URLs (ArrayList<String>)
	 */
	public final ArrayList<String> getPicURLs() {

		// Return the ArrayList of Video Pic URLs
		ArrayList<String> urls = new ArrayList<String>();
		for (int i = 0; i < videos.size(); i++) {
			urls.add(videos.get(i).getPicUrl());
		}
		return urls;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns an ArrayList of Full Video URLs
	 * 
	 * @return List of Video URLs (ArrayList<String>)
	 */
	public final ArrayList<String> getVideoURLs() {

		// Return the ArrayList of Video URLs
		ArrayList<String> urls = new ArrayList<String>();
		for (int i = 0; i < videos.size(); i++) {
			urls.add(videos.get(i).getVideoUrl());
		}
		return urls;
	}

	/**
	 * Create Socket for Google Server
	 */
	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * AUTHENTICATION METHOD Allows Google and YouTube to Authenticate API Token and
	 * User
	 * 
	 * @return Credentials
	 * @throws IOException
	 */
	private static Credential authorize() throws IOException {
		InputStream in = Application.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}

	/**
	 * ACCESSOR METHOD Accesses Google and YouTube Servers via Credentials
	 * 
	 * @return Youtube Object
	 * @throws IOException
	 */
	private static YouTube getYouTubeService() throws IOException {
		Credential credential = authorize();
		return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}

	/**
	 * TO STRING METHOD Pretty Prints the Contents of the Searched Videos
	 * 
	 * @param iteratorSearchResults (Iterator of Search Results)
	 * @param query                 (Queried String)
	 */
	public static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

		System.out.println("\n=============================================================");
		System.out.println("   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			if (rId.getKind().equals("youtube#video")) {
				ThumbnailDetails thumbnail = singleVideo.getSnippet().getThumbnails();

				System.out.println(" Video Id: " + rId.getVideoId());
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				System.out.println(" Thumbnail: " + thumbnail.getDefault().getUrl());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}
}
