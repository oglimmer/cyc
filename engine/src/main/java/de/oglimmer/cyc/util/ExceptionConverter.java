package de.oglimmer.cyc.util;

import com.google.common.base.Throwables;

public class ExceptionConverter {

	public static String convertToString(Throwable t) {
		String throwAsString = Throwables.getStackTraceAsString(t);
		return throwAsString.replace("\n", "<br/>");
	}

}
