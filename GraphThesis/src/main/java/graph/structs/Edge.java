package graph.structs;

import java.util.ArrayList;
import java.util.List;

public class Edge {
	protected List<Path> components;
	protected String bar1;
	protected String bar2;

	public Edge(String barOne, String barTwo) {
		components = new ArrayList<Path>();
		bar1 = barOne;
		bar2 = barTwo;
	}

	public void addComponnents(Path c) {
		if (!existPath(c))
			components.add(c);
	}
	public void setComponents(List<Path> newComponents) {
		components=newComponents;
	}

	public List<Path> getComponents() {
		return components;
	}

	public String getBar1() {
		return bar1;
	}

	public String getBar2() {
		return bar2;
	}

	public boolean existMatch(String b1, String b2) {
		return ((bar1.equals(b1) && bar2.equals(b2)) || (bar1.equals(b2) && bar2.equals(b1)));
	}
	
	

	public boolean existPath(Path nuevo) {
		boolean exist = false;
		int j = 0;
		while ((!exist) && (j < components.size())) {
			if (components.get(j).areEquals(nuevo)) {
				exist = true;
			}
			j = j + 1;
		}
		return exist;
	}

	@Override
	public String toString() {
		return "(" + bar1 + ", " + bar2 + ")" + " : " + components;
	}
}
