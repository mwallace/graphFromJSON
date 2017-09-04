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

// Vertex with label and adjacency list
class Vertex {
	
	private String label;
	private ArrayList<Edge> edges;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	
	// One-way weighted edge to a vertex
	class Edge {
		
		private String adjacentTo;
		private int weight;
		
		public String getAdjacentTo() {
			return adjacentTo;
		}
		public void setAdjacentTo(String adjacentTo) {
			this.adjacentTo = adjacentTo;
		}
		public int getWeight() {
			return weight;
		}
		public void setWeight(int weight) {
			this.weight = weight;
		}
	}
}

