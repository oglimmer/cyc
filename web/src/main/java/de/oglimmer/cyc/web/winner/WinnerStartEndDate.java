package de.oglimmer.cyc.web.winner;

import java.text.DateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class WinnerStartEndDate {

	private Date start;
	private Date end;

	@Override
	public String toString() {
		DateFormat df = DateFormat.getDateTimeInstance();
		if (getStart() != null) {
			return "from " + df.format(getEnd()) + " to " + df.format(getStart());
		} else {
			return "since " + df.format(getEnd());
		}
	}
}