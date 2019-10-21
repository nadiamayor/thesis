package graph.structs;

import java.util.ArrayList;
import java.util.List;

public class ReducedEdge extends Edge {
	
	protected List<Edge> completeGraphEdges;

	public ReducedEdge(String barOne, String barTwo) {
		super(barOne, barTwo);
		completeGraphEdges=new ArrayList<Edge>();
		// TODO Auto-generated constructor stub
	}
	public void addCompleteGraphEdge(Edge edge) {
		completeGraphEdges.add(edge);
	}
	
	public Edge getCompleteEdge(String barOne,String barTwo) {
		
		return completeGraphEdges.get(getIndex(barOne,barTwo));
	}
	
	public Path getPathWithComponents(Path shortPath,String barOne,String barTwo) {
		
		List<Path> paths= completeGraphEdges.get(getIndex(barOne,barTwo)).getComponents();
		Path completePath= new Path(new ArrayList<String>());
	
		for(Path path:paths){
			if (path.hasComponent(shortPath)) {
				completePath= new Path(path.getcomponents());
				break;
			}
		}
		return completePath;
	}
	
	public int getIndex(String barOne, String barTwo) {
		int j = 0;
		while (!completeGraphEdges.get(j).existMatch(barOne, barTwo) && j < completeGraphEdges.size()) {
			j = j + 1;
		}
		return j;
	}
	public String getBarTwoCompleteGraph(String barOne) {

		if (completeGraphEdges.get(0).getBar1().equals(barOne))
			return completeGraphEdges.get(0).getBar2();
		else
			return completeGraphEdges.get(0).getBar1();
	}
	
}
