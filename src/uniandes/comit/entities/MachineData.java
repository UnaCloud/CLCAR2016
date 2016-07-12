package uniandes.comit.entities;
import java.util.Date;
import java.util.TreeMap;


public class MachineData {
	public String machine;
	public Date date;
	public TreeMap<String,Casilla> metricas = new TreeMap<String,Casilla>();
	
	public MachineData clone(){
		MachineData copy = new MachineData();
		copy.machine = machine;
		copy.metricas = metricas;
		copy.date = date;
		return copy;
	}
	
	public void print(){			
	
		String data = "";
		for(Casilla casilla: metricas.values())
			data += ","+casilla.toString();
		System.out.println(machine+","+date+data);
			
	}
}
