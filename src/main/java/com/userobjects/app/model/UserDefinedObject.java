package com.userobjects.app.model;

import java.util.Date;


import org.springframework.data.annotation.Id;

import com.userobjects.app.utilities.Utilities;


import lombok.Getter;
import lombok.Setter;


//lombok annotation not a reference to @Entity
//@Data
//try with getter setter and entity 
@Getter
@Setter
//@Entity
public final class UserDefinedObject {
	

		@Id
		public String id;
		private Date dateAdded;
		private String myObjectId;
		private String rightAcension;
		private String declination;
		private String otherCatalogueId;
		private String description;
		private String type;
		private String userId;
		private String version;
		
		public UserDefinedObject() {
			this.version = "0.01";
			this.userId = "1";
		}
		
		public void updateObject(UserDefinedObject newObject) {
			if(Utilities.notNull(newObject.dateAdded)) {
				this.dateAdded = newObject.dateAdded;
			}
			if(Utilities.notNull(newObject.myObjectId)) {
				this.myObjectId = newObject.myObjectId;
			}
			if(Utilities.notNull(newObject.rightAcension)) {
				this.rightAcension = newObject.rightAcension;
			}
			if(Utilities.notNull(newObject.declination)) {
				this.declination = newObject.declination;
			}
			if(Utilities.notNull(newObject.otherCatalogueId)) {
				this.otherCatalogueId = newObject.otherCatalogueId;
			}
			if(Utilities.notNull(newObject.description)) {
				this.description = newObject.description;
			}
			if(Utilities.notNull(newObject.type)) {
				this.type = newObject.type;
			}
			if(Utilities.notNull(newObject.userId)) {
				this.userId = newObject.userId;
			}

		}
		
	
}
