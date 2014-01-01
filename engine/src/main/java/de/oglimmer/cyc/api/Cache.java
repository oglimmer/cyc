package de.oglimmer.cyc.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract public class Cache<T> {

	public enum Type {
		DAILY
	}

	private T cachedValue;

	@Getter
	private Type type;

	private String infoText;

	public Cache(Type type, Game game, String infoText) {
		this.type = type;
		this.infoText = infoText;
		game.getCaches().add(this);
	}

	abstract protected T fetchValue();

	public T getValue() {
		if (cachedValue == null) {
			log.debug("Fetch new value for {}", infoText);
			cachedValue = fetchValue();
		}
		return cachedValue;
	}

	public void reset() {
		cachedValue = null;
	}

}
