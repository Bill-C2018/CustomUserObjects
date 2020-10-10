package com.userobjects.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;



import lombok.Data;



@Data
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
		}
		
		public void updateObject(UserDefinedObject newObject) {
			if(notNull(newObject.dateAdded)) {
				this.dateAdded = newObject.dateAdded;
			}
			if(notNull(newObject.myObjectId)) {
				this.myObjectId = newObject.myObjectId;
			}
			if(notNull(newObject.rightAcension)) {
				this.rightAcension = newObject.rightAcension;
			}
			if(notNull(newObject.declination)) {
				this.declination = newObject.declination;
			}
			if(notNull(newObject.description)) {
				this.description = newObject.description;
			}
			if(notNull(newObject.type)) {
				this.type = newObject.type;
			}
			if(notNull(newObject.userId)) {
				this.userId = newObject.userId;
			}


		}
		
		static  <T> Boolean notNull(T n)  {
			if (n == null) {
				return false;
			}
			return true;
		}		
}
