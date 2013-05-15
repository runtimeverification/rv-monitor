package cmongen.plugins.fsm;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.io.*;

import javax.xml.bind.*;

import cmongen.LogicException;
import cmongen.CMonGenData;
import cmongen.cmongensyntax.CMonGenType;
import cmongen.plugins.*;

public class FSMPlugin extends LogicPlugin {

	public CMonGenType process(CMonGenType logicInputXML) throws LogicException {

		String logic = logicInputXML.getProperty().getLogic();
		logic = logic.toUpperCase();

		if (!logic.equals("FSM")) {
			throw new LogicException("incorrect logic type: " + logic);
		}

		ArrayList<String> events = new ArrayList<String>();
		ArrayList<String> categories = new ArrayList<String>();

		for (String event : (((String) logicInputXML.getEvents()).trim()).split("\\s+"))
			events.add(event);

		for (String event : (((String) logicInputXML.getCategories()).trim()).split("\\s+"))
			categories.add(event);

		String fsm = logicInputXML.getProperty().getFormula();

		// parse the imput fsm in order to acquire the
		// stateMap, aliases, and state list
		FSMParser fsmParser = FSMParser.parse(fsm);

		// compute the enables
		FSMEnables fsmEnables = new FSMEnables(fsmParser.getStartState(), events, fsmParser.getStates(), categories,
				fsmParser.getAliases(), fsmParser.getStateMap());

		CMonGenType logicOutputXML = logicInputXML;
		logicOutputXML.getMessage().add("done");

		logicOutputXML.setCreationEvents(fsmEnables.creationEvents());
		logicOutputXML.setEnableSets(fsmEnables.toString());
		
		return logicOutputXML;

	}

	static protected FSMPlugin plugin = new FSMPlugin();

	public static void main(String[] args) {

		try {
			// Parse Input
			CMonGenData logicInputData = new CMonGenData(System.in);

			// use plugin main function
			if (plugin == null) {
				throw new LogicException("Each plugin should initiate plugin field.");
			}
			CMonGenType logicOutputXML = plugin.process(logicInputData.getXML());

			if (logicOutputXML == null) {
				throw new LogicException("no output from the plugin.");
			}

			ByteArrayOutputStream logicOutput = new CMonGenData(logicOutputXML).getOutputStream();
			System.out.println(logicOutput);
		} catch (LogicException e) {
			System.out.println(e);
		}

	}

}
