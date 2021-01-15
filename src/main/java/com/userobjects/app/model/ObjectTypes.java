package com.userobjects.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ObjectTypes {
	
	public static final String VARIABLE_STAR = "Variable Star";
	public static final String NEBULA = "Nebula";
	public static final String GALAXY = "Galaxy";
	public static final String OTHER = "Other";
	public static final String IMAGE_STAR = "Star";
	
	
	public static List<String> toArrayList() {
		List<String> t = new ArrayList<String>();
		t.add(VARIABLE_STAR);
		t.add(NEBULA);
		t.add(GALAXY);
		t.add(OTHER);
		t.add(IMAGE_STAR);
		return Collections.unmodifiableList(t);
	}
			
}
