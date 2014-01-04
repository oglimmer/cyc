package de.oglimmer.cyc.api;

import java.util.Collections;
import java.util.Set;

public class Visitor extends Guest {

	@Override
	public Set<Company> getAlreadyVisited() {
		return Collections.emptySet();
	}

	@Override
	public void addAlreadyVisited(Company company) {
		// visitors haven't memory
	}

}
