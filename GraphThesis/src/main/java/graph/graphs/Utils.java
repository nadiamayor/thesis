package graph.graphs;

import org.tenergiaEditor.commons.cim.enums.SwitchStates;

import cim.Model;
import cim.manager.data.respository.ComponentsDataRepository;
import cim.manager.data.results.Row;
import names.ResultsNames;

public class Utils {

	public static SwitchStates getSwitchState(String idComponent, Model cimModel) {
		Row switchRow = ComponentsDataRepository.getInstance().getComponentDataById(cimModel, idComponent);
		SwitchStates value = SwitchStates.fromString(switchRow.getValue(ResultsNames.normalOpen));
		// Meassurement m =
		// Model.getInstance().getDinamicModel().getMeassurement(idComponent);

		// return (SwitchStates) m.getValue();

		return value;
	}
}
