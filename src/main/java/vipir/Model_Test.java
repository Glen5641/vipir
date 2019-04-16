package vipir;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Test Cases for Model
 * @author Glen5641
 *
 */
public class Model_Test {

	public static void main(String[] args) {

		Controller 	controller = new Controller();
		Model 		model      = new Model(controller);
		controller.setModel(model);
		
		model.setSearch("Games");
		
		ArrayList<Video> 		videos      = model.getVideosList();
		HashMap<String, Video> 	videomap	= model.getVideosMap();
		Video[] 				videoarr    = model.getVideosAsArray();
		ArrayList<String> 		purls       = model.getPicURLs();
		ArrayList<String> 		ids         = model.getVideoIDs();
		ArrayList<String> 		titles      = model.getVideoTitles();
		ArrayList<String> 		vurls       = model.getVideoURLs();
		Video 					vid         = videos.get(0);
		System.out.println("Query         : " + model.getQuery());		
		System.out.println(vid.toString());

	}
}
