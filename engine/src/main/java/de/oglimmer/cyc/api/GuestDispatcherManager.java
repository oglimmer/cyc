package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GuestDispatcherManager {

	private Map<Set<Company>, GuestDispatcher> map = new HashMap<>();

	private Game game;
	private City city;

	public GuestDispatcherManager(Game game, City city) {
		this.game = game;
		this.city = city;
		map.put(null, new GuestDispatcher(game, city));
	}

	public GuestDispatcher getDispatcher(Guest guest) {
		GuestDispatcher guestdisp = map.get(guest.getAlreadyVisited());
		if (guestdisp == null) {
			guestdisp = new GuestDispatcher(game, city, guest.getAlreadyVisited());
			map.put(guest.getAlreadyVisited(), guestdisp);
		}
		return guestdisp;
	}

}
