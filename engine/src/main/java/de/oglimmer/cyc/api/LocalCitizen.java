package de.oglimmer.cyc.api;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

public class LocalCitizen implements Guest {

	@Getter
	private Set<Company> alreadyVisited = new HashSet<>();

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
	}
}
