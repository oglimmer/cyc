package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.Set;

public class LocalCitizen extends Guest {

	private Set<Company> alreadyVisited = new HashSet<>();

	private long alreadyVisitedHash;

	private Game game;

	public LocalCitizen(Game game) {
		this.game = game;
	}

	@Override
	public void addAlreadyVisited(Company company) {
		alreadyVisited.add(company);
		if (alreadyVisited.size() >= game.getCompanies().size() - 1) {
			alreadyVisited.clear();
		}
		calcHash();
	}

	private void calcHash() {
		alreadyVisitedHash = 0;
		for (Company c : alreadyVisited) {
			alreadyVisitedHash ^= c.hashCode();
		}
	}

	@Override
	public Long getAlreadyVisitedHash() {
		return alreadyVisitedHash;
	}

	@Override
	public Set<Company> getAlreadyVisited() {
		return alreadyVisited;
	}
}
