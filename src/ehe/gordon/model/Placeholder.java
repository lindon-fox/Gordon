package ehe.gordon.model;

public class Placeholder {
	private String name;
	private String defaultValue;
	private PlaceholderType placeholderType;

	public enum PlaceholderType {
		Value, DataList, Template
	}

	public Placeholder(String name, String defaultValue,
			PlaceholderType placeholderType) {
		super();
		this.name = name;
		this.defaultValue = defaultValue;
		this.placeholderType = placeholderType;
	}

	public String getName() {
		return name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public PlaceholderType getPlaceholderType() {
		return placeholderType;
	}

	public static Placeholder parseRawContents(String placeholderRawContents) {

		// get the values
		String name = null;
		String defaultValue = "";
		PlaceholderType placeholderType = PlaceholderType.Template;

		int defaultValueMarkerIndex = placeholderRawContents.indexOf("|");
		int typeMarkerIndex = placeholderRawContents.indexOf("`");
		if (defaultValueMarkerIndex < 1 && typeMarkerIndex < 1) {
			// there is not default value or type specified, so just use the
			// default defaults
			name = placeholderRawContents;
		} else {
			// do some checking...
			String examplePlaceholder = "{{{<placeholder name>|<default value>`<default type>}}}, for exmaple; {{{name|This is a value to be subbed in automatically`Value}}}. Other types are DataList (expecting a file) and Template (expecting a template name).";
			if (defaultValueMarkerIndex < 0 || typeMarkerIndex < 0) {
				System.err
						.println("When specifying a placeholder, either specify both the default value and type, or neither. This is what was received: "
								+ placeholderRawContents
								+ ". "
								+ examplePlaceholder);
				return null;
			}
			if (defaultValueMarkerIndex > typeMarkerIndex) {
				System.err
						.println("The default value should be defined before the default type "
								+ placeholderRawContents
								+ ". "
								+ examplePlaceholder);
			}
			name = placeholderRawContents.substring(0, defaultValueMarkerIndex);
			defaultValue = placeholderRawContents.substring(
					defaultValueMarkerIndex, typeMarkerIndex);
			if(defaultValue.equals("\"")){
				defaultValue = name;
			}
			try{
			placeholderType = Enum.valueOf(PlaceholderType.class,
					placeholderRawContents.substring(typeMarkerIndex,
							placeholderRawContents.length()));
			}
			catch(Exception e){
				System.err.println("The placeholder type was not recognised: "+ placeholderRawContents
								+ ". "
								+ examplePlaceholder);
			}
		}
		return new Placeholder(name, defaultValue, placeholderType);
	}
}
