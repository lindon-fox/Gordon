package ehe.gordon.model.card;

public class Parameter {
	public static final String REPEATER = "*";
	private String parameterName;
	private String parameterValue;
	
	
	public Parameter(String parameterName, String parameterValue) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}
	public String getName() {
		return parameterName;
	}
	public String getValue() {
		return parameterValue;
	}	
	
	public boolean valueIsVariable() {
		return Parameter.REPEATER.equals(this.getValue());
	}
	
	@Override
	public String toString() {
		return parameterName + " = " + parameterValue;
	}
}
