package de.oglimmer.cyc.api;

public enum MenuEntryRule {
	INSTACE;

	private GroovyInitializer<IMenuEntryRule> ini;

	private MenuEntryRule() {
		ini = new GroovyInitializer<>();
		ini.loadGroovyScript("de.oglimmer.cyc.api.MenuEntryRules.groovy");
	}

	/**
	 * base deliciousness is 5. max 10, min 0.
	 */
	int getDeliciousness(Object ingredients, Object price) {
		return ini.getGroovyObject().getDeliciousness(ingredients, price);
	}

}
