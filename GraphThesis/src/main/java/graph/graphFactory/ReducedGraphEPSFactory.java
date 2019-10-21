package graph.graphFactory;

import java.util.ArrayList;
import java.util.List;

import org.tenergiaEditor.commons.cim.enums.SwitchStates;

import cim.Model;
import cim.manager.data.respository.ComponentsDataRepository;
import cim.manager.data.results.Result;
import cim.manager.data.results.Row;
import graph.graphs.Graph;
import graph.graphs.IdsTranslator;
import graph.graphs.ReducedGraphEPS;
import graph.graphs.Utils;
import graph.structs.Edge;
import graph.structs.Node;
import graph.structs.Path;
import graph.structs.ReducedEdge;
import graph.structs.ReducedNode;
import names.ResultsNames;
import util.ComponentType;

public class ReducedGraphEPSFactory implements GraphFactory {
	
	private Model model;
	private Graph<Node,Edge> completeGraph;
	private ReducedGraphEPS reduceGraph;
	
	public ReducedGraphEPSFactory(Graph<Node,Edge> graphToReduce) {
		this.completeGraph=graphToReduce;
		model=graphToReduce.getModel();
		

	}
	

	@Override
	public ReducedGraphEPS createGraph() {
		Result barras = ComponentsDataRepository.getInstance().getAllComponentsDataByType(Model.getInstance(),
				ComponentType.Barra);
		ArrayList<String> idsBarras = new ArrayList<String>();
		for (Row row : barras) {
			idsBarras.add(row.getValue(ResultsNames.id));
		}
		reduceGraph = new ReducedGraphEPS(completeGraph.getModel());
		reduceGraph.setIdsTranslator(new IdsTranslator(idsBarras) );
		List<Edge> edges = completeGraph.getEdges();
		int j = 0;
		while (j < edges.size()) {
			List<Path> paths = edges.get(j).getComponents();
			int i = 0;
			while (i < paths.size()) {
				if (closePathWithoutImpedance(paths.get(i))) {
					reduceGraph.getIdsTranslator().unirIds(edges.get(j).getBar1(), edges.get(j).getBar2());
					break;
				}
				i = i + 1;
			}
			j = j + 1;
		}
		for (String idBarra : completeGraph.getBars()) {
			String idBarraFic = reduceGraph.getIdsTranslator().getIdTranslated(idBarra);
			reduceGraph.addNode(idBarraFic, new ReducedNode(model));
		}
		
		//recorrer otra vez los arcos para agregar las relaciones entre las barras que se traducen a una barra fic
		for(Edge edge: completeGraph.getEdges() ) {
			List<Path> paths = edge.getComponents();
			Edge newEdge=new Edge(edge.getBar1(),edge.getBar2());
			int i = 0;
			while (i < paths.size()) {
				if (closePathWithoutImpedance(paths.get(i))) {
					String barFic = reduceGraph.getIdsTranslator().getIdTranslated(edge.getBar1());
					newEdge.addComponnents(paths.get(i));
					reduceGraph.addCompleteEdgeInNode(newEdge, barFic,paths.get(i));
					break;
				}
				i = i + 1;
			}
		}
		
		
		j = 0;
		boolean addPathWhithoutSwitches;
		while (j < edges.size()) {
			String barId1= edges.get(j).getBar1();
			String barId2 = edges.get(j).getBar2();

			String barOneFic = reduceGraph.getIdsTranslator().getIdTranslated(barId1);
			String barTwoFic = reduceGraph.getIdsTranslator().getIdTranslated(barId2);
			addPathWhithoutSwitches=false;
			List<Path> paths = edges.get(j).getComponents();
			List<Path> newPaths= new ArrayList<Path>();
			for (Path path : paths) {
				Path pathWhithoutSwitches = copyWithoutSwitches(path);
				if (pathWhithoutSwitches.getPathSize() > 0) {
					reduceGraph.addEdge(pathWhithoutSwitches, new ReducedEdge(barOneFic, barTwoFic));
					newPaths.add(path);
					addPathWhithoutSwitches=true;
				}
			}
			//aca guardar en el edge como estaban comunicados esas dos barras con switches
			if(addPathWhithoutSwitches) {
				reduceGraph.addCompleteEdge(barOneFic, barTwoFic, edges.get(j),newPaths);
			}
			
			j = j + 1;

		}
		for (String barId : completeGraph.getBars()) {
			String barFicId = reduceGraph.getIdsTranslator().getIdTranslated(barId);
			
			List<Path> completePaths= new ArrayList<Path>();
			List<Path> paths = completeGraph.getPathsBar(barId);
			for (Path path : paths) {
				if (closePathWithoutImpedance(path)) {
					reduceGraph.addPathToNode(barFicId, copyWithoutSwitches(path));
					completePaths.add(path);		
				}
			}
			
			Node completeNode= new Node();
			completeNode.setPath(completePaths);
			reduceGraph.addCompleteNode(barFicId,barId,completeNode);
				
		}

		return reduceGraph;
	}
	private boolean closePathWithoutImpedance(Path path) {
		for (String idComponent : path) {
			if (ComponentsDataRepository.getInstance().isSwitch(model, idComponent)) {

				if (!Utils.getSwitchState(idComponent, model).equals(SwitchStates.close)) {
					return false;
				}

			} else {
				if (isEndOfPath(idComponent))
					return true;
				else
					return false;
			}
		}
		return true;
	}
	private boolean isEndOfPath(String idComponent) {
		ComponentType type = ComponentsDataRepository.getInstance().getType(model, idComponent);
		return type.equals(ComponentType.Generador) || type.equals(ComponentType.GeneradorSlack)
				|| type.equals(ComponentType.Carga) || type.equals(ComponentType.Capacitor)
				|| type.equals(ComponentType.Tierra); // fin de camino (Tierra?)
	}
	
	private Path copyWithoutSwitches(Path path) {
		ArrayList<String> withoutSwitches = new ArrayList<String>();
		for (String idComponent : path) {
			if (!ComponentsDataRepository.getInstance().isSwitch(model, idComponent))
				withoutSwitches.add(idComponent);
			else {
				if (!Utils.getSwitchState(idComponent, model).equals(SwitchStates.close))
					return new Path(new ArrayList<String>());
			}

		}
		return new Path(withoutSwitches);
	}

}
