package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class City {

	@Getter
	private String name;

	@Getter
	private Collection<Guest> guests = new ArrayList<>();

	public City(String name, Game game) {
		super();
		this.name = name;

		int companies = game.getCompanies().size();
		int baseGuests = game.getConstants().getBaseGuests(companies);
		int rndGuests = game.getConstants().getRndGuests(companies);

		int totalLocal = (int) ((Math.random() * rndGuests) + baseGuests);
		int totalVisitor = (int) ((Math.random() * rndGuests) + baseGuests) * 1 / 3;
		log.debug("Guests in {}: {} / {}", name, totalLocal, totalVisitor);

		for (int i = 0; i < totalLocal; i++) {
			guests.add(new LocalCitizen(game));
		}
		for (int i = 0; i < totalVisitor; i++) {
			guests.add(new Visitor());
		}
	}
}
