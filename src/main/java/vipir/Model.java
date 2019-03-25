
package vipir;

import java.util.HashMap;
import javafx.application.Platform;

public final class Model {
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
	}

	public Object getValue(String key) {
		return properties.get(key);
	}

	public void setValue(String key, Object value) {
		if (properties.containsKey(key) && properties.get(key).equals(value)) {
			System.out.println("  model: value not changed");
			return;
		}
		Platform.runLater(new Updater(key, value));
	}

	public void trigger(String name) {
		System.out.println("  model: (not!) calculating function: " + name);
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

//******************************************************************************
