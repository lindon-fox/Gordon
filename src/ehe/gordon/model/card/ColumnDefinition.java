package ehe.gordon.model.card;

import java.util.ArrayList;
import java.util.List;


public class ColumnDefinition {
	String snippetName;//the name of the snippet that this column will be linked with
	List<Parameter> parameters; //the parameter and values to be substituted
	
	
	
	public ColumnDefinition(String snippetName) {
		super();
		this.snippetName = snippetName;
		this.parameters = new ArrayList<Parameter>();
	}

	public void addParameter(Parameter parameter){
		parameters.add(parameter);
	}
	
	public String getParameterValue(String parameterName){
		for (Parameter parameter: parameters) {
			if(parameter.getName().equals(parameterName)){
				return parameter.getValue();
			}
		}
		return null;
	}

	public String getSnippetName() {
		return snippetName;
	}

	
	public List<Parameter> getParameters() {
		return parameters;
	}
	
	
}