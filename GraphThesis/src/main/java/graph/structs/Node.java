package graph.structs;

import java.util.ArrayList;
import java.util.List;

import cim.Model;

public class Node {
	protected List<Path> paths;
	
	public Node() {
		paths = new ArrayList<Path>();
	}
	
	public void addPath(Path newPath) {
		paths.add(newPath);
	}
	public List<Path> getPaths(){
		return paths;
	}
	public void setPath(List<Path> newPath) {
		paths=newPath;
	}
	@Override
	public String toString() {
		return "<" + paths + ">";
	}

	public boolean hasGenerator(Model model) {
		
		int j = 0;
		boolean has = false;
		while (j<paths.size() && !has) {
			if (paths.get(j).hasGenerator(model)) {
				has=true;
			}
			j= j+1;
		}
		return has;
	}
	
	public boolean hasCharge(Model model) {
		int j = 0;
		boolean has = false;
		while (j<paths.size() && !has) {
			if (paths.get(j).hasCharge(model)) {
				has=true;
			}
			j= j+1;
		}
		return has;
	}
}
