package uniandes.comit.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;


public class Laboratory {

	public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
	
	private ExecutionThread[] executions;
	private int quantity;

	private static String[] machines = {"ISC201","ISC202","ISC203",
		"isc204","ISC205","ISC206","ISC207","ISC208","ISC209","ISC210",
		"ISC211","ISC212","ISC213","ISC214","ISC215","ISC216","ISC217","ISC218","ISC219","ISC220",
		"ISC221","ISC222","ISC223","ISC224","ISC225","ISC226","ISC227","ISC228","ISC229","ISC230",
		"ISC301","ISC302","ISC303","isc304","ISC305","ISC306","ISC307","ISC308","ISC309","ISC310",
		"ISC311","ISC312","ISC313","ISC314","ISC315","ISC316","ISC317","ISC318","ISC319","ISC320",
		"ISC321","ISC322","ISC323","isc324","ISC325","ISC326","ISC327","ISC328","ISC329","ISC330"};
	
	public Laboratory(int threads){
		executions = new ExecutionThread[threads];
		double rs = ((double)machines.length)/((double)threads);
		quantity = (int) Math.ceil(rs);
		System.out.println("Total: "+machines.length+" - Grupos: "+threads+" - de: "+quantity);
		for(int i = 0; i<threads; i++){
			executions[i] = new ExecutionThread();
			for(int j = i*quantity; j < (i+1)*quantity && j < machines.length; j++){
				executions[i].addMachine(new Machine(machines[j]));
			}
		}	
		for(ExecutionThread exe:executions){
			System.out.print(exe.getMachines().size()+" - ");
		}
	}
	
	public void fillLab(Date initialDate, Date endDate, int time) throws IOException, ParseException, InterruptedException{
		Date tempDate = new Date(initialDate.getTime());
		while (tempDate.before(endDate)){
			
			for(int i = 0; i<executions.length; i++){
				ExecutionThread newExe = new ExecutionThread();
				newExe.setMachines(executions[i].getMachines());
				for(Machine machine: newExe.getMachines())
					machine.data = new TreeMap<Date, MachineData>();
				executions[i] = newExe;
			}	
						
			Date start = new Date();
			System.out.println("START: " +start);
			for(ExecutionThread execution: executions){
				execution.setConfig(time, tempDate);
				execution.start();
			}
			for(ExecutionThread execution: executions)execution.join();
			evaluateLab(tempDate, time);
			Date end = new Date();
			System.out.println("END: " +end);
			System.out.println("Total -> "+(end.getTime()-start.getTime()));
			tempDate.setTime(tempDate.getTime()+(1000*60*60*24));
		}
	}
	
