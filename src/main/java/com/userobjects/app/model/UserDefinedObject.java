package com.userobjects.app.model;

import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;


import com.userobjects.app.utilities.Utilities;


import lombok.Getter;
import lombok.Setter;
import lombok.Data;


@Data
public class UserDefinedObject {
	

		@Id
		public String id;
		@NotBlank
		private String recordType;
		private Date dateAdded;
		@NotBlank
		private String myObjectId;
		private double rightAcension;
		private double declination;
		private String otherCatalogueId;
		private String description;
		@NotBlank
		private String type;
		@NotBlank
		private String userId;
		private String version;
		private double magnitude;
		private double magnitudeError;
		private double xvalue;
		private double yvalue;
		private double fwhm;
		private double elongation;
		private String sourceFileName;
		
		
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
