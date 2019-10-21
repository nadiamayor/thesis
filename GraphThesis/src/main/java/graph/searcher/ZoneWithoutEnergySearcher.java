package graph.searcher;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import graph.graphs.ReducedGraphEPS;
import graph.structs.Edge;

public class ZoneWithoutEnergySearcher extends Searcher {

	Map<String, Boolean> withEnergy;

	public ZoneWithoutEnergySearcher(ReducedGraphEPS toSearch) {
		super(toSearch);
		Set<String> nodes = graph.getBars();
		withEnergy = new HashMap<String, Boolean>();
		for (String node : nodes) {
			withEnergy.put(node, false);
		}
	}

	@Override
	public void search() {
		Set<String> nodes = graph.getBars();
		for (String node : nodes) {
			if(!visited.get(node)&&graph.nodeHasGenerator(node)) {
				dfs(node, null, null);
			}
		}
		
	}
	@Override
	void dfs(String node, List<Edge> path, String nodeWithGenerator) {
		visited.put(node, true);
		withEnergy.put(node, true);
		List<String> neighbors = graph.getNeighbors(node);
		for (String neighbor: neighbors) {
			if (!visited.get(neighbor)) {
				dfs(neighbor,null,null);
			}
		}
	}
	

	@Override
	public void print() {
		Map<String, Boolean> translatedWithEnergy=((ReducedGraphEPS) graph).translateNodesWithoutEnergy(withEnergy);
		Set<String> hasEnergia = translatedWithEnergy.keySet();
		System.out.println("Nodos si tienen o no energía:");
		for (String nodo : hasEnergia) {
			System.out.println(nodo + ": " + translatedWithEnergy.get(nodo));
		}

	}
	
}
