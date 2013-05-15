package RVC.plugins.ere;

import java.io.*;
import java.util.*;
import RVC.LogicException;
import RVC.CMonGenData;
import RVC.Main;
import RVC.RVCsyntax.*;
import RVC.plugins.*;

public class EREPlugin extends LogicPlugin {
	
	public CMonGenType process(CMonGenType logicInputXML) throws LogicException {
		String logicStr = logicInputXML.getProperty().getFormula();
		String eventsStr = logicInputXML.getEvents();
		eventsStr.replaceAll("\\s+", " ");

		String[] eventStrings = eventsStr.split(" ");
		Symbol[] events = new Symbol[eventStrings.length];
		for (int i = 0; i < eventStrings.length; ++i) {
			events[i] = Symbol.get(eventStrings[i]);
		}

		EREParser ereParser = EREParser.parse(logicStr);
		ERE ere = ereParser.getERE();

		FSM fsm = FSM.get(ere, events);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		fsm.print(ps);

		String output = os.toString();

		String logic = "fsm";
		String formula = output;

		CMonGenType logicOutputXML = logicInputXML;
		logicOutputXML.getProperty().setLogic(logic);
		logicOutputXML.getProperty().setFormula(formula);

		return logicOutputXML;
	}
	
	static protected EREPlugin plugin = new EREPlugin();
	
	public static void main(String[] args) {

		try {
			// Parse Input
			CMonGenData logicInputData = new CMonGenData(System.in);

			// use plugin main function
			if(plugin == null){
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
