package de.oglimmer.cyc.engineContainer;

public class NoEngineException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoEngineException(String baseDir) {
		super("No engine found at " + baseDir);
	}

}
