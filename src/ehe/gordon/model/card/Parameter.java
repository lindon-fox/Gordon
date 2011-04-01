package ehe.gordon.model.card;

public class Parameter {
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
}
