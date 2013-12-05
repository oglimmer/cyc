package de.oglimmer.cyc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;
import de.oglimmer.cyc.util.CountMap;

@Slf4j
public class RealEstateProfiles implements Iterable<RealEstateProfile>, Container<RealEstateProfile> {

	private List<RealEstateProfile> profiles = new ArrayList<>();

	private List<DataPair> citiesToRestaurants = new ArrayList<>();

	public RealEstateProfiles(List<String> cities, Collection<Company> companies) {
		CountMap<String> tmpCountMap = new CountMap<>();
		for (String city : cities) {
			tmpCountMap.add(city, 0);
		}
		for (Company c : companies) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishments()) {
					tmpCountMap.add(est.getAddress().substring(0, est.getAddress().indexOf("-")), 1);
				}
			}
		}
		for (String city : tmpCountMap.keySet()) {
			citiesToRestaurants.add(new DataPair(city, tmpCountMap.get(city)));
		}

		for (int i = 0; i < companies.size(); i++) {
			int locationQuality = Constants.INSTACE.getLocationQuality();
			int locationSize = Constants.INSTACE.getLocationSize();
			int salePrice = Constants.INSTACE.getSalePrice(locationQuality, locationSize);
			int leaseCosts = Constants.INSTACE.getLeaseCosts(locationQuality, locationSize);
			profiles.add(new RealEstateProfile(cities.get((int) (Math.random() * cities.size())), salePrice,
					leaseCosts, locationQuality, locationSize));
		}
	}

	public static void readCities(List<String> cities, int noPlayer) {
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				RealEstateProfiles.class.getResourceAsStream("/uk_cities.csv")))) {
			while ((line = br.readLine()) != null) {
				cities.add(line);
			}
		} catch (IOException e) {
			log.error("Failed to read cities", e);
		}
		Collections.shuffle(cities);
		int numberOfCities = Constants.INSTACE.getNumberCities(noPlayer);
		while (cities.size() != numberOfCities) {
			cities.remove(0);
		}
	}

	@Override
	public Iterator<RealEstateProfile> iterator() {
		return new UnmodifiableIterator<RealEstateProfile>(profiles.iterator());
	}

	Iterator<RealEstateProfile> iteratorInt() {
		return profiles.iterator();
	}

	public List<DataPair> getCitiesToRestaurants() {
		return Collections.unmodifiableList(citiesToRestaurants);
	}

	Map<String, Object> getOfferFor(RealEstateProfile p) {
		boolean buy = true;
		int maxOff = -1;
		List<Entry<Company, Map<String, Integer>>> goodOfferings = new ArrayList<>();
		for (Entry<Company, Map<String, Integer>> en : p.getOffers().entrySet()) {
			if (en.getValue().get("buy") == 1) {
				if (maxOff < en.getValue().get("bribe")) {
					goodOfferings.clear();
					goodOfferings.add(en);
					maxOff = en.getValue().get("bribe");
				} else if (maxOff == en.getValue().get("bribe")) {
					goodOfferings.add(en);
					maxOff = en.getValue().get("bribe");
				}
			}
		}
		if (goodOfferings.isEmpty()) {
			buy = false;
			for (Entry<Company, Map<String, Integer>> en : p.getOffers().entrySet()) {
				if (en.getValue().get("buy") == 0) {
					if (maxOff < en.getValue().get("bribe")) {
						goodOfferings.clear();
						goodOfferings.add(en);
						maxOff = en.getValue().get("bribe");
					} else if (maxOff == en.getValue().get("bribe")) {
						goodOfferings.add(en);
						maxOff = en.getValue().get("bribe");
					}
				}
			}
		}

		if (goodOfferings.size() == 0) {
			return null;
		} else if (goodOfferings.size() > 1) {
			while (goodOfferings.size() > 1) {
				goodOfferings.remove((int) (Math.random() * goodOfferings.size()));
			}
		}

		Map<String, Object> resultOffering = new HashMap<>();
		Entry<Company, Map<String, Integer>> en = goodOfferings.iterator().next();
		resultOffering.put("buy", buy);
		resultOffering.put("company", en.getKey());
		resultOffering.put("bribe", en.getValue().get("bribe"));

		return resultOffering;
	}

	@Override
	public RealEstateProfile get(int index) {
		try {
			return profiles.get(index);
		} catch (IndexOutOfBoundsException e) {
			return new RealEstateProfile(null, -1, -1, -1, -1);
		}
	}

	public RealEstateProfiles sortByLeaseCost() {
		Collections.sort(profiles, new Comparator<RealEstateProfile>() {
			@Override
			public int compare(RealEstateProfile o1, RealEstateProfile o2) {
				return Integer.compare(o1.getLeaseCost(), o2.getLeaseCost());
			}
		});
		return this;
	}

	public RealEstateProfiles sortBySalePrice() {
		Collections.sort(profiles, new Comparator<RealEstateProfile>() {
			@Override
			public int compare(RealEstateProfile o1, RealEstateProfile o2) {
				return Integer.compare(o1.getSalePrice(), o2.getSalePrice());
			}
		});
		return this;
	}

	public RealEstateProfiles sortByLocationQuality() {
		Collections.sort(profiles, new Comparator<RealEstateProfile>() {
			@Override
			public int compare(RealEstateProfile o1, RealEstateProfile o2) {
				return Integer.compare(o1.getLocationQuality(), o2.getLocationQuality());
			}
		});
		return this;
	}

	public RealEstateProfiles sortByLocationSize() {
		Collections.sort(profiles, new Comparator<RealEstateProfile>() {
			@Override
			public int compare(RealEstateProfile o1, RealEstateProfile o2) {
				return Integer.compare(o1.getLocationSize(), o2.getLocationSize());
			}
		});
		return this;
	}

	@Override
	public String toString() {
		return "RealEstateProfiles [profiles=" + Arrays.toString(profiles.toArray()) + "]";
	}

	@Override
	public int size() {
		return profiles.size();
	}

	@Override
	public void each(ForEach r) {
		for (RealEstateProfile rep : profiles) {
			r.run(rep);
		}
	}

}
