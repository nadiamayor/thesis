package graph.graphFactory;

import java.util.ArrayList;

import cim.Model;
import cim.manager.data.respository.ComponentsDataRepository;
import cim.manager.data.results.Result;
import cim.manager.data.results.Row;
import graph.graphs.Graph;

import graph.structs.Edge;
import graph.structs.Node;
import graph.structs.Path;
import names.ResultsNames;
import util.ComponentType;

public class GraphEPSFactory implements GraphFactory {
	
	Graph<Node,Edge> graphEPS;
	Model model;
	
	public GraphEPSFactory(Model model) {
		this.model=model;
		graphEPS= new Graph<Node, Edge>(model);
	}

	@Override
	public Graph<Node,Edge> createGraph() {
		Result barras = ComponentsDataRepository.getInstance().getAllComponentsDataByType(model, ComponentType.Barra);

		for (Row rowBarra : barras) {
			String idBarra = rowBarra.getValue(ResultsNames.id);
			graphEPS.addNode(idBarra, new Node());
		}
		for (Row rowBarra : barras) {
			String idBarra = rowBarra.getValue(ResultsNames.id);
			Result connectedComponents = ComponentsDataRepository.getInstance().getConnectedComponents(model, idBarra);
			ArrayList<String> components = new ArrayList<String>();
			for (Row rowComponent : connectedComponents) {
				String idComponent = rowComponent.getValue(ResultsNames.id);
				crearConexiones(idBarra, idComponent, idBarra, components);
			}
		}

		return graphEPS;
	
	}
	
	public void crearConexiones(String idBarra, String idComponent, String idAnterior,
			ArrayList<String> components) {
		ComponentType type = ComponentsDataRepository.getInstance().getType(model, idComponent);
		if (type.equals(ComponentType.Barra)) {
			graphEPS.addEdge( new Path(components),new Edge(idBarra, idComponent));
		} else {
			Result connectedComponents = ComponentsDataRepository.getInstance().getConnectedComponents(model,
					idComponent);
			components.add(idComponent);
			if (connectedComponents.getSize() == 1)
				graphEPS.addPathToNode(idBarra, new Path(components));

			else {
				for (Row row : connectedComponents) {
					if (!row.getValue(ResultsNames.id).equals(idAnterior))
						crearConexiones(idBarra, row.getValue(ResultsNames.id), idComponent,
								components);
				}
			}

			components.remove(components.size() - 1);
		}
	}


}
