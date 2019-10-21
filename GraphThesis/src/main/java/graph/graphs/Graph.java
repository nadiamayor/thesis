package graph.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import cim.Model;
import graph.structs.Edge;
import graph.structs.Node;
import graph.structs.Path;

public class Graph<N extends Node, E extends Edge> {

	protected Map<String, N> nodes;
	protected List<E> edges;
	protected Model model;

	public Graph(Model newModel) {
		model = newModel;
		nodes = new HashMap<String, N>();
		edges = new ArrayList<E>();
	}

	public void addNode(String bar, N node) {
		nodes.put(bar, node);
	}

	public N getNode(String bar) {
		return nodes.get(bar);
	}

	public Model getModel() {
		return model;
	}

	public Set<String> getBars() {
		return nodes.keySet();
	}

	public List<Path> getPathsBar(String idBar) {
		return nodes.get(idBar).getPaths();
	}

	public void addPathToNode(String bar, Path newNode) {
		nodes.get(bar).addPath(newNode);
	}

	public void addEdge(Path c, E edge) {
		if (!existEdge(edge.getBar1(), edge.getBar2())) {
			edges.add(edge);
		}
		addPathEdge(c, edge.getBar1(), edge.getBar2());

	}

	public boolean existEdge(String barOne, String barTwo) {
		int j = 0;
		boolean exist = false;
		while (j < edges.size() && !exist) {
			exist = edges.get(j).existMatch(barOne, barTwo);
			j = j + 1;
		}
		return exist;
	}

	public void addPathEdge(Path c, String barOne, String barTwo) {
	
		edges.get(getIndex(barOne, barTwo)).addComponnents(c);
	}

	public void printGraph() {
		System.out.println("Nodos del grafo");
		System.out.println("   " + nodes);
		System.out.println();
		System.out.println("Arcos ");
		edges.stream().map(Object::toString).forEach(System.out::println);
	}

	public List<String> getNeighbors(String idBar) {
		List<String> neighbotingBars = new ArrayList<String>();
		for (Edge a : edges) {
			if (a.getBar1().equals(idBar))
				neighbotingBars.add(a.getBar2());
			if (a.getBar2().equals(idBar))
				neighbotingBars.add(a.getBar1());
		}

		return neighbotingBars;
	}

	public List<Path> getPathToEdges(String barOne, String barTwo) {
		return edges.get(getIndex(barOne, barTwo)).getComponents();
	}
	
	public Edge getEdge(String barOne, String barTwo) {
		
		return edges.get(getIndex(barOne, barTwo));
	}

	public List<E> getEdges() {
		return edges;
	}

	public boolean nodeHasGenerator(String node) {
		return nodes.get(node).hasGenerator(model);

	}
	
	public String getNodeWithGenerator() {
		String result="";
		for(String bar: nodes.keySet()) {
			if (nodes.get(bar).hasGenerator(model)) {
				result=bar;
				break;
			}
		}
		return result;	
	}

	public boolean nodeHasCharge(String node) {
		return nodes.get(node).hasCharge(model);

	}
	
	public int getIndex(String barOne, String barTwo) {
		int j = 0;
		while (!edges.get(j).existMatch(barOne, barTwo) && j < edges.size()) {
			j = j + 1;
		}
		return j;
	}
	public int getSizeNodes() {
		return nodes.size();
	}
	

}
