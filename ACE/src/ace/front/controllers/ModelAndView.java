package ace.front.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView implements Serializable{
//다음 이동할 뷰의 이름, 전달하는 key와 value값을 map
	private String viewName;
	private Map<String,Object> model = new HashMap<>();
	
	public ModelAndView() {}

	public ModelAndView(String viewName) {
		this.viewName = viewName;
	}
	
	public ModelAndView(String viewName, String key, Object obj) {
		this.viewName = viewName;
		this.addObject(key, obj);
	}
	
	public void addObject(String key, Object obj) {
		model.put(key, obj);
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "ModelAndView [viewName=" + viewName + ", model=" + model + "]";
	}
	
	
	
}
