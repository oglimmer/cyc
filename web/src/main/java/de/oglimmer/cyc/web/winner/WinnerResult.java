package de.oglimmer.cyc.web.winner;

import java.util.List;

import de.oglimmer.cyc.model.GameWinners;
import lombok.Value;

@Value
public class WinnerResult {

	/**
	 * Format: "Company name (number-of-wins)"
	 * Has WinnerHistoryCalculation.NUMBER_OF_PLACES_TO_CALC entries.
	 */
	private String[] threeDayWinner;

	/**
	 * Format: "Company name (total-assets-in-dollars)"
	 */
	private String lastWinner;

	private WinnerStartEndDate threeDayWinnerTimeRange;

	private List<GameWinners> gameWinnersList;

}