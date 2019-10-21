package graph.graphs;

import java.util.ArrayList;
import java.util.HashMap;

public class IdsTranslator {
	// Mapa de nombre de barra real a id
	private HashMap<String, Integer> mapaIds;
	// Mapa de id a nombre de barra ficticio
	private HashMap<Integer, String> mapaNros;
	private UnionFind uf;

	public IdsTranslator(ArrayList<String> ids) {
		 mapaIds = new HashMap<String, Integer>();
		 mapaNros = new HashMap<Integer, String>();
		
		uf = new UnionFind(ids.size());
		for (int i = 0; i < ids.size(); i++) {
			mapaIds.put(ids.get(i), i);
			mapaNros.put(i, ids.get(i) + "_Fic");
		}
	}

	public void unirIds(String idBarra1, String idBarra2) {
		int nro1 = mapaIds.get(idBarra1);
		int nro2 = mapaIds.get(idBarra2);
		uf.union(nro1, nro2);
	}

	public String getIdTranslated(String id) {
		int nro1 = mapaIds.get(id);
		return mapaNros.get(uf.find(nro1));
	}

	public int getInt(String id) {
		return uf.find(mapaIds.get(id));
	}

	public void print() {
		for (String idBarra : mapaIds.keySet()) {
			// System.out.println(idBarra + " -> " +
			// uf.find(mapaIds.get(idBarra)));
			System.out.println(idBarra + " -> " + getIdTranslated(idBarra));
		}
	}

	public ArrayList<String> getBarsFromTranslation(String idBarraTraducida) {

		ArrayList<String> lista = new ArrayList<String>();
		for (int intKey : mapaNros.keySet()) {
			if (mapaNros.get(intKey).equals(idBarraTraducida)) {

				for (String idBarra : mapaIds.keySet()) {
					int padre = uf.find(mapaIds.get(idBarra));
					if (intKey == padre)
						lista.add(idBarra);
				}
			}
		}
		return lista;
	}

}
