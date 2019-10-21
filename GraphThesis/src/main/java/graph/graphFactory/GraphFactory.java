package graph.graphFactory;

import graph.graphs.Graph;
import graph.structs.Edge;
import graph.structs.Node;

public interface GraphFactory {
	
	public Graph<? extends Node,? extends Edge> createGraph();

}
