package de.oglimmer.cyc.api;

public enum EstablishmentRule {
	INSTACE;

	private GroovyInitializer<IEstablishmentRule> ini;

	private EstablishmentRule() {
		ini = new GroovyInitializer<>();
		ini.loadGroovyScript("de.oglimmer.cyc.api.EstablishmentRules.groovy");
	}

	int getScore(Object est, Object log) {
		return ini.getGroovyObject().getScore(est);
	}

}
