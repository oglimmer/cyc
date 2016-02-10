package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

public class GuestDispatcherManager {

	private Map<Long, GuestDispatcher> map = new HashMap<>();

	private Game game;
	private City city;

	public GuestDispatcherManager(Game game, City city) {
		this.game = game;
		this.city = city;
		map.put(0L, new GuestDispatcher(game, city));
	}

	public GuestDispatcher getDispatcher(Guest guest) {
		GuestDispatcher guestdisp = map.get(guest.getAlreadyVisitedHash());
		if (guestdisp == null) {
			guestdisp = new GuestDispatcher(game, city, guest.getAlreadyVisited());
			map.put(guest.getAlreadyVisitedHash(), guestdisp);
		}
		return guestdisp;
	}

}
