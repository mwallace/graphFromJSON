package graphFromJSON;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
		g.addEdge(v1, v2, 5);
		Vertex v3 = new Vertex("g");
		g.removeVertex(v3);
		Vertex v4 = new Vertex("x");
		v4.setEdge("aa", 5);
		g.addVertex(v4);
		g.print();
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
			verticesSet = 
					gson.fromJson(fr, new TypeToken<HashSet<Vertex>>(){}.getType());
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
	
	// The JSON data might not contain a valid graph structure, so this data must be 
	// validated (e.g. if the JSON indicates an edge involving a vertex that isn't
	// also defined in its own entry, then it won't get added to the vertex set. We
	// need to add it to the set manually).
	private void validateGraph() {
		addMissingVertices();
	}

	// Verify that every vertex listed as an edge exists in graph. If it doesn't
	// exist, add it with no new edges. We do this by creating a set of all 
	// vertex labels that are reachable by vertices already present in the graph.
	private void addMissingVertices() {
		HashSet<String> reachableVertices = new HashSet<String>();
		// Populate set
		for (Vertex v: verticesSet) {
			// Add labels in each vertex's adjacency list to set
			ArrayList<String> edges = v.getAdjacent();
			for (String e: edges) {
				reachableVertices.add(e);
			}
		}
		reachableVertices.forEach(
					(label) -> {
			            if (!verticesSet.contains(label)) {
			            	Vertex vertex = new Vertex(label);
			            	verticesSet.add(vertex);
			            }
					}
				);
	}

	// Add edge between two vertices
	public void addEdge(Vertex src, Vertex dst, int weight) {
		// Add edge between src and dst
		src.setEdge(dst.getLabel(), weight);
		// If src does not exist in graph, add it
		if (!verticesSet.contains(src))
			verticesSet.add(src);
		// If dst does not exist in graph, add it
		if (!verticesSet.contains(dst))
			verticesSet.add(dst);
	}
	
	// Remove edge between two vertices
	public void removeEdge(Vertex src, Vertex dst) {
		if (verticesSet.contains(src)) {
			src.removeEdge(dst);
		}
	}
	
	// Add vertex
	public void addVertex(Vertex addMe) {
		// Check to see if vertex exists in graph
		if (!verticesSet.contains(addMe)) {
			verticesSet.add(addMe);
		} else {
			// The "addMe" vertex already exists in the graph. Merge its adjacency 
			// list with the pre-existing vertex. Since the adjacency list is a set, 
			// we can simply add the two lists to merge without duplicates.
		    for (Iterator<Vertex> it = verticesSet.iterator(); it.hasNext(); ) {
		        Vertex v = it.next();
		        if (v.getLabel() == addMe.getLabel()) {
		        	v.mergeEdges(addMe.getEdges());
		        }
		    }
		}
		// Check to see if all vertices in the adjacency list exist in 
		// the graph
		addMe.getAdjacent().forEach(
				(label) -> {
		            if (!verticesSet.contains(label)) {
		            	Vertex vertex = new Vertex(label);
		            	verticesSet.add(vertex);
		            }
				}
			);			
	}
	
	// Remove vertex
	public void removeVertex(Vertex removeMe) {
		if (verticesSet.contains(removeMe)) {
			// Remove all edges that reach removeMe
			verticesSet.forEach((v) -> removeEdge(v, removeMe));
			// Remove removeMe from graph
			verticesSet.remove(removeMe);
		}
	}

	// Print out the complete adjacency list
	public void print() {
		for (Vertex v: verticesSet) {
			v.print();
			System.out.println();
		}
	}
	//protected ArrayList<Vertex> vertices;
	protected HashSet<Vertex> verticesSet;
}

//Vertex with label and adjacency list
class Vertex {
	
	public Vertex(String label) {
		this.label = label;
		edges = new HashSet<Edge>();
	}
	
	// Print out the vertex label and its adjacency list, including weights
	public void print() {
		if (label != null)
			System.out.print(label + "=>");
		if (edges != null) {
			for (Edge e : edges) {
				System.out.println("\t" + e.adjacentTo + "\tweight: " + e.weight);
			}
		}
	}
	
	// Return a list of the labels of each adjacent vertex
	public ArrayList<String> getAdjacent() {
		ArrayList<String> adjacentVertices = new ArrayList<String>();
		for (Edge e: edges) {
			adjacentVertices.add(e.adjacentTo);
		}
		return adjacentVertices;
	}
	
	// Remove edge, give an Vertex object
	public void removeEdge(Vertex deleteMe) {
		// Create a temporary new Edge that simply has the same label as the 
		// Vertex object that we want to delete
		Edge deleteMeEdge = new Edge(deleteMe.getLabel());
		if (edges.contains(deleteMeEdge))
			edges.remove(deleteMeEdge);
	}
	
	// Merge one set of edges with this vertex's edges
	public void mergeEdges(HashSet<Edge> edges) {
		edges.forEach( (e) -> this.edges.add(e));
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public HashSet<Edge> getEdges() {
		if (edges.isEmpty()) {
			return null;
		} else {
			return edges;
		}
	}
	
	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}
	
	public void setEdge(String adjacentTo, int weight) {
		Edge e = new Edge(adjacentTo, weight);
		this.edges.add(e);
	}
	
 @Override
 public boolean equals(Object obj) {
     return !super.equals(obj);
 }
 
 public int hashCode() {
     return this.label.hashCode();
 }   
 
	private String label;
	private HashSet<Edge> edges;
	// One-way weighted edge to a vertex
	public class Edge {
		
		public Edge(String adjacentTo, int weight) {
			this.adjacentTo = adjacentTo;
			this.weight = weight;
		}
		
		public Edge(String adjacentTo) {
			this.adjacentTo = adjacentTo;
		}
		
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
		
	    @Override
	    public boolean equals(Object obj) {
	        return !super.equals(obj);
	    }
	    
	    public int hashCode() {
	        return this.adjacentTo.hashCode();
	    }
	    
		private String adjacentTo;
		private int weight;
	}
}