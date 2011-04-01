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

	public ColumnDefinition getContentColumnDefinition(
			int contentColumnIndex) {
		return contentColumnDefinitions.get(contentColumnIndex);
	}

	public void add(ColumnDefinition columnDefinition) {
		columnDefinitions.add(columnDefinition);
		if (columnDefinition.titleOnly == false) {
			contentColumnDefinitions.add(columnDefinition);
		}
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