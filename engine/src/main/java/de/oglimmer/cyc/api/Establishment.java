package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import de.oglimmer.cyc.collections.CycCollections;
import de.oglimmer.cyc.collections.JavaScriptList;
import de.oglimmer.cyc.collections.JavaScriptSet;
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

	private _Cache cache;

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
		cache = new _Cache(parent.getGame());
	}

	int getScore() {
		return cache.getValue();
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
		for (InteriorAccessory intAcc : interiorAccessories) {
			int cost = (int) (intAcc.getAssetCost() * parent.getGame().getConstants()
					.getSellFactorInteriorAccessories());
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

	public void buyInteriorAccessories(String... intAccies) {
		Collection<InteriorAccessory> iaToAdd = InteriorAccessory.valuesOf(null, intAccies);
		buyInteriorAccessories(iaToAdd);
	}

	public void buyInteriorAccessoriesNotExist(String... intAccies) {
		Collection<InteriorAccessory> iaToAdd = InteriorAccessory.valuesOf(interiorAccessories, intAccies);
		buyInteriorAccessories(iaToAdd);
	}

	private int calcTotalCost(Collection<InteriorAccessory> iaToAdd) {
		int total = 0;
		for (InteriorAccessory intAcc : iaToAdd) {
			total += intAcc.getAssetCost();
		}
		return total;
	}

	private void buyInteriorAccessories(Collection<InteriorAccessory> iaToAdd) {
		int total = calcTotalCost(iaToAdd);
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

	public JavaScriptList<InteriorAccessory> getInteriorAccessories() {
		return CycCollections.unmodifiableList(interiorAccessories);
	}

	public void sendFood(FoodUnit foodUnit) {
		storedFoodUnits.add(foodUnit);
	}

	Set<FoodUnit> getStoredFoodUnitsInt() {
		return storedFoodUnits;
	}

	public JavaScriptSet<FoodUnit> getStoredFoodUnits() {
		return CycCollections.unmodifiableSet(storedFoodUnits);
	}

	public JavaScriptList<Employee> getEmployees() {
		List<Employee> subList = new ArrayList<>();
		for (Employee e : parent.getHumanResources().getEmployees()) {
			if (e.getEstablishment() == this) {
				subList.add(e);
			}
		}
		return CycCollections.unmodifiableList(subList);
	}

	public JavaScriptList<Employee> getEmployees(String jobPosition) {
		List<Employee> subList = new ArrayList<>();
		for (Employee employee : parent.getHumanResources().getEmployees(jobPosition)) {
			if (employee.getEstablishment() == this) {
				subList.add(employee);
			}
		}
		return CycCollections.unmodifiableList(subList);
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

	void cleanFoodStorage() {
		for (Iterator<FoodUnit> it = storedFoodUnits.iterator(); it.hasNext();) {
			FoodUnit fu = it.next();
			if (fu.incDay(this)) {
				it.remove();
			}
		}
	}

	public int getTotalFoodUnits(String foodName) {
		int total = 0;
		for (FoodUnit fu : storedFoodUnits) {
			if (fu.getFood().name().equalsIgnoreCase(foodName)) {
				total += fu.getUnits();
			}
		}
		return total;
	}

	class _Cache extends Cache<Integer> {

		public _Cache(Game game) {
			super(Type.DAILY, game, "Establishment");
		}

		@Override
		protected Integer fetchValue() {
			return EstablishmentRule.INSTACE.getScore(Establishment.this, log);
		}

	}

}
