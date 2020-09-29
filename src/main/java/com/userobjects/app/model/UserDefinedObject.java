package com.userobjects.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class UserDefinedObject {

		@Id
		public String id;
		private Date dateAdded;
		private String myObjectId;
		private String rightAcension;
		private String declination;
		private String otherCatalogueId;
		private String description;
		private String type;
		
		public UserDefinedObject() {}
}
