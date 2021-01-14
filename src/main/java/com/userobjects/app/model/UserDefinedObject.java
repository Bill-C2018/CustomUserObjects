package com.userobjects.app.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;


import com.userobjects.app.utilities.Utilities;


import lombok.Getter;
import lombok.Setter;



@Getter
@Setter

public final class UserDefinedObject {
	

		@Id
		public String id;
		private String recordType;
		private Date dateAdded;
		@NotBlank
		private String myObjectId;
		private String rightAcension;
		private String declination;
		private String otherCatalogueId;
		private String description;
		private String type;
		private String userId;
		private String version;
		private String magnitude;
		private String magnitudeError;
		private double xLocalImage;
		private double yLocalImage;
		private double FWHM;
		private double elongation;
		
		
		public UserDefinedObject() {
			this.version = "0.01";
			this.userId = "1";
		}
		
		{
			this.recordType = "USER_OBJECT";
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
