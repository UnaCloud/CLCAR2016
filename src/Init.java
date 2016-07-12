import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import uniandes.comit.entities.Laboratory;
import uniandes.comit.entities.Variables;


public class Init {
	
	private static String initialDate = "2015-10-19 00:00:00:000";
	private static String endDate = "2015-11-16 00:00:00:000";
	//private static String endDate = "2015-10-20 00:00:00:000";
	private static int[] timeRange = {1,15,30};	

	public SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
	
//	private static String[] machines = {"ISC207","ISC208","ISC209","ISC210"};	
	public static void main(String[] args) throws ParseException, IOException, InterruptedException {
		//Son n días
		//Recorrer día a día
		//Recorrer máquina a máquina por día
		//Generar un archivo por día
		//Generar promedio de CPU, _RAM, espacio en disco, Processor power por x tiempo
		//Foto en el momento NetRX, NetTX, Uptime
		// - Algoritmo  (1, 15, 30, 60)
		//		-Fecha (3 semanas, ojala consecutivas
	    //			-Máquina
		//				- archivo csv -- promedio por algoritmo
		//String initDate = "2015-10-27";
		new Init();
	}	
	
	public Init()throws ParseException, IOException, InterruptedException{
		File folder = new File("results");
		folder.mkdir();		
		for(int time: timeRange){
			Laboratory waira1 = new Laboratory(10);
			File folderTime = new File("results/range_"+time);
			folderTime.mkdir();
			waira1.fillLab(df.parse(initialDate), df.parse(endDate), time);	
		}
	}
}
