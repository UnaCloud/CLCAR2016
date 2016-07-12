package uniandes.comit.entities;
import java.util.ArrayList;


public class Casilla {
	ArrayList<Double> values = new ArrayList<Double>();
	String metricName;
	
	private Double mean = null;
	private Double variance = null;
	private Double stdDeviation = null;
	private Double variability = null;
	
	public Casilla(String metric){
		this.metricName = metric;
	}
	
	public void addValue(double value){
		values.add(value);
	}
	
	public double getMean(){
		if(values.size()==0)return -1;
		if(mean!=null)return mean;
		double total = 0;
		for(Double value: values)total+=value;
		mean= total/values.size();
		return mean;
	}
	
	public double getVariance(){
		if(variance!=null)return variance;
        double temp = 0;
        for(Double value : values)
            temp += (mean-value)*(mean-value);
        variance = temp/values.size();
		return variance;
	}
	
	public double getStdDeviation(){
		if(stdDeviation!=null)return stdDeviation;
		stdDeviation =  Math.sqrt(getVariance());
		return stdDeviation;
	}
	
	public double getVariability(){
		if(variability!=null)return variability;
		variability = getStdDeviation()/getMean();
		return variability;
	}
	
	@Override
	public String toString(){
		if(values.size()==0)return values.size()+"|N/A|N/A";
		return values.size()+"|"+getMean()+"|"+getVariability();
	}
	
	public String getLastValue(){
		if(values.size()==0)return "N/A";
		return values.get(values.size()-1)+"";
	}
}
