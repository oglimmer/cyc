package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Immutable
 * 
 * @author oli
 *
 */
@Slf4j
public class GuestDispatcher {

	@Getter
	private final City city;

	private final Map<Integer, Establishment> estList = new LinkedHashMap<>();

	private final int totalScore;

	public GuestDispatcher(Game game, City city) {
		this(game, city, new HashSet<Company>());
	}

	public GuestDispatcher(Game game, City city, Set<Company> excluded) {
		this.city = city;
		this.totalScore = buildData(game, excluded);
	}

	public boolean hasRestaurants() {
		return !estList.isEmpty();
	}

	public Establishment getRandom() {
		double rnd = Math.random() * totalScore;
		for (Integer startScore : estList.keySet()) {
			if (rnd <= startScore) {
				return estList.get(startScore);
			}
		}
		assert false;
		return null;
	}

	private int buildData(Game game, Set<Company> excluded) {
		int totalScoreCalc = 0;
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt() && !excluded.contains(c)) {
				for (Establishment est : c.getEstablishments(city.getName())) {
					int score = est.getScore();
					log.debug("Est score: {}:{} ({})", est.getAddress(), score, c.getName());
					game.getResult().getCreateNotExists(c.getName()).addEstablishmentScore(est.getAddress(), score);
					if (score > 0) {
						estList.put(totalScoreCalc + score, est);
						totalScoreCalc += score;
					}
				}
			}
		}
		return totalScoreCalc;
	}

}
