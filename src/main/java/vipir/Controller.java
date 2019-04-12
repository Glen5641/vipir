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
		//System.out.println("controller: set " + key + " to " + value);
		model.setValue(key, value);
	}

	public void trigger(String name) {
		//System.out.println("controller: trigger " + name);
		model.trigger(name);
	}
}

