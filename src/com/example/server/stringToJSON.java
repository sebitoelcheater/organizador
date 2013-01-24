package com.example.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class stringToJSON {

	/**
	 * @param args
	 * @return
	 */
	public static ArrayList<JSONObject> getArray(String JSONchain, String nombreTabla) {
		try {
			// String the_json =
			// "{'profiles': [{'name':'john', 'age': 44}, {'name':'Alex','age':11}],'weita': [{'name':'john', 'age': 44}, {'name':'Alex','age':11}]}";
			JSONObject myjson = new JSONObject(JSONchain);
			JSONArray the_json_array = myjson.getJSONArray(nombreTabla);
			
			int size = the_json_array.length();
			ArrayList<JSONObject> arrays = new ArrayList<JSONObject>();
			for (int i = 0; i < size; i++) {
				JSONObject another_json_object = the_json_array	.getJSONObject(i);
				arrays.add(another_json_object);
			}

			// Finally
			JSONObject[] jsons = new JSONObject[arrays.size()];
			arrays.toArray(jsons);

			return arrays;

		} catch (JSONException e) {

			e.printStackTrace();
			return null;
		}

	}

}
