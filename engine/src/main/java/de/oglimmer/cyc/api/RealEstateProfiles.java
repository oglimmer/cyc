package de.oglimmer.cyc.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import de.oglimmer.cyc.collections.DataPair;
import de.oglimmer.cyc.collections.ForEach;
import de.oglimmer.cyc.collections.SortableContainer;
import de.oglimmer.cyc.collections.UnmodifiableIterator;
import de.oglimmer.cyc.util.CountMap;

@Slf4j
public class RealEstateProfiles implements Iterable<RealEstateProfile>, SortableContainer<RealEstateProfile> {

	private List<RealEstateProfile> profiles = new ArrayList<>();

	private List<DataPair> citiesToRestaurants = new ArrayList<>();

	public RealEstateProfiles(Game game, List<String> cities, Collection<Company> companies) {
		CountMap<String> tmpCountMap = new CountMap<>();
		for (String city : cities) {
			tmpCountMap.add(city, 0);
		}
		for (Company c : companies) {
			if (!c.isBankrupt()) {
				for (Establishment est : c.getEstablishmentsInt()) {
					tmpCountMap.add(est.getAddress().substring(0, est.getAddress().indexOf("-")), 1);
				}
			}
		}
		for (String city : tmpCountMap.keySet()) {
			citiesToRestaurants.add(new DataPair(city, tmpCountMap.get(city)));
		}

		for (int i = 0; i < game.getConstants().getNumberRealEstateProfiles(companies.size()); i++) {
			int locationQuality = game.getConstants().getLocationQuality();
			int locationSize = game.getConstants().getLocationSize();
			int salePrice = game.getConstants().getSalePrice(locationQuality, locationSize);
			int leaseCosts = game.getConstants().getLeaseCosts(locationQuality, locationSize);
			profiles.add(new RealEstateProfile(cities.get((int) (Math.random() * cities.size())), salePrice,
					leaseCosts, locationQuality, locationSize));
		}
	}

	public static void readCities(Game game, List<String> cities, int noPlayer) {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				RealEstateProfiles.class.getResourceAsStream("/uk_cities.csv")))) {
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				cities.add(line);
			}
		} catch (IOException e) {
			log.error("Failed to read cities", e);
		}
		Collections.shuffle(cities);
		int numberOfCities = game.getConstants().getNumberCities(noPlayer);
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

	@Override
	public RealEstateProfile get(int index) {
		try {
			return profiles.get(index);
		} catch (IndexOutOfBoundsException e) {
			return new RealEstateProfile(null, -1, -1, -1, -1);
		}
	}

	@Override
	public RealEstateProfile getLowest() {
		return get(0);
	}

	@Override
	public RealEstateProfile getHighest() {
		return get(size() - 1);
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
