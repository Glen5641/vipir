package vipir;

/**
 * Class to Easily Deal with Videos that are queried by setting as a video
 * object and encapsulating the Contents of the video.
 * 
 * @author Glen5641
 */
public class Video {

	// Class Variables
	private String id;
	private String title;
	private String thumbnailURL;

	/**
	 * VIDEO CONSTRUCTOR - Builds Video Object
	 * 
	 * @param id    (Identification of Video)
	 * @param title (Title of Video)
	 * @param url   (URL of Video Thumbnail)
	 */
	public Video(String id, String title, String url) {

		// Set Private Variables from Params
		this.id = id;
		this.title = title;
		this.thumbnailURL = url;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Returns the ID of Video
	 * 
	 * @return ID of Video (String)
	 */
	public final String getID() {

		// Return Video ID
		return this.id;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Return the Title of Video
	 * 
	 * @return Title of Video (String)
	 */
	public final String getTitle() {

		// Return Video Title
		return this.title;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Return Thumbnail URL of Video
	 * 
	 * @return Thumbnail URL (String)
	 */
	public final String getPicUrl() {

		// Return Thumbnail URL of Video
		return this.thumbnailURL;
	}

	/**
	 * ACCESSOR METHOD - IMMUTABLE - Return the full URL of Video
	 * 
	 * @return Video URL (String)
	 */
	public final String getVideoUrl() {

		// Return the full URL of Video
		return ("https://www.youtube.com/watch?v=" + this.id);
	}
	
	public String toString() {
		String s = 	"ID            : " + getID() + "\n" +
					"Title         : " + getTitle() + "\n" +
					"ThumbNail URL : " + getPicUrl() + "\n" +
					"Video URL     : " + getVideoUrl();
		return s;
	}
}
