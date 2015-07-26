package sim.components.basic;

import java.util.HashMap;

public class Job {
	private HashMap<String,String> _property;

	public Job(){
		_property = new HashMap<String,String>();
	}
	
	public void addString(String property, String value){
		_property.put(property,value);
	}
	
	public String getString(String property){
		return _property.get(property);
	}
	
	public void addDouble(String property, double value){
		_property.put(property, Double.toString(value));
	}
	
	public double getDouble(String property){
		return Double.parseDouble(_property.get(property));
	}

	public void addLong(String property, long value){
		_property.put(property, Long.toString(value));
	}
	
	public Long getLong(String property){
		return Long.parseLong(_property.get(property));
	}
	
	public boolean contains(String property){
		return _property.containsKey(property);
	}
}
