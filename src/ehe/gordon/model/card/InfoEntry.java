package ehe.gordon.model.card;

public class InfoEntry {
	ColumnDefinition columnDefinition;
	String label;
	public InfoEntry(String label, ColumnDefinition columnDefinition) {
		this.columnDefinition = columnDefinition;
		this.label = label;
	}
	
	public ColumnDefinition getColumnDefinition() {
		return columnDefinition;
	}

	/**
	 * TODO rename this to a more meaningful name
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label + " (" + columnDefinition.toString() + ")";
	}
}