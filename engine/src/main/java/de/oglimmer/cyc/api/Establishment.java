package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.cyc.util.CountMap;

@Slf4j
public class Establishment {

	private static CountMap<String> cityNames = new CountMap<>();

	@Getter(AccessLevel.PACKAGE)
	private Company parent;
	@Getter
	private String address;
	@Getter
	private boolean rented;
	@Getter
	private int salePrice;
	@Getter
	private int leaseCost;
	@Getter
	private int locationQuality;
	@Getter
	private int locationSize; // 25..250

	private List<InteriorAccessory> interiorAccessories = new ArrayList<>();
	private Set<FoodUnit> storedFoodUnits = new TreeSet<>(new FoodUnit.FoodUnitComparator());

	public Establishment(Company parent, String city, int locationQuality, int locationSize, int leaseCost,
			int salePrice) {
		this.parent = parent;
		this.address = nextAddress(city);
		this.locationQuality = locationQuality;
		this.locationSize = locationSize;
		this.salePrice = salePrice;
		this.leaseCost = leaseCost;
		this.rented = true;
	}

	int getScore() {
		return EstablishmentRule.INSTACE.getScore(this, log);
	}

	public void sell() {
		if (!rented) {
			layOffAllEmployees();
			sellInteriorAccessories();
			parent.getEstablishmentsInt().remove(this);
			parent.incCash(salePrice);
		}
	}

	public void vacate() {
		if (rented) {
			layOffAllEmployees();
			sellInteriorAccessories();
			parent.getEstablishmentsInt().remove(this);
		}
	}

	public void layOffAllEmployees() {
		parent.getHumanResources().layOffAll(this);
	}

	public void sellInteriorAccessories() {
		for (InteriorAccessory ia : interiorAccessories) {
			int cost = (int) (ia.getAssetCost() * Constants.INSTACE.getSellFactorInteriorAccessories());
			parent.incCash(cost);
			parent.getGame().getResult().get(parent.getName()).addTotalInterior(-cost);
		}
	}

	public void buy() throws OutOfMoneyException {
		parent.decCash(salePrice);
		rented = false;
		leaseCost = 0;
		parent.getGame().getResult().get(parent.getName()).addTotalRealEstate(getSalePrice());
	}

	public void buyInteriorAccessories(String... ias) {
		Collection<InteriorAccessory> iaToAdd = new ArrayList<>();
		for (String ia : ias) {
			iaToAdd.add(InteriorAccessory.valueOf(ia));
		}

		int total = 0;
		for (InteriorAccessory ia : iaToAdd) {
			total += ia.getAssetCost();
		}

		if (parent.getCash() >= total) {
			try {
				parent.decCash(total);
				parent.getGame().getResult().get(parent.getName()).addTotalInterior(total);
				for (InteriorAccessory ia : iaToAdd) {
					interiorAccessories.add(ia);
					log.debug(parent.getName() + " bought " + ia + " for " + getAddress());
				}
			} catch (OutOfMoneyException e) {
				log.debug("Company " + e.getCompany() + " is bankrupt");
			}
		}
	}

	public void buyInteriorAccessoriesNotExist(String... ias) {
		Collection<InteriorAccessory> iaToAll = new ArrayList<>();
		for (String ia : ias) {
			InteriorAccessory _ia = InteriorAccessory.valueOf(ia);
			if (!interiorAccessories.contains(_ia)) {
				iaToAll.add(_ia);
			}
		}

		int total = 0;
		for (InteriorAccessory ia : iaToAll) {
			total += ia.getAssetCost();
		}

		if (parent.getCash() >= total) {
			try {
				parent.decCash(total);
				parent.getGame().getResult().get(parent.getName()).addTotalInterior(total);
				for (InteriorAccessory ia : iaToAll) {
					interiorAccessories.add(ia);
					log.debug(parent.getName() + " bought " + ia + " for " + getAddress());
				}
			} catch (OutOfMoneyException e) {
				log.debug("Company " + e.getCompany() + " is bankrupt");
			}
		}
	}

	public List<InteriorAccessory> getInteriorAccessories() {
		return Collections.unmodifiableList(interiorAccessories);
	}

	public void sendFood(FoodUnit foodUnit) {
		storedFoodUnits.add(foodUnit);
	}

	Set<FoodUnit> getStoredFoodUnitsInt() {
		return storedFoodUnits;
	}

	public Set<FoodUnit> getStoredFoodUnits() {
		return Collections.unmodifiableSet(storedFoodUnits);
	}

	public List<Employee> getEmployees() {
		List<Employee> subList = new ArrayList<>();
		for (Employee e : parent.getHumanResources().getEmployees()) {
			if (e.getEstablishment() == this) {
				subList.add(e);
			}
		}
		return subList;
	}

	public List<Employee> getEmployees(String jp) {
		List<Employee> subList = new ArrayList<>();
		for (Employee e : parent.getHumanResources().getEmployees(jp)) {
			if (e.getEstablishment() == this) {
				subList.add(e);
			}
		}
		return subList;
	}

	@Override
	public String toString() {
		return "Establishment [address=" + address + ", rented=" + rented + ", leaseCost=" + leaseCost
				+ ", locationQuality=" + locationQuality + ", locationSize=" + locationSize + "]";
	}

	private static String nextAddress(String city) {
		cityNames.add(city, 1);
		return city + "-" + cityNames.get(city);
	}

}