	public void evaluateLab(Date dayDate, int time) throws ParseException, IOException{
		
		ArrayList<Machine> machines = new ArrayList<Machine>();
		for(ExecutionThread execution: executions)
			machines.addAll(execution.getMachines());
		
		String date = (dayDate.getYear()+1900)+"-"+((dayDate.getMonth()+1)>9?(dayDate.getMonth()+1):"0"+(dayDate.getMonth()+1))+"-"+(dayDate.getDate()<10?"0"+dayDate.getDate():dayDate.getDate());
		Date dInitDate = df.parse(date+" 07:01:00:000");
		Date dEndDate = df.parse(date+" 22:00:01:000");
		
		TreeMap<Date, MachineData> dataTemp = new TreeMap<Date, MachineData>();				
		
		while(dInitDate.before(dEndDate)){
			MachineData mData = new MachineData();
			mData.date = (Date) dInitDate.clone();
			mData.machine = "waira";			
					
			mData.metricas.put(Variables.CPU1, new Casilla(Variables.CPU1));
			mData.metricas.put(Variables.CPU2, new Casilla(Variables.CPU2));
			mData.metricas.put(Variables.CPU3, new Casilla(Variables.CPU3));
			mData.metricas.put(Variables.CPU4, new Casilla(Variables.CPU4));
			mData.metricas.put(Variables.CPUTOTAL, new Casilla(Variables.CPUTOTAL));
			mData.metricas.put(Variables.RAMUSED, new Casilla(Variables.RAMUSED));
			mData.metricas.put(Variables.CPUL1, new Casilla(Variables.CPUL1));
			mData.metricas.put(Variables.CPUL2, new Casilla(Variables.CPUL2));
			mData.metricas.put(Variables.CPUL3, new Casilla(Variables.CPUL3));
			mData.metricas.put(Variables.CPUL4, new Casilla(Variables.CPUL4));
			mData.metricas.put(Variables.CPUL5, new Casilla(Variables.CPUL5));
			mData.metricas.put(Variables.CPUL6, new Casilla(Variables.CPUL6));
			mData.metricas.put(Variables.CPUL7, new Casilla(Variables.CPUL7));
			mData.metricas.put(Variables.CPUL8, new Casilla(Variables.CPUL8));
			mData.metricas.put(Variables.PPOWER, new Casilla(Variables.PPOWER));
			mData.metricas.put(Variables.NETRX, new Casilla(Variables.NETRX));
			mData.metricas.put(Variables.NETTX, new Casilla(Variables.NETTX));
			mData.metricas.put(Variables.UPTIME, new Casilla(Variables.UPTIME));
			mData.metricas.put(Variables.HDUSED, new Casilla(Variables.HDUSED));					

			for(Machine machine: machines){
				MachineData machineData = machine.data.get(dInitDate);
				for(String key: machineData.metricas.descendingKeySet()){
					if(machineData.metricas.get(key).getMean()!=-1){
						mData.metricas.get(key).addValue(machineData.metricas.get(key).getMean());
					}						
				}
			}
			
			dataTemp.put((Date) dInitDate.clone(), mData);
			dInitDate.setMinutes(dInitDate.getMinutes()+time);			
		}	
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("results/range_"+time+"/"+date+"/Waira.csv")));
		bw.append("Date,"+Variables.CPU1
				+","+Variables.CPU2
				+","+Variables.CPU3
				+","+Variables.CPU4
				+","+Variables.CPUTOTAL
				+","+Variables.RAMUSED
				+","+Variables.CPUL1
				+","+Variables.CPUL2
				+","+Variables.CPUL3
				+","+Variables.CPUL4
				+","+Variables.CPUL5
				+","+Variables.CPUL6
				+","+Variables.CPUL7
				+","+Variables.CPUL8
				+","+Variables.PPOWER
				+","+Variables.NETRX
				+","+Variables.NETTX
				+","+Variables.UPTIME
				+","+Variables.HDUSED);
		bw.newLine();
		//System.out.println("****************************");
		for(MachineData mData: dataTemp.values()){
		//	mData.print();
			bw.append(mData.date
					+","+mData.metricas.get(Variables.CPU1).toString()
					+","+mData.metricas.get(Variables.CPU2).toString()
					+","+mData.metricas.get(Variables.CPU3).toString()
					+","+mData.metricas.get(Variables.CPU4).toString()
					+","+mData.metricas.get(Variables.CPUTOTAL).toString()
					+","+mData.metricas.get(Variables.RAMUSED).toString()
					+","+mData.metricas.get(Variables.CPUL1).toString()
					+","+mData.metricas.get(Variables.CPUL2).toString()
					+","+mData.metricas.get(Variables.CPUL3).toString()
					+","+mData.metricas.get(Variables.CPUL4).toString()
					+","+mData.metricas.get(Variables.CPUL5).toString()
					+","+mData.metricas.get(Variables.CPUL6).toString()
					+","+mData.metricas.get(Variables.CPUL7).toString()
					+","+mData.metricas.get(Variables.CPUL8).toString()
					+","+mData.metricas.get(Variables.PPOWER).toString()
					+","+mData.metricas.get(Variables.NETRX).getLastValue()
					+","+mData.metricas.get(Variables.NETTX).getLastValue()
					+","+mData.metricas.get(Variables.UPTIME).getLastValue()
					+","+mData.metricas.get(Variables.HDUSED).toString());
			bw.newLine();
		}
		bw.close();	
	}

}
