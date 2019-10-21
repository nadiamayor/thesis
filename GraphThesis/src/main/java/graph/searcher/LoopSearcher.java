package graph.searcher;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.graphs.ReducedGraphEPS;
import graph.structs.Edge;
import graph.structs.Path;

public class LoopSearcher extends Searcher {

	Map<Duo, List<List<Edge>>> possibleLoops;

	public LoopSearcher(ReducedGraphEPS toSearch) {
		super(toSearch);
		possibleLoops = new HashMap<Duo, List<List<Edge>>>();
	}

	@Override
	public void search() {
		Set<String> nodes = graph.getBars();
		ArrayList<Edge> path;
		for (String node : nodes) {
			if (!visited.get(node) && graph.nodeHasGenerator(node)) {
				path = new ArrayList<Edge>();
				dfs(node, path, node);
			}

		}

	}

	@Override
	void dfs(String node, List<Edge> path, String nodeWithGenerador) {
		
		visited.put(node, true);
		 
		if (!node.equals(nodeWithGenerador)) {
			Duo bars = new Duo(nodeWithGenerador, node);
			List<Edge> finalPath = new ArrayList<Edge>(path);
			if (possibleLoopExists(bars.getBarOne(), bars.getBarTwo())) {
				List<List<Edge>> newEntry = possibleLoops.get(bars);
				newEntry.add(finalPath);
				possibleLoops.put(bars, newEntry);
			} else {
				List<List<Edge>> newEntry = new ArrayList<List<Edge>>();
				newEntry.add(finalPath);
				possibleLoops.put(bars, newEntry);
			}
		}
		List<String> neighbors = graph.getNeighbors(node);
		List<Path> pathList = new ArrayList<Path>();
		Edge edge;
		for (String neighbor : neighbors) {
			if (!visited.get(neighbor)) {
				pathList = graph.getPathToEdges(node, neighbor);
				for (Path c : pathList) {
					edge = new Edge(node, neighbor);
					edge.addComponnents(c);
					path.add(edge);
					dfs(neighbor, path, nodeWithGenerador);
					path.remove(path.size() - 1);
				}
			}
		}

		visited.put(node, false);

	}

	public boolean possibleLoopExists(String bar1, String bar2) {
		return possibleLoops.containsKey(new Duo(bar1, bar2));
		//return possibleLoop.containsKey(new Duo(bar1, bar2)) || possibleLoop.containsKey(new Duo(bar2, bar1));
	}

	@Override
	public void print() {
		Set<Duo> duos = possibleLoops.keySet();
		System.out.println("Duos con distintos caminos:");
		for (Duo bars : duos) {
			System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
			List<List<Edge>> pathsList = possibleLoops.get(bars);
			for (List<Edge> paths : pathsList) {
				System.out.println("Recorrido " + pathsList.indexOf(paths) + ":");
				for (Edge a : paths) {
					System.out.println("     " + a.toString());
				}

			}

		}

	}

	public void printLoops() {
		Set<Duo> duos = possibleLoops.keySet();
		System.out.println("Lazos:");
		for (Duo bars : duos) {
			List<List<Edge>> pathsList = possibleLoops.get(bars);
			if (pathsList.size() > 1) {
				System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
				for (List<Edge> paths : pathsList) {
					System.out.println("Recorrido " + pathsList.indexOf(paths) + ":");
					for (Edge a : paths) {
						System.out.println("     " + a.toString());
					}
				}
			}

		}

	}
	
	public void printTranlatedLoops() {
		Map<Duo, List<List<Edge>>> translated= ((ReducedGraphEPS) graph).translateHashMap(possibleLoops);
		Set<Duo> duos = translated.keySet();
		System.out.println("Lazos:");
		for (Duo bars : duos) {
			List<List<Edge>> pathsList = translated.get(bars);
			if (pathsList.size() > 1) {
				System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
				for (List<Edge> paths : pathsList) {
					System.out.println("Recorrido " + pathsList.indexOf(paths) + ":");
					for (Edge a : paths) {
						System.out.println("     " + a.toString());
					}
				}
			}

		}
	}

}
