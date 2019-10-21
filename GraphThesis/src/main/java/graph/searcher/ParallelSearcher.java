package graph.searcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.graphs.ReducedGraphEPS;
import graph.structs.Edge;
import graph.structs.Path;

public class ParallelSearcher extends Searcher {

	Map<Duo, List<List<Edge>>> possibleParallels;

	public ParallelSearcher(ReducedGraphEPS toSearch) {
		super(toSearch);
		possibleParallels = new HashMap<Duo, List<List<Edge>>>();
	}

	@Override
	public void search() {
		Set<String> nodes = graph.getBars();
		List<Edge> path;
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

		Duo bars = new Duo(nodeWithGenerador, node);
		List<Edge> finalPath = new ArrayList<Edge>(path);
		if (possibleParallelExists(bars.getBarOne(), bars.getBarTwo())) {
			List<List<Edge>> newEntry = possibleParallels.get(bars);
			newEntry.add(finalPath);
			possibleParallels.put(bars, newEntry);
		} else {
			List<List<Edge>> newEntry = new ArrayList<List<Edge>>();
			newEntry.add(finalPath);
			possibleParallels.put(bars, newEntry);
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

	public boolean possibleParallelExists(String bar1, String bar2) {
		return possibleParallels.containsKey(new Duo(bar1, bar2));
	}

	@Override
	public void print() {
		Set<Duo> duos = possibleParallels.keySet();
		System.out.println("Duos con distintos caminos:");
		for (Duo bars : duos) {
			System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
			List<List<Edge>> pathsList = possibleParallels.get(bars);
			for (List<Edge> path : pathsList) {
				System.out.println("Recorrido " + pathsList.indexOf(path) + ":");
				for (Edge a : path) {
					System.out.println("     " + a.toString());
				}

			}

		}

	}
/*
	public void printTranslatedPossibleParallels() {
		Map<Duo, ArrayList<ArrayList<Edge>>> translatedParallels = graph.translateHashMap(possibleParallels);
		Set<Duo> duos = translatedParallels.keySet();
		System.out.println("Duos con distintos caminos:");
		for (Duo bars : duos) {
			System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
			ArrayList<ArrayList<Edge>> pathsList = translatedParallels.get(bars);
			for (ArrayList<Edge> path : pathsList) {
				System.out.println("Recorrido " + pathsList.indexOf(path) + ":");
				for (Edge a : path) {
					System.out.println("     " + a.toString());
				}

			}

		}
	}*/

	public void printTranslatedParallels() {
		
		Map<Duo, List<List<Edge>>> translatedParallels = ((ReducedGraphEPS) graph).translateHashMap(possibleParallels);
		System.out.println("Paralelos:");
		List<Duo> repeatedBars = getRepeatedBars(translatedParallels);
		for (Duo bars : repeatedBars) {
			System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
			List<List<Edge>> pathsList = translatedParallels.get(bars);
			for (List<Edge> paths : pathsList) {
				System.out.println("Recorrido " + pathsList.indexOf(paths) + ":");
				for (Edge a : paths) {
					System.out.println("     " + a.toString());
				}

			}

		}

	}

	public void printParallels() {
		System.out.println("Paralelos:");
		List<Duo> repeatedBars = getRepeatedBars(possibleParallels);
		for (Duo bars : repeatedBars) {
			System.out.println("Barra 1:" + bars.getBarOne() + ", Barra 2:" + bars.getBarTwo());
			List<List<Edge>> pathsList = possibleParallels.get(bars);
			for (List<Edge> recorridos : pathsList) {
				System.out.println("Recorrido " + pathsList.indexOf(recorridos) + ":");
				for (Edge a : recorridos) {
					System.out.println("     " + a.toString());
				}

			}

		}
	}

	List<Duo> getRepeatedBars(Map<Duo,List<List<Edge>>> hashParallels) {
		List<Duo> repeatedBars = new ArrayList<Duo>();
		Set<Duo> duos = hashParallels.keySet();
		List<Duo> duosList = new ArrayList<Duo>();
		for (Duo bars : duos) {
			duosList.add(bars);
		}
		for (Duo bars : duos) {
			if (repeated(duosList, bars.getBarTwo())) {
				repeatedBars.add(bars);
			}
		}

		return repeatedBars;

	}

	boolean repeated(List<Duo> list, String bar) {
		boolean repeat = false;
		int times = 0;
		int i = 0;
		String barToCompare;
		Duo duo;
		while (i < list.size() && !repeat) {
			duo = list.get(i);
			barToCompare = duo.getBarTwo();
			if (bar.equals(barToCompare)) {
				times = times + 1;
				if (times > 1) {
					repeat = true;
				}
			}
			i = i + 1;
		}
		return repeat;
	}

}
