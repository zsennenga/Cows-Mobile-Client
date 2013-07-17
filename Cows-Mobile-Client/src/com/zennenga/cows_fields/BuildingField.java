package com.zennenga.cows_fields;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BuildingField extends BaseField {
	private String building;
	private String room;
	
	public BuildingField (String building, String room)	{
		this.building = building;
		this.room = room;
		this.fieldName = "BuildingAndRoom";
	}
	@Override
	public void setData(String newData) {
		this.beenValidated = false;
		String[] buildingData = newData.split(":");
		if (buildingData[0].length() > 0) this.building = buildingData[0];
		else if (buildingData[1].length() > 0) this.room = buildingData[1];
		else throw new IllegalArgumentException("Invalid Building/Room selection");
		this.beenValidated = true;
	}
	@Override
	public String getData() throws UnsupportedEncodingException	{
		return "&" + this.fieldName + "=" + URLEncoder.encode(building.replace(" ", "_") + "!" + room,"UTF-8");
	}

}
