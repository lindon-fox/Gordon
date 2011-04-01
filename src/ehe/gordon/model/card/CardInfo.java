package ehe.gordon.model.card;

import java.util.ArrayList;
import java.util.List;


public 	class CardInfo{
	List<InfoEntry> infoEntries;
	
	public CardInfo(){
		infoEntries = new ArrayList();
	}
	
	public List<InfoEntry> getInfoEntries() {
		return infoEntries;
	}

	public void add(InfoEntry infoInfo){
		infoEntries.add(infoInfo);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		boolean firstPassDone = false;
		for (InfoEntry infoEntry : infoEntries) {
			if(firstPassDone == true){
				builder.append(", ");
			}
			else{
				firstPassDone = true;
			}
			builder.append(infoEntry.toString());
		}
		return builder.toString();
	}
}
