package de.oglimmer.cyc.api;

import java.util.Set;

public interface Guest {

	Set<Company> getAlreadyVisited();

	void addAlreadyVisited(Company company);

}
