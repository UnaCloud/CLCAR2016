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

public class DataManagerPerfmon extends AbstractManager{
	
	
	private SimpleDateFormat dateFormatP = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS", Locale.US);

	public DataManagerPerfmon() {
		super("perfmon");		
	}

	@Override
	protected void calculateDataByFle(File file,TreeMap<Date, MachineData> machineData, int time)
			throws IOException, ParseException {
				
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String line = null;
		Date endTime = machineData.firstKey();

    	while((line = bf.readLine())!=null){			
			
			if(!line.contains("Procesador")){
				//System.out.println("Init "+initTime+" - End "+endTime);
				line = line.replace("\" \"", "0");
				line = line.replaceAll("\"", "");
				String[] data = line.split(",");	
				Date fecha = dateFormatP.parse(data[0]);	
				long millis = fecha.getTime();
				millis = (millis/1000)*1000;
				fecha.setTime(millis);
				fecha.setSeconds(0);
				
				if(fecha.after(endTime)||fecha.compareTo(endTime)==0){					
					endTime = machineData.higherKey(fecha);
					if(endTime==null)break;			
				}	
			
														
				machineData.get(endTime).metricas.get(Variables.CPUL1).addValue(Double.parseDouble(data[1]));
				machineData.get(endTime).metricas.get(Variables.CPUL2).addValue(Double.parseDouble(data[2]));
				machineData.get(endTime).metricas.get(Variables.CPUL3).addValue(Double.parseDouble(data[3]));
				machineData.get(endTime).metricas.get(Variables.CPUL4).addValue(Double.parseDouble(data[4]));
				machineData.get(endTime).metricas.get(Variables.CPUL5).addValue(Double.parseDouble(data[5]));
				machineData.get(endTime).metricas.get(Variables.CPUL6).addValue(Double.parseDouble(data[6]));
				machineData.get(endTime).metricas.get(Variables.CPUL7).addValue(Double.parseDouble(data[7]));
				machineData.get(endTime).metricas.get(Variables.CPUL8).addValue(Double.parseDouble(data[8]));
			}
		}	 
		bf.close();
	}

}
 