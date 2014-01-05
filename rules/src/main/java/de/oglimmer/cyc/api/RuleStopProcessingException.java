package de.oglimmer.cyc.api;

import lombok.Getter;

public class RuleStopProcessingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private int score;

	public RuleStopProcessingException(int score) {
		this.score = score;
	}

}
