package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.oglimmer.cyc.util.PublicAPI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

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
	private Map<Company, Offer> offers;

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

	@PublicAPI
	public void tryLease(int bribe) {
		if (bribe >= 0) {
			offers.put(ThreadLocal.getCompany(), new Offer(false, bribe));
		}
	}

	@PublicAPI
	public void tryAcquisition(int bribe) {
		if (bribe >= 0) {
			offers.put(ThreadLocal.getCompany(), new Offer(true, bribe));
		}
	}

	@Override
	public String toString() {
		return "RealEstateProfile [city=" + city + ", salePrice=" + salePrice + ", leaseCost=" + leaseCost
				+ ", locationQuality=" + locationQuality + ", locationSize=" + locationSize + "]";
	}

	RealEstateOffer getMaxOfferFor() {
		boolean buy = true;
		List<Entry<Company, Offer>> goodOfferings = getBuyOffers();
		if (goodOfferings.isEmpty()) {
			buy = false;
			getLeaseOffers(goodOfferings);
		}

		RealEstateOffer bestOffer = null;
		if (goodOfferings.size() == 1) {
			bestOffer = new RealEstateOffer(buy, goodOfferings.get(0));
		} else if (goodOfferings.size() > 1) {
			bestOffer = new RealEstateOffer(buy, goodOfferings.get((int) (Math.random() * goodOfferings.size())));
		}
		return bestOffer;
	}

	private void getLeaseOffers(List<Entry<Company, Offer>> goodOfferings) {
		int maxOff = -1;
		for (Entry<Company, Offer> en : offers.entrySet()) {
			if (!en.getValue().isBuy()) {
				if (maxOff < en.getValue().getBribe()) {
					goodOfferings.clear();
					goodOfferings.add(en);
					maxOff = en.getValue().getBribe();
				} else if (maxOff == en.getValue().getBribe()) {
					goodOfferings.add(en);
					maxOff = en.getValue().getBribe();
				}
			}
		}
	}

	private List<Entry<Company, Offer>> getBuyOffers() {
		List<Entry<Company, Offer>> goodOfferings = new ArrayList<>();
		int maxOff = -1;
		for (Entry<Company, Offer> en : offers.entrySet()) {
			if (en.getValue().isBuy()) {
				if (maxOff < en.getValue().getBribe()) {
					goodOfferings.clear();
					goodOfferings.add(en);
					maxOff = en.getValue().getBribe();
				} else if (maxOff == en.getValue().getBribe()) {
					goodOfferings.add(en);
					maxOff = en.getValue().getBribe();
				}
			}
		}
		return goodOfferings;
	}

	@Value
	class Offer {
		private boolean buy;
		private int bribe;
	}

	@Value
	class RealEstateOffer {
		public RealEstateOffer(boolean buy, Entry<Company, Offer> en) {
			offer = new Offer(buy, en.getValue().getBribe());
			company = en.getKey();
		}

		private Offer offer;
		private Company company;
	}
}
