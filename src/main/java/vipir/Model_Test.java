package vipir;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
		
		//System.out.println("Search Something!!!");
		//Scanner sc = new Scanner(System.in);
		//model.setSearch(sc.nextLine());
		
		//ArrayList<Video> videos = model.getVideosList();
		ArrayList<Video> action = model.getAction();
		ArrayList<Video> comedy = model.getComedy();
		ArrayList<Video> romance = model.getRomance();
		ArrayList<Video> horror = model.getHorror();
		ArrayList<Video> drama = model.getDrama();
		ArrayList<Video> fantasy = model.getFantasy();
		ArrayList<Video> scifi = model.getScifi();
		ArrayList<Video> documentary = model.getDocumentary();
		ArrayList<Video> games = model.getGames();
		ArrayList<Video> anime = model.getAnime();
		
		for(int i = 0; i < action.size(); i++) {
			//System.out.println(videos.get(i).toString());
			System.out.println(i*11+1);
			System.out.println(action.get(i).toString());
			System.out.println(i*11+2);
			System.out.println(comedy.get(i).toString());
			System.out.println(i*11+3);
			System.out.println(romance.get(i).toString());
			System.out.println(i*11+4);
			System.out.println(horror.get(i).toString());
			System.out.println(i*11+5);
			System.out.println(drama.get(i).toString());
			System.out.println(i*11+6);
			System.out.println(fantasy.get(i).toString());
			System.out.println(i*11+7);
			System.out.println(scifi.get(i).toString());
			System.out.println(i*11+8);
			System.out.println(documentary.get(i).toString());
			System.out.println(i*11+9);
			System.out.println(games.get(i).toString());
			System.out.println(i*11+10);
			System.out.println(anime.get(i).toString());
			System.out.println(i*11+11);
		}
		//sc.close();
	}
}
