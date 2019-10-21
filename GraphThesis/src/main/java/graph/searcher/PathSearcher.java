package graph.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import graph.graphs.Graph;
import graph.graphs.ReducedGraphEPS;
import graph.structs.Edge;
import graph.structs.Path;

public class PathSearcher extends Searcher {
	List<Edge> pathWithEdges;
	String firstNode;


	public PathSearcher(Graph toSearch) {
		super(toSearch);
		pathWithEdges=new ArrayList<Edge>();
		// TODO Auto-generated constructor stub
	}
	public void setFirst(String newFirst) {
		firstNode= newFirst;
	}
	
	public String getLastNode() {
		return pathWithEdges.get(pathWithEdges.size()-1).getBar2();
	}
	public List<Edge> getPathWithEdges(){
		return pathWithEdges;
	}
	@Override
	public void search() {
		Set<String> nodes = graph.getBars();
		List<Edge> pathForDfs=new ArrayList<Edge>();
		for (String bar: nodes) {
			if(!visited.get(bar)&&bar.equals(firstNode)) {
				dfs(bar,pathForDfs,null);
			}
		}
	}

	@Override
	void dfs(String nodeStart, List<Edge> pathComplete, String nodeFinal) {
		visited.put(nodeStart, true);
		List<String> neighbors = graph.getNeighbors(nodeStart);
		Edge edge;
		Path path;
		for (String neighbor : neighbors) {
			if (!visited.get(neighbor)) {
				path= graph.getEdge(nodeStart, neighbor).getComponents().get(0);
				edge=new Edge(nodeStart,neighbor);
				edge.addComponnents(path);
				pathComplete.add(edge);
				dfs(neighbor,pathComplete,nodeFinal);
			}
			if (allVisited()) {
				pathWithEdges=new ArrayList<>(pathComplete);
			}
		}
		visited.put(nodeStart, false);
	}

	

	boolean allVisited() {
		
		boolean visitedAll= false;
		for(String bar:visited.keySet()) {
			if(!visited.get(bar)) {
				visitedAll=false;
				break;
			}
			else 
				visitedAll=true;
		}
			
		return visitedAll;
	}

	@Override
	void print() {
		// TODO Auto-generated method stub

	}

}
