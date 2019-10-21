package graph.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cim.Model;
import graph.searcher.Duo;
import graph.structs.Edge;
import graph.structs.Node;
import graph.structs.Path;
import graph.structs.ReducedEdge;
import graph.structs.ReducedNode;

public class ReducedGraphEPS extends Graph<ReducedNode, ReducedEdge> {

	private IdsTranslator idsTranslator;

	public ReducedGraphEPS(Model model) {
		super(model);
	}

	public IdsTranslator getIdsTranslator() {
		return idsTranslator;
	}

	public void setIdsTranslator(IdsTranslator idsTranslator) {
		this.idsTranslator = idsTranslator;
	}

	public void addCompleteEdge(String barOneFic, String barTwoFic, Edge completeEdge, List<Path> completePath) {

		Edge newEdge = new Edge(completeEdge.getBar1(), completeEdge.getBar2());
		List<Path> newComponents = new ArrayList<>(completePath);
		newEdge.setComponents(newComponents);
		int j = 0;
		while (!edges.get(j).existMatch(barOneFic, barTwoFic) && j < edges.size()) {
			j = j + 1;
		}
		ReducedEdge reduceEdge = (ReducedEdge) edges.get(j);
		reduceEdge.addCompleteGraphEdge(newEdge);
		edges.remove(j);
		edges.add(reduceEdge);
	}

	public void addCompleteNode(String barFicId, String bar, Node completeNode) {
		ReducedNode reduceNode = (ReducedNode) nodes.get(barFicId);
		reduceNode.addCompleteNode(bar, completeNode);
		nodes.put(barFicId, reduceNode);
	}

	public void addCompleteEdgeInNode(Edge completeEdge, String barFic, Path path) {
		ReducedNode reduceNode = (ReducedNode) nodes.get(barFic);
		reduceNode.addCompleteEdge(completeEdge, path);
		nodes.put(barFic, reduceNode);

	}

	public Map<String, Boolean> translateNodesWithoutEnergy(Map<String, Boolean> nodes) {
		Map<String, Boolean> translated = new HashMap<>();
		for (String key : nodes.keySet()) {
			List<String> translatedKeys = idsTranslator.getBarsFromTranslation(key);
			for (String translatedKey : translatedKeys) {
				translated.put(translatedKey, nodes.get(key));
			}
		}
		return translated;
	}

	public Map<Duo, List<List<Edge>>> translateHashMap(Map<Duo, List<List<Edge>>> toTranslate) {
		Map<Duo, List<List<Edge>>> translated = new HashMap<Duo, List<List<Edge>>>();
		for (Duo duo : toTranslate.keySet()) {

			List<String> nodesBarOne = nodes.get(duo.getBarOne()).getCompleteGraphNodesKeys();
			List<String> nodesBarTwo = nodes.get(duo.getBarTwo()).getCompleteGraphNodesKeys();

			if (nodesBarOne.size() == 1 && nodesBarTwo.size() == 1) {
				List<List<Edge>> newPathList = translatePaths(toTranslate.get(duo), nodesBarOne.get(0));
				Duo completeDuo = new Duo(nodesBarOne.get(0), nodesBarTwo.get(0));
				translated.put(completeDuo, newPathList);

			} else if (nodesBarOne.size() > 1 && nodesBarTwo.size() == 1) {

				String barOneDuo = nodes.get(duo.getBarOne()).getCompleteNodeWithGenerator();
				List<List<Edge>> newPathList = translatePaths(toTranslate.get(duo), barOneDuo);
				Duo completeDuo = new Duo(barOneDuo, nodesBarTwo.get(0));
				translated.put(completeDuo, newPathList);

			} else if (nodesBarOne.size() == 1 && nodesBarTwo.size() > 1) {
				List<List<Edge>> newPathList = translatePaths(toTranslate.get(duo), nodesBarOne.get(0));
				List<Edge> lastList = newPathList.get(newPathList.size() - 1);
				String barTwo = lastList.get(lastList.size() - 1).getBar2();
				Duo completeDuo = new Duo(nodesBarOne.get(0), barTwo);
				translated.put(completeDuo, newPathList);

			} else {
				String barOneDuo = nodes.get(duo.getBarOne()).getCompleteNodeWithGenerator();
				List<List<Edge>> newPathList = translatePaths(toTranslate.get(duo), barOneDuo);
				List<Edge> lastList = newPathList.get(newPathList.size() - 1);
				String barTwo = lastList.get(lastList.size() - 1).getBar2();
				Duo completeDuo = new Duo(barOneDuo, barTwo);
				translated.put(completeDuo, newPathList);

			}
		}

		return translated;

	}

