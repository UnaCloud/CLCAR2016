package uniandes.comit.manager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TreeMap;

import uniandes.comit.entities.MachineData;
import uniandes.comit.entities.Variables;

public class DataManagerOpenHardware extends AbstractManager{
		
	
	private SimpleDateFormat dateFormatOH = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
	
	public DataManagerOpenHardware() {
		super("openHardware");
	}
	
	@Override
	protected void calculateDataByFle(File file, TreeMap<Date, MachineData> machineData)	throws IOException, ParseException {
		
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String line = null;
		Date endTime = machineData.firstKey();		

		while((line = bf.readLine())!=null){			
			line = line.trim();
			if(!line.startsWith(",/intelcpu/")&&!line.startsWith("Time")&&!line.startsWith("\"")&&!line.isEmpty()&&!line.startsWith(" \"")){
				//System.out.println("Init "+initTime+" - End "+endTime);
				//System.out.println(line);
				try {
					String[] data = line.split(",");	
					Date fecha = dateFormatOH.parse(data[0]);				
					fecha.setSeconds(0);
					
					if(fecha.after(endTime)||fecha.compareTo(endTime)==0){					
						endTime = machineData.higherKey(fecha);
						if(endTime==null)break;			
					}	
//					if(fecha.getHours()==17&&fecha.getMinutes()==2){
//						System.out.println(endTime+" - "+fecha);
//					}			
					
					machineData.get(endTime).metricas.get(Variables.CPU1).addValue(Double.parseDouble(data[1]));
					machineData.get(endTime).metricas.get(Variables.CPU2).addValue(Double.parseDouble(data[2]));
					machineData.get(endTime).metricas.get(Variables.CPU3).addValue(Double.parseDouble(data[3]));
					machineData.get(endTime).metricas.get(Variables.CPU4).addValue(Double.parseDouble(data[4]));
					machineData.get(endTime).metricas.get(Variables.CPUTOTAL).addValue(Double.parseDouble(data[5]));
					machineData.get(endTime).metricas.get(Variables.RAMUSED).addValue(Double.parseDouble(data[21]));
				} catch (ParseException e) {
					System.err.println(line);
					throw e;
				}
			
			}
		}	 
		bf.close();
	}

}
