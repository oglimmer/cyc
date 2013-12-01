package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;

public class RealEstateProfile {

	@Getter
	private String city;

	@Getter
	private int salePrice;

	@Getter
	private int leaseCost;

	@Getter
	private int locationQuality;

	@Getter
	private int locationSize;

	@Getter(AccessLevel.PACKAGE)
	private Map<Company, Map<String, Integer>> offers;

	@Getter(AccessLevel.PACKAGE)
	private Map<Company, Integer> bribes;

	public RealEstateProfile(String city, int salePrice, int leaseCosts, int locationQuality, int locationSize) {
		this.city = city;
		this.salePrice = salePrice;
		this.leaseCost = leaseCosts;
		this.locationQuality = locationQuality;
		this.locationSize = locationSize;
		this.offers = new HashMap<>();
		this.bribes = new HashMap<>();
	}

	public void tryLease(int bribe) {
		Map<String, Integer> map = new HashMap<>();
		map.put("buy", 0);
		map.put("bribe", bribe);
		offers.put(ThreadLocal.getCompany(), map);
	}

	public void tryAcquisition(int bribe) {
		Map<String, Integer> map = new HashMap<>();
		map.put("buy", 1);
		map.put("bribe", bribe);
		offers.put(ThreadLocal.getCompany(), map);
	}

	@Override
	public String toString() {
		return "RealEstateProfile [city=" + city + ", salePrice=" + salePrice + ", leaseCost=" + leaseCost
				+ ", locationQuality=" + locationQuality + ", locationSize=" + locationSize + "]";
	}

}