	List<List<Edge>> translatePaths(List<List<Edge>> toTranslate, String firstBar) {
		List<List<Edge>> translated = new ArrayList<List<Edge>>();
		List<String> nodesBarFicOne;
		List<String> nodesBarFicTwo;
		Edge newEdge;
		String firstNode = firstBar;
		for (List<Edge> edgeList : toTranslate) {
			List<Edge> newEdgeList = new ArrayList<Edge>();
			for (Edge edgeFic : edgeList) {
				nodesBarFicOne = nodes.get(edgeFic.getBar1()).getCompleteGraphNodesKeys();
				nodesBarFicTwo = nodes.get(edgeFic.getBar2()).getCompleteGraphNodesKeys();
				if (nodesBarFicOne.size() == 1 && nodesBarFicTwo.size() == 1) {
					newEdge = new Edge(nodesBarFicOne.get(0), nodesBarFicTwo.get(0));
					ReducedEdge reducedEdge = (ReducedEdge) getEdge(edgeFic.getBar1(), edgeFic.getBar2());
					Path completePath = reducedEdge.getPathWithComponents(edgeFic.getComponents().get(0),
							nodesBarFicOne.get(0), nodesBarFicTwo.get(0));
					newEdge.addComponnents(completePath);
					newEdgeList.add(newEdge);
					firstNode = nodesBarFicTwo.get(0);
				} else if (nodesBarFicOne.size() == 1 && nodesBarFicTwo.size() > 1) {
					ReducedEdge reducedEdge = (ReducedEdge) getEdge(edgeFic.getBar1(), edgeFic.getBar2());
					String barTwo = reducedEdge.getBarTwoCompleteGraph(nodesBarFicOne.get(0));
					Path completePath = reducedEdge.getPathWithComponents(edgeFic.getComponents().get(0),
							nodesBarFicOne.get(0), barTwo);

					ReducedNode reduceNode = getNode(edgeFic.getBar2());
					List<Edge> pathInFicNodeTwo = reduceNode.getPathCompleteGraph(barTwo);

					newEdge = new Edge(nodesBarFicOne.get(0), barTwo);
					newEdge.addComponnents(completePath);
					newEdgeList.add(newEdge);
					for (Edge edgeCompleteGraph : pathInFicNodeTwo) {
						newEdge = new Edge(edgeCompleteGraph.getBar1(), edgeCompleteGraph.getBar2());
						newEdge.addComponnents(edgeCompleteGraph.getComponents().get(0));
						newEdgeList.add(newEdge);
					}
					firstNode = pathInFicNodeTwo.get(pathInFicNodeTwo.size() - 1).getBar2();
				} else if (nodesBarFicOne.size() > 1 && nodesBarFicTwo.size() == 1) {
					ReducedEdge reducedEdge = (ReducedEdge) getEdge(edgeFic.getBar1(), edgeFic.getBar2());
					String barOne = reducedEdge.getBarTwoCompleteGraph(nodesBarFicTwo.get(0));
					Path completePath = reducedEdge.getPathWithComponents(edgeFic.getComponents().get(0), barOne,
							nodesBarFicTwo.get(0));
					if (!barOne.equals(firstNode)) {
						ReducedNode reduceNode = getNode(edgeFic.getBar1());
						List<Edge> pathInFicNodeOne = reduceNode.getPathCompleteGraph(firstNode);
						for (Edge edgeCompleteGraph : pathInFicNodeOne) {
							newEdge = new Edge(edgeCompleteGraph.getBar1(), edgeCompleteGraph.getBar2());
							newEdge.addComponnents(edgeCompleteGraph.getComponents().get(0));
							newEdgeList.add(newEdge);
						}
					}
					newEdge = new Edge(barOne, nodesBarFicTwo.get(0));
					newEdge.addComponnents(completePath);
					newEdgeList.add(newEdge);
					firstNode = nodesBarFicTwo.get(0);
				} else {
					ReducedEdge reducedEdge = (ReducedEdge) getEdge(edgeFic.getBar1(), edgeFic.getBar2());

					ReducedNode reduceNodeOne = getNode(edgeFic.getBar1());
					List<Edge> pathInFicNodeOne = reduceNodeOne.getPathCompleteGraph(firstNode);
					for (Edge edgeCompleteGraph : pathInFicNodeOne) {
						newEdge = new Edge(edgeCompleteGraph.getBar1(), edgeCompleteGraph.getBar2());
						newEdge.addComponnents(edgeCompleteGraph.getComponents().get(0));
						newEdgeList.add(newEdge);
					}
					String barTwo = reducedEdge.getBarTwoCompleteGraph(reduceNodeOne.getLastNode());
					Path completePath = reducedEdge.getPathWithComponents(edgeFic.getComponents().get(0),
							reduceNodeOne.getLastNode(), barTwo);
					newEdge = new Edge(reduceNodeOne.getLastNode(), barTwo);
					newEdge.addComponnents(completePath);
					newEdgeList.add(newEdge);

					ReducedNode reduceNodeTwo = getNode(edgeFic.getBar2());
					List<Edge> pathInFicNodeTwo = reduceNodeTwo.getPathCompleteGraph(barTwo);
					for (Edge edgeCompleteGraph : pathInFicNodeTwo) {
						newEdge = new Edge(edgeCompleteGraph.getBar1(), edgeCompleteGraph.getBar2());
						newEdge.addComponnents(edgeCompleteGraph.getComponents().get(0));
						newEdgeList.add(newEdge);
					}
					firstNode = pathInFicNodeTwo.get(pathInFicNodeTwo.size() - 1).getBar2();
				}
			}
			translated.add(newEdgeList);
		}

		return translated;

	}

}