package uniandes.comit.entities;

import java.util.ArrayList;
import java.util.Date;

public class ExecutionThread extends Thread{

	private ArrayList<Machine> machines = new ArrayList<Machine>();
	private int time;
	private Date dayDate;
	
	public void addMachine(Machine machine){
		machines.add(machine);
	}
	
	public void setConfig(int time, Date dayDate){
		this.time = time;
		this.dayDate = dayDate;
	}
	
	public ArrayList<Machine> getMachines() {
		return machines;
	}
	
	public void setMachines(ArrayList<Machine> machines) {
		this.machines = machines;
	}
	
	@Override
	public void run() {
		try {
			for(Machine machine:machines)
				machine.evaluateMachine(time, dayDate);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
	
}
