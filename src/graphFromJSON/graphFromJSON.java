package graphFromJSON;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class graphFromJSON {

	public static void main(String[] args) {
		// Read JSON file
		String input = "./json/graph.json";
		// Convert JSON to vertex object
		jsonToVertex(input);
	}
	
	// Preliminary sanity checking only: Just prints vertex data
	private static void jsonToVertex(String input) {
		Gson gson = new Gson();
		try {
			Vertex[] vertices = gson.fromJson(new FileReader(input), Vertex[].class);
			System.out.println(gson.toJson(vertices));
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

class Vertex {
	private String label;
	private ArrayList<String> edges;
}