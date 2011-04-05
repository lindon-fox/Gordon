package ehe.gordon.model.card;

import java.util.ArrayList;
import java.util.List;

public class ColumnDefinitions {

	public List<ColumnDefinition> columnDefinitions;

	public ColumnDefinitions() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
	}

	public ColumnDefinition getColumnDefinition(int columnIndex) {
		return columnDefinitions.get(columnIndex);
	}

	public void add(ColumnDefinition columnDefinition) {
		columnDefinitions.add(columnDefinition);
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
}