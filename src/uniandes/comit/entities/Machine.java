package uniandes.comit.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import uniandes.comit.manager.DataManagerOpenHardware;
import uniandes.comit.manager.DataManagerPerfmon;
import uniandes.comit.manager.DataManagerPowerGadget;
import uniandes.comit.manager.DataManagerSigar;

public class Machine {
	
	private String name;
	TreeMap<Date, MachineData> data = new TreeMap<Date, MachineData>();	

	public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);

	public Machine(String nName){
		name = nName;		
	}
	public void evaluateMachine(final int time, final Date initialDate, final Date endDate) throws IOException, ParseException{	
		Date tempDate = new Date(initialDate.getTime());
		while (tempDate.before(endDate)){
			System.out.println(name+" - "+tempDate);
			
			Date flag = new Date();
			String initDate = (tempDate.getYear()+1900)+"-"+((tempDate.getMonth()+1)>9?(tempDate.getMonth()+1):"0"+(tempDate.getMonth()+1))+"-"+(tempDate.getDate()<10?"0"+tempDate.getDate():tempDate.getDate());
			Date dInitDate = df.parse(initDate+" 07:01:00:000");
			Date dEndDate = df.parse(initDate+" 22:00:01:000");
			
			TreeMap<Date, MachineData> dataTemp = new TreeMap<Date, MachineData>();
			
			while(dInitDate.before(dEndDate)){
				MachineData mData = new MachineData();
				mData.date = (Date) dInitDate.clone();
				mData.machine = name;
				
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
								
				dataTemp.put((Date) dInitDate.clone(), mData);
				dInitDate.setMinutes(dInitDate.getMinutes()+time);			
			}	
			
			DataManagerOpenHardware ohManager = new DataManagerOpenHardware();
			DataManagerPerfmon perfManager = new DataManagerPerfmon();
			DataManagerPowerGadget pgManager = new DataManagerPowerGadget();
			DataManagerSigar sigarManager = new DataManagerSigar();
					
		
			ohManager.fillData(dataTemp, initDate, name, time);
			perfManager.fillData(dataTemp, initDate, name, time);
			pgManager.fillData(dataTemp, initDate, name, time);
			sigarManager.fillData(dataTemp, initDate, name, time);
			Date flag2 = new Date();
			System.out.println(name+" read millis: "+(flag2.getTime()-flag.getTime()));
			try {				
				File folder = new File("results/range_"+time+"/"+initDate);
				if(!folder.exists())folder.mkdir();
			} catch (Exception e) {
				// TODO: handle exception
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("results/range_"+time+"/"+initDate+"/"+name+".csv")));
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
			tempDate.setTime(tempDate.getTime()+(1000*60*60*24));
			
			for(Date d: dataTemp.navigableKeySet()){
				data.put(d, dataTemp.get(d));
			}
			Date flag3 = new Date();
			System.out.println(name+" write millis: "+(flag3.getTime()-flag.getTime()));
		}
		
	}	
}
