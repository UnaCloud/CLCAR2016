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

public class DataManagerPowerGadget extends AbstractManager{

	private String dateFile;

	private SimpleDateFormat dateFormatP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
	
	public DataManagerPowerGadget() {
		super("powerGadget");
	}
	
	@Override
	public void fillData(TreeMap<Date, MachineData> data, String day,
			String machine) throws IOException, ParseException {
		dateFile = day;
		//String[] dateParts = dateFile.split("-");
		//dateFile = dateParts[1]+"/"+dateParts[2]+"/"+dateParts[0];
		super.fillData(data, day, machine);
	}

	@Override
	protected void calculateDataByFle(File file,TreeMap<Date, MachineData> machineData)
			throws IOException, ParseException {
	
		BufferedReader bf = new BufferedReader(new FileReader(file));
		String line = null;
		Date endTime = machineData.firstKey();
		
		while((line = bf.readLine())!=null){			
			
			if(!line.startsWith("System")&&!line.startsWith("Total")&&!line.startsWith("Measured")&&!line.startsWith("Cumulative")&&!line.startsWith("Average")&&!line.isEmpty()){
				//System.out.println("Init "+initTime+" - End "+endTime);
				String[] data = line.split(",");	
				Date fecha = dateFormatP.parse(dateFile+" "+data[0]);	
				long millis = fecha.getTime();
				millis = (millis/1000)*1000;
				fecha.setTime(millis);
				fecha.setSeconds(0);
				
				if(fecha.after(endTime)||fecha.compareTo(endTime)==0){					
					endTime = machineData.higherKey(fecha);
					if(endTime==null)break;			
				}										
				machineData.get(endTime).metricas.get(Variables.PPOWER).addValue(Double.parseDouble(data[3]));
			}
		}	 
		bf.close();
	}

}
