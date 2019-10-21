package graph.searcher;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.graphs.Graph;
import graph.graphs.ReducedGraphEPS;
import graph.structs.Edge;

public abstract class Searcher {

	Map<String, Boolean> visited;

	Graph graph;

	public Searcher(Graph toSearch) {
		graph = toSearch;
		Set<String> nodes = graph.getBars();
		visited = new HashMap<String, Boolean>();
		for (String node : nodes) {
			visited.put(node, false);
		}

	}

	abstract void search();

	abstract void dfs(String nodeStart, List<Edge> path, String nodeFinal);

	abstract void print();

	public void printVisted() {
		Set<String> allVisited = visited.keySet();
		System.out.println("Nodos visitados:");
		for (String node : allVisited) {
			System.out.println(node + ": " + visited.get(node));
		}
	}

}
