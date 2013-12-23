package com.cloudbees.util.rhino.sandbox;

import org.mozilla.javascript.EvaluatorException;

public class RuntimeExceededException extends EvaluatorException {

	public RuntimeExceededException(int number) {
		super("Exeeded runtime of " + number + " seconds");
	}

	private static final long serialVersionUID = 1L;

}
