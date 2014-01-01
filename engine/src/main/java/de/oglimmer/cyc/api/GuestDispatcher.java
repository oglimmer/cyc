package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuestDispatcher {

	private Game game;

	private City city;

	private Map<Integer, Establishment> estList = new LinkedHashMap<>();

	private Set<Company> excluded;

	private int totalScore;

	public GuestDispatcher(Game game, City city) {
		this(game, city, new HashSet<Company>());
	}

	public GuestDispatcher(Game game, City city, Set<Company> excluded) {
		this.game = game;
		this.city = city;
		this.excluded = excluded;
		buildData();
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

	private void buildData() {
		for (Company c : game.getCompanies()) {
			if (!c.isBankrupt() && !excluded.contains(c)) {
				for (Establishment est : c.getEstablishments(city.getName())) {
					int score = est.getScore();
					log.debug("Est score: {}:{} ({})", est.getAddress(), score, c.getName());
					game.getResult().get(c.getName()).addEstablishmentScore(est.getAddress(), score);
					if (score > 0) {
						estList.put(totalScore + score, est);
						totalScore += score;
					}
				}
			}
		}
	}

}
