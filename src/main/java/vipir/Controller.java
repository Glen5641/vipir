package vipir;

import java.util.ArrayList;

public final class Controller {

	private Model model;
	private final ArrayList<View> views;

	public Controller() {
		this.views = new ArrayList<View>();
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void update(String key, Object value) {
		for (View v : views)
			v.update(key, value);
	}

	public void addView(View view) {
		view.initialize();
		views.add(view);
	}

	public void removeView(View view) {
		views.remove(view);
		view.terminate();
		if (views.isEmpty())
			System.exit(0);
	}

	public Object get(String key) {
		return model.getValue(key);
	}

	public void set(String key, Object value) {
		System.out.println("controller: set " + key + " to " + value);
		model.setValue(key, value);
	}

	public void trigger(String name) {
		System.out.println("controller: trigger " + name);
		model.trigger(name);
	}
}

/*
 * import com.google.api.client.auth.oauth2.Credential; import
 * com.google.api.client.extensions.java6.auth.oauth2.
 * AuthorizationCodeInstalledApp; import
 * com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
 * import
 * com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
 * import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
 * import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
 * import com.google.api.client.googleapis.json.GoogleJsonResponseException;
 * import com.google.api.client.http.HttpTransport; import
 * com.google.api.client.json.jackson2.JacksonFactory; import
 * com.google.api.client.json.JsonFactory; import
 * com.google.api.client.util.store.FileDataStoreFactory;
 * 
 * import com.google.api.services.youtube.YouTubeScopes; import
 * com.google.api.services.youtube.model.*; import
 * com.google.api.services.youtube.YouTube; import
 * com.google.api.services.youtube.YouTube.Search;
 * 
 * import java.io.BufferedReader; import java.io.IOException; import
 * java.io.InputStream; import java.io.InputStreamReader; import
 * java.util.Arrays; import java.util.Iterator; import java.util.List; import
 * java.util.Properties;
 * 
 * public class Quickstart {
 * 
 * private static final String APPLICATION_NAME = "API Sample";
 * 
 * private static final java.io.File DATA_STORE_DIR = new
 * java.io.File(System.getProperty("user.home"),
 * ".credentials/youtube-java-quickstart");
 * 
 * private static FileDataStoreFactory DATA_STORE_FACTORY;
 * 
 * private static final JsonFactory JSON_FACTORY =
 * JacksonFactory.getDefaultInstance();
 * 
 * private static HttpTransport HTTP_TRANSPORT;
 * 
 * private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
 * 
 * private static final String PROPERTIES_FILENAME = "youtube.properties";
 * 
 * private static final List<String> SCOPES =
 * Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);
 * 
 * private static YouTube youtube = null;
 * 
 * static { try { HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
 * DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR); } catch
 * (Throwable t) { t.printStackTrace(); System.exit(1); } }
 * 
 * public static Credential authorize() throws IOException { InputStream in =
 * Quickstart.class.getResourceAsStream("/client_secret.json");
 * GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
 * new InputStreamReader(in));
 * 
 * GoogleAuthorizationCodeFlow flow = new
 * GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
 * clientSecrets,
 * SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").
 * build(); Credential credential = new AuthorizationCodeInstalledApp(flow, new
 * LocalServerReceiver()).authorize("user"); return credential; }
 * 
 * public static YouTube getYouTubeService() throws IOException { Credential
 * credential = authorize(); return new YouTube.Builder(HTTP_TRANSPORT,
 * JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME) .build(); }
 * 
 * public static void main(String[] args) { try { youtube = getYouTubeService();
 * } catch (IOException e1) {
 * System.err.println("Could not initiallize youtube."); } try {
 * YouTube.Channels.List channelsListByUsernameRequest = youtube.channels()
 * .list("snippet,contentDetails,statistics");
 * channelsListByUsernameRequest.setForUsername("GoogleDevelopers");
 * 
 * ChannelListResponse response = channelsListByUsernameRequest.execute();
 * Channel channel = response.getItems().get(0); System.out.
 * printf("This channel's ID is %s. Its title is '%s', and it has %s views.\n",
 * channel.getId(), channel.getSnippet().getTitle(),
 * channel.getStatistics().getViewCount());
 * 
 * Properties properties = new Properties(); try { InputStream in =
 * Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
 * properties.load(in);
 * 
 * } catch (IOException e) { System.err.println("There was an error reading " +
 * PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
 * System.exit(1); }
 * 
 * String queryTerm = getInputQuery();
 * 
 * YouTube.Search.List search = youtube.search().list("id,snippet");
 * 
 * String apiKey = properties.getProperty("youtube.apikey");
 * search.setKey(apiKey); search.setQ(queryTerm);
 * 
 * search.setType("video");
 * 
 * search.setFields(
 * "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
 * search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
 * 
 * SearchListResponse searchResponse = search.execute(); List<SearchResult>
 * searchResultList = searchResponse.getItems(); if (searchResultList != null) {
 * prettyPrint(searchResultList.iterator(), queryTerm); } } catch
 * (GoogleJsonResponseException e) { e.printStackTrace(); System.err.println(
 * "There was a service error: " + e.getDetails().getCode() + " : " +
 * e.getDetails().getMessage()); } catch (Throwable t) { t.printStackTrace(); }
 * }
 * 
 * private static String getInputQuery() throws IOException {
 * 
 * String inputQuery = "";
 * 
 * System.out.print("Please enter a search term: "); BufferedReader bReader =
 * new BufferedReader(new InputStreamReader(System.in)); inputQuery =
 * bReader.readLine();
 * 
 * if (inputQuery.length() < 1) { inputQuery = "YouTube Developers Live"; }
 * return inputQuery; }
 * 
 * private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults,
 * String query) {
 * 
 * System.out.println(
 * "\n=============================================================");
 * System.out.println("   First " + NUMBER_OF_VIDEOS_RETURNED +
 * " videos for search on \"" + query + "\"."); System.out.println(
 * "=============================================================\n");
 * 
 * if (!iteratorSearchResults.hasNext()) {
 * System.out.println(" There aren't any results for your query."); }
 * 
 * while (iteratorSearchResults.hasNext()) {
 * 
 * SearchResult singleVideo = iteratorSearchResults.next(); ResourceId rId =
 * singleVideo.getId();
 * 
 * if (rId.getKind().equals("youtube#video")) { Thumbnail thumbnail =
 * singleVideo.getSnippet().getThumbnails().getDefault();
 * 
 * System.out.println(" Video Id: " + rId.getVideoId());
 * System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
 * System.out.println(" Thumbnail: " + thumbnail.getUrl()); System.out.println(
 * "\n-------------------------------------------------------------\n"); } } } }
 * 
 */