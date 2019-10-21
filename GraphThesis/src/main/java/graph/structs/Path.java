package graph.structs;

import java.util.ArrayList;
import java.util.Iterator;
import cim.Model;
import cim.manager.data.respository.ComponentsDataRepository;

import util.ComponentType;

public class Path implements Iterable<String> {
	ArrayList<String> components;
	
	public Path(ArrayList<String> newComponents) {
		components = new ArrayList<>(newComponents);
	}
	public void setcomponents(ArrayList<String> newComponents) {
		components = newComponents;
	}
	public boolean areEquals(Path newPath) {
		if (components.containsAll(newPath.getcomponents()) && newPath.getcomponents().containsAll(components))
			return true;
		else
			return false;
	}
	public ArrayList<String> getcomponents(){
		return components;
	}
	
	public int getPathSize() {
		return components.size();
	}
	
	public String componentAt(int i) {
		return components.get(i);
	}
	@Override
	public String toString() {
		return "{" + components + "}";
	}
	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return components.iterator();
		
	}
	public boolean hasGenerator(Model model) {
		
		int j = 0;
		boolean has = false;
		while(j<components.size() && !has) {
			ComponentType type = ComponentsDataRepository.getInstance().getType(model, components.get(j));
			if (type.equals(ComponentType.Generador) || type.equals(ComponentType.GeneradorSlack)) {
				has= true;
			}
			j = j +1;
		}
		return has;
	}
	public boolean hasCharge(Model model) {
		int j = 0;
		boolean has = false;
		while(j<components.size() && !has) {
			ComponentType type = ComponentsDataRepository.getInstance().getType(model, components.get(j));
			if (type.equals(ComponentType.Carga)){
				has= true;
			}
			j = j +1;
		}
		return has;
	}
	public boolean hasComponent(Path path) {
		Boolean has= false;
		int j=0;
		while (j<components.size() && !has) {
			for(String component:path.getcomponents() ) {
				if (components.get(j).equals(component)) {
					has=true;
				}
			}
			j=j+1;
		}
		return has;
	}

}
