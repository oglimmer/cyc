package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

public class RealEstateProfile {

	private String city;

	private int salePrice;
	private int leaseCost;

	private int locationQuality;
	private int locationSize;

	private Map<Company, Map<String, Integer>> offers;
	private Map<Company, Integer> bribes;

	public RealEstateProfile(String city, int salePrice, int leaseCosts, int locationQuality, int locationSize) {
		this.city = city;
		this.salePrice = salePrice;
		this.leaseCost = leaseCosts;
		this.locationQuality = locationQuality;
		this.locationSize = locationSize;
		init();
	}

	void init() {
		offers = new HashMap<>();
		bribes = new HashMap<>();
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

	Map<Company, Map<String, Integer>> getOffers() {
		return offers;
	}

	Map<Company, Integer> getBribes() {
		return bribes;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public int getLocationQuality() {
		return locationQuality;
	}

	public int getLeaseCost() {
		return leaseCost;
	}

	public int getLocationSize() {
		return locationSize;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "RealEstateProfile [city=" + city + ", salePrice=" + salePrice + ", leaseCost=" + leaseCost
				+ ", locationQuality=" + locationQuality + ", locationSize=" + locationSize + "]";
	}

}
