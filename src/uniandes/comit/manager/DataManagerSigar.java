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

public class DataManagerSigar extends AbstractManager{
	
	private SimpleDateFormat dateFormatP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

	public DataManagerSigar() {
		super("sigar");
	}

	@Override
	protected void calculateDataByFle(File file,TreeMap<Date, MachineData> machineData, int time)
			throws IOException, ParseException {
	
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String line = null;
		Date endTime = machineData.firstKey();
	
		while((line = bf.readLine())!=null){			
			
			if(!line.isEmpty()&&!line.startsWith("MonitorInitialReport")&&!line.startsWith("Timestamp")){
				//System.out.println("Init "+initTime+" - End "+endTime);
				line = line.replace("MonitorReport [Timestamp=", "");
				line = line.replace("]", "");
				line = line.replace(Variables.NETRX+"=", "");//14
				line = line.replace(Variables.NETTX+"=", "");//15
				line = line.replace(Variables.UPTIME+"=", "");//3
				line = line.replace(Variables.HDUSED+"=", "");//13
				//System.out.println(line);
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
//				if(fecha.getHours()==17&&fecha.getMinutes()==2){
//					System.out.println(endTime+" - "+fecha);
//				}				
				machineData.get(endTime).metricas.get(Variables.NETRX).addValue(Double.parseDouble(data[14]));
				machineData.get(endTime).metricas.get(Variables.NETTX).addValue(Double.parseDouble(data[15]));
				machineData.get(endTime).metricas.get(Variables.UPTIME).addValue(Double.parseDouble(data[3]));
				machineData.get(endTime).metricas.get(Variables.HDUSED).addValue(Double.parseDouble(data[13]));
			}
		}	 
		bf.close();
	}

}
