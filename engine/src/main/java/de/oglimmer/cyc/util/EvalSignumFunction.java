package de.oglimmer.cyc.util;

import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;

public class EvalSignumFunction implements Function {

	@Override
	public String getName() {
		return "signum";
	}

	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException {
		try {
			Double number = new Double(arguments);
			Double result = new Double(Math.signum(number.doubleValue()));
			return new FunctionResult(result.toString(), FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
		} catch (Exception e) {
			throw new FunctionException("Invalid argument.", e);
		}
	}

}
