package com.zennenga.cows_fields;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;

public class BuildingField extends BaseField {
	private String building;
	private String room;

	public BuildingField(String building, String room) {
		this.building = building;
		this.room = room;
		this.fieldName = "BuildingAndRoom";
		this.beenValidated = false;
		this.optional = false;
	}

	/**
	 * Updates either the building or the room depending on the format of the
	 * newData string
	 */
	@Override
	public void setData(String newData) {
		this.beenValidated = false;
		if (newData.equals(":")) {
			Log.i("BuildingRoom", "Tap to select detected");
			return;
		}
		String[] buildingData = newData.split(":");
		if (buildingData[0].length() > 0)
			this.building = buildingData[0];
		else if (buildingData[1].length() > 0)
			this.room = buildingData[1];
		else
			throw new IllegalArgumentException(
					"Invalid Building/Room selection");
		this.beenValidated = true;
	}

	@Override
	public String getData() throws UnsupportedEncodingException {
		return "&"
				+ this.fieldName
				+ "="
				+ URLEncoder.encode(building.replace(" ", "_") + "!" + room,
						"UTF-8");
	}

	@Override
	public Boolean checkValidation() {
		return this.room != "" && this.building != "" && this.beenValidated;
	}

}
