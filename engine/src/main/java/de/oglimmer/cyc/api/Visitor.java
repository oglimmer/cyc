package de.oglimmer.cyc.api;

import java.util.Set;

public class Visitor implements Guest {

	@Override
	public Set<Company> getAlreadyVisited() {
		return null;
	}

	@Override
	public void addAlreadyVisited(Company company) {
	}

}
