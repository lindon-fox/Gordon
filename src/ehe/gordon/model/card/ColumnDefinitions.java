package ehe.gordon.model.card;

import java.util.ArrayList;
import java.util.List;

public class ColumnDefinitions {

	public List<ColumnDefinition> columnDefinitions;
	public List<ColumnDefinition> contentColumnDefinitions;

	public ColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
		contentColumnDefinitions = new ArrayList<ColumnDefinition>();
	}

	public ColumnDefinition getContentColumnDefinition(int contentColumnIndex) {
		if(contentColumnIndex >= contentColumnDefinitions.size()){
			System.err.println("Attempting to get a non existing content column...");
			return null;
		}
		return contentColumnDefinitions.get(contentColumnIndex);
	}

	public void add(ColumnDefinition columnDefinition) {
		columnDefinitions.add(columnDefinition);
		int contentColumnCount = columnDefinition.getVariableParameterValueCount();
		while(contentColumnCount > 0){
			contentColumnDefinitions.add(columnDefinition);
			contentColumnCount--;
		}
	}
	
	
	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean firstPassDone = false;
		for (ColumnDefinition columnDefinition : columnDefinitions) {
			if(firstPassDone == true){
				builder.append(", ");
			}
			else{
				firstPassDone = true;
			}
			builder.append(columnDefinition.toString());
		}
		return builder.toString();
	}

	public int getContentColumnCount() {
		return contentColumnDefinitions.size();
	}
}