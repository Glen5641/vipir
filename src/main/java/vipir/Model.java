
package vipir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public final class Model {
	
	private static final String APPLICATION_NAME = "API Sample";
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/youtube-java-quickstart");
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
	private static final String PROPERTIES_FILENAME = "youtube.properties";
	private static final List<String> SCOPES = Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);
	private static YouTube youtube = null;
	private final Controller controller;
	private final HashMap<String, Object> properties;

	public Model(Controller controller) {
		
		this.controller = controller;
		
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
		
		try {
			youtube = getYouTubeService();
		} catch (IOException e1) {
			System.err.println("Could not initiallize youtube.");
		}
		try {
			YouTube.Channels.List channelsListByUsernameRequest = youtube.channels()
					.list("snippet,contentDetails,statistics");
			channelsListByUsernameRequest.setForUsername("GoogleDevelopers");

			ChannelListResponse response = channelsListByUsernameRequest.execute();
			Channel channel = response.getItems().get(0);
			System.out.printf("This channel's ID is %s. Its title is '%s', and it has %s views.\n", channel.getId(),
					channel.getSnippet().getTitle(), channel.getStatistics().getViewCount());

			Properties properties = new Properties();
			try {
				InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
				properties.load(in);

			} catch (IOException e) {
				System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : "
						+ e.getMessage());
				System.exit(1);
			}

			String queryTerm = getInputQuery();

			YouTube.Search.List search = youtube.search().list("id, snippet");

			String apiKey = properties.getProperty("youtube.apikey");
			search.setKey(apiKey);
			search.setQ(queryTerm);

			search.setType("video");

			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

			SearchListResponse searchResponse = search.execute();
			List<SearchResult> searchResultList = searchResponse.getItems();
			if (searchResultList != null) {
				prettyPrint(searchResultList.iterator(), queryTerm);
			}
		} catch (GoogleJsonResponseException e) {
			e.printStackTrace();
			System.err.println(
					"There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	public static Credential authorize() throws IOException {
		InputStream in = Application.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}

	public static YouTube getYouTubeService() throws IOException {
		Credential credential = authorize();
		return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
	}
	
	private static String getInputQuery() throws IOException {

		String inputQuery = "";

		System.out.print("Please enter a search term: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		inputQuery = bReader.readLine();

		if (inputQuery.length() < 1) {
			inputQuery = "YouTube Developers Live";
		}
		return inputQuery;
	}

	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

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
	
	public Object getValue(String key) {
		return properties.get(key);
	}

	public void setValue(String key, Object value) {
		if (properties.containsKey(key) && properties.get(key).equals(value)) {
			//System.out.println("  model: value not changed");
			return;
		}
		Platform.runLater(new Updater(key, value));
	}

	public void trigger(String name) {
		//System.out.println("  model: (not!) calculating function: " + name);
	}

	private class Updater implements Runnable {
		private final String key;
		private final Object value;

		public Updater(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public void run() {
			properties.put(key, value);
			controller.update(key, value);
		}
	}
}
