package uniandes.comit.manager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.TreeMap;

import uniandes.comit.entities.MachineData;

public abstract class AbstractManager {

	private String name;
	
	public AbstractManager(String nName){
		this.name = nName;//openHardware
	}
	
	public void fillData(TreeMap<Date, MachineData> data, String day, String machine, int time) throws IOException, ParseException{
		
		File folder = new File("Z:\\Monitoreo\\UnaCloudMonitor\\"+day+"\\"+machine+"\\"+name);
		
		if(!folder.exists())
			return;
		
		TreeMap<String,File> cleanFiles = new TreeMap<String,File>();
		
		for(File ohFile: folder.listFiles()){
			String fileName = ohFile.getName().substring(0, ohFile.getName().length()-28);
			cleanFiles.put(fileName, ohFile); 		
		}
		for(File file: cleanFiles.values()){
			try {
				calculateDataByFle(file,data, time);	
			} catch (ParseException e) {
				System.err.println(machine+"-"+file);
				throw e;
			}			
		}
//		for(MachineData singleData:data.values()){
//			singleData.print();			 
//		}
	}
	
	protected abstract void calculateDataByFle(File file,  TreeMap<Date,MachineData> machineData, int time) throws IOException, ParseException;
}
