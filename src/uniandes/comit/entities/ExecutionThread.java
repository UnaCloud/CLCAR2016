package uniandes.comit.entities;

import java.util.ArrayList;
import java.util.Date;

public class ExecutionThread extends Thread{

	private ArrayList<Machine> machines = new ArrayList<Machine>();
	private int time;
	private Date initialDate, endDate;
	
	public void addMachine(Machine machine){
		machines.add(machine);
	}
	
	public void setConfig(int time, Date initialDate, Date endDate){
		this.time = time;
		this.initialDate = initialDate;
		this.endDate = endDate;
	}
	
	public ArrayList<Machine> getMachines() {
		return machines;
	}
	
	@Override
	public void run() {
		try {
			for(Machine machine:machines)
				machine.evaluateMachine(time, initialDate, endDate);
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}	
	
}
