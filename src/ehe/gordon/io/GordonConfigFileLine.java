package ehe.gordon.io;

import ehe.gordon.model.Placeholder;
import ehe.gordon.model.SnippetDefinition;

/**
 * @author Boy.pockets
 *
 */
public class GordonConfigFileLine {
	private int level;
	private Placeholder placeholder;

	public GordonConfigFileLine(String line) {
		level = countIndentLevel(line);
		String rawContents = line.trim();//looks like this: {{{something|something`something}}
		assert rawContents.indexOf(SnippetDefinition.PARAMETER_START) == 0;
		assert rawContents.indexOf(SnippetDefinition.PARAMETER_END) == rawContents.length() - SnippetDefinition.PARAMETER_END.length();
		placeholder = Placeholder.parseRawContents(rawContents.substring(2, rawContents.length() - 3));
	}
	
	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return
	 */
	public Placeholder getPlaceholder() {
		return placeholder;
	}


	private static int countIndentLevel(String line) {
		int level = 0;
		// the first two characters must be white space...
		if (hasIndentAtLevel(line, level)) {
			// ok so far
			level++;
			while (hasIndentAtLevel(line, level)) {
				level++;
			}
			return level;
		} else {
			return -1;
		}
	}
	private static boolean hasIndentAtLevel(String line, int level) {
		assert level >= 0 && level < line.length() - 1;
		if (line.charAt(level * 2) == ' ' && line.charAt(level * 2 + 1) == ' ') {
			return true;
		}
		return false;
	}
}
