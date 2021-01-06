package com.userobjects.app.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.userobjects.app.controller.UserObjectController;
import com.userobjects.app.model.ObjectTypes;
import com.userobjects.app.model.UserDefinedObject;

@Component
public class Utilities {
	
	Logger logger = LoggerFactory.getLogger(Utilities.class);
	
	public Utilities() {}
	
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
	
	
	//basic tests for now
	public String validateObjectFields(UserDefinedObject o) {
		logger.info("validating user defined object");
		String  res = "";
		if(!notNull(o.getDeclination()) || o.getDeclination().length() < 6 ) {
			logger.info("invalid declination");
			return "Invalid declination";
		}
		if(!notNull(o.getRightAcension() ) || o.getRightAcension().length() < 6 ) {
			logger.info("invalid right acension");
			return "Invalid right acension";
		}	
		if(!notNull(o.getMyObjectId() ) || o.getMyObjectId().length() < 4 ) {
			logger.info("invalid my object id");
			return "invalid object id";
		}	
		if(!notNull(o.getOtherCatalogueId()  )) {
			logger.info("invalid alt catalogue");
			return "invalid catalogue Id";
		}	
		return res;		
	}
	
	public static<T> Boolean notNull(T n)  {
		if( n == null ) {
			return false;
		} else {
			return true;
		}
	}	
	


}
