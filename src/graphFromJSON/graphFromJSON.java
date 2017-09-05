package graphFromJSON;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class graphFromJSON {

	public static void main(String[] args) {
		// Read JSON file
		String input = "./json/graph.json";
		// Convert JSON to vertex object
		Graph g = new Graph(input);

		Vertex v1 = new Vertex("x");
		Vertex v2 = new Vertex("y");
		g.addEdge(v1, v2, 5);
		
		g.print();
	}
	
}

// Vertex with label and adjacency list
class Vertex {
	
	public Vertex(String label) {
		this.label = label;
		edges = new ArrayList<Edge>();
	}
	
	public void print() {
		if (label != null)
			System.out.print(label + "=>");
		if (edges != null) {
			for (Edge e : edges) {
				System.out.println("\t" + e.adjacentTo + "\tweight: " + e.weight);
			}
		}
	}
	
	private String label;
	private ArrayList<Edge> edges;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	public void setEdge(String adjacentTo, int weight) {
		Edge e = new Edge(adjacentTo, weight);
		this.edges.add(e);
	}
	public ArrayList<String> getAdjacent() {
		ArrayList<String> adjacentVertices = new ArrayList<String>();
		for (Edge e: edges) {
			adjacentVertices.add(e.adjacentTo);
		}
		return adjacentVertices;
	}
	
	// One-way weighted edge to a vertex
	class Edge {
		
		public Edge(String adjacentTo, int weight) {
			this.adjacentTo = adjacentTo;
			this.weight = weight;
		}
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

class Graph {
	
	// Take a path to a JSON file and build a graph
	public Graph(String pathToJSON) {
		jsonToVertex(pathToJSON);
		validateGraph();
	}
	
	// Read a JSON file and convert it into a weighted graph
	private void jsonToVertex(String pathToJSON) {
		Gson gson = new Gson();
		try {
			FileReader fr = new FileReader(pathToJSON);
			vertices = gson.fromJson(fr, new TypeToken<ArrayList<Vertex>>(){}.getType());

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
	
	// Verify that every vertex listed as an edge exists in graph. If it doesn't
	// exist, add it with no new edges.
	private void validateGraph() {
		HashMap<String, Integer> labelsInGraph = new HashMap<String, Integer>();
		HashMap<String, Integer> labelsToCheck = new HashMap<String, Integer>();
		// Build a hash table with all vertex labels
		for (Vertex v: vertices) {
			// Add labels of vertices in graph to hash table
			labelsInGraph.put(v.getLabel(), 0);
			// Add labels in each vertex's adjacency list to second hash table
			ArrayList<String> edges = v.getAdjacent();
			for (String e: edges) {
				labelsToCheck.put(e, 0);
			}
		}
        for (String l: labelsToCheck.keySet())
        {
            if (!labelsInGraph.containsKey(l)) {
            	Vertex v = new Vertex(l);
            	vertices.add(v);
            }
        } 
	}
	
	public void addEdge(Vertex src, Vertex dst, int weight) {
		// If either src or dst do not already exist in graph, add them
		if (!vertices.contains(src))
			vertices.add(src);
		if (!vertices.contains(dst))
			vertices.add(dst);
		for (Vertex v: vertices) {
			if (v == src) {
				v.setEdge(dst.getLabel(), weight);
			}
		}
	}
	
	// Print out the complete adjacency list
	public void print() {
		for (Vertex v: vertices) {
			v.print();
			System.out.println();
		}
	}
	protected ArrayList<Vertex> vertices;
}
