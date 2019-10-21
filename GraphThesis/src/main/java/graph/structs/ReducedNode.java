package graph.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cim.Model;
import graph.graphs.Graph;
import graph.searcher.PathSearcher;

public class ReducedNode extends Node {
	
	protected Graph<Node,Edge> completeGraph;
	protected PathSearcher pathSearcher;
	
	public ReducedNode(Model model) {
		super();
		completeGraph= new Graph<>(model);

	}
	
	public void addCompleteNode(String bar,Node node) {
		completeGraph.addNode(bar, node);
	}
	public void addCompleteEdge(Edge completeEdge,Path c) {
		completeGraph.addEdge(c, completeEdge);
	}
	public int getSizeCompleteGraphNodes() {
		return completeGraph.getSizeNodes();
	}
	public List<String> getCompleteGraphNodesKeys(){
		List<String> keys= new ArrayList<String>();
		for(String key:completeGraph.getBars()) {
			keys.add(key);
		}
		return keys;
	}
	public String getCompleteNodeWithGenerator() {
	
		return completeGraph.getNodeWithGenerator();
	}
	public List<Edge> getPathCompleteGraph(String firstNode){
		pathSearcher= new PathSearcher(completeGraph);
		pathSearcher.setFirst(firstNode);
		pathSearcher.search();
		return pathSearcher.getPathWithEdges();
	}
	public String getLastNode() {
		return pathSearcher.getLastNode();
	}
}
