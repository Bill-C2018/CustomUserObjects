package com.userobjects.app.utilities;

import com.userobjects.app.model.ObjectTypes;
import com.userobjects.app.model.UserDefinedObject;

public class utilities {
	
	public utilities() {}
	
	public void validateObjectType(UserDefinedObject object) {
		
		String type = object.getType();
		if(type == null) {
			object.setType(ObjectTypes.OTHER);
			return;
		}
			
		if(type.equalsIgnoreCase(ObjectTypes.OTHER)) {
			object.setType(ObjectTypes.OTHER);
			return;
		} else if(type.equalsIgnoreCase(ObjectTypes.GALAXY)) {
			object.setType(ObjectTypes.GALAXY);
			return;
		} else if(type.equalsIgnoreCase(ObjectTypes.VARIABLE_STAR)) {
			object.setType(ObjectTypes.VARIABLE_STAR);
			return;
		} else if(type.equalsIgnoreCase(ObjectTypes.NEBULA)) {
			object.setType(ObjectTypes.NEBULA);
			return;
		} 
		
		object.setType(ObjectTypes.OTHER);
		
		
	}

}
