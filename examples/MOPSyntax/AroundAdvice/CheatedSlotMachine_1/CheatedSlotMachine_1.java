package casino;

import casino.mop.CheatedSlotMachineRuntimeMonitor;

public class CheatedSlotMachine_1 {
	public static void main(String[] args){
		SlotMachine machine = new SlotMachine();
		for (int i = 0; i < 10 ; ++i) {
			System.out.println("Round " + i);
			machine.insertCoin();
			casino.mop.CheatedSlotMachineRuntimeMonitor.insert_coinEvent(machine);
			machine.push();
			casino.mop.CheatedSlotMachineRuntimeMonitor.push_buttonEvent(machine);
			casino.mop.CheatedSlotMachineRuntimeMonitor.resultEvent(machine);
			if (!CheatedSlotMachineRuntimeMonitor.skipEvent)
				System.out.println(machine.getResult());
		}
	}
}
