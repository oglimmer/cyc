package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Establishment {

	private Logger log = LoggerFactory.getLogger(Establishment.class);

	private Company parent;

	private String address;

	private boolean rented;

	private int salePrice;
	private int leaseCost;

	private int locationQuality;
	private int locationSize; // 25..250

	private List<InteriorAccessory> interiorAccessories = new ArrayList<>();
	private List<FoodUnit> storedFoodUnits = new ArrayList<>();

	public Establishment(Company parent, String city, int locationQuality, int locationSize, int leaseCost,
			int salePrice) {
		this.parent = parent;
		this.address = nextAddress(parent, city);
		this.locationQuality = locationQuality;
		this.locationSize = locationSize;
		this.salePrice = salePrice;
		this.leaseCost = leaseCost;
		this.rented = true;
	}

	int getScore() {
		EstablishmentRule r = new EstablishmentRule();
		return r.getScore(this, log, JobPosition.class, InteriorAccessory.class);
	}

	public boolean isRented() {
		return rented;
	}

	public int getLeaseCost() {
		return leaseCost;
	}

	public int getLocationQuality() {
		return locationQuality;
	}

	public int getLocationSize() {
		return locationSize;
	}

	public String getAddress() {
		return address;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public void sell() {
		if (!rented) {
			parent.getEstablishmentsInt().remove(this);
			parent.incCash(salePrice);
		}
	}

	public void vacate() {
		if (rented) {
			parent.getEstablishmentsInt().remove(this);
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

	List<FoodUnit> getStoredFoodUnitsInt() {
		return storedFoodUnits;
	}

	public List<FoodUnit> getStoredFoodUnits() {
		return Collections.unmodifiableList(storedFoodUnits);
	}

	Company getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return "Establishment [address=" + address + ", rented=" + rented + ", leaseCost=" + leaseCost
				+ ", locationQuality=" + locationQuality + ", locationSize=" + locationSize + "]";
	}

	private static String nextAddress(Company company, String city) {
		int estsInCity = 1;
		for (Establishment est : company.getEstablishments()) {
			if (est.getAddress().startsWith(city)) {
				estsInCity++;
			}
		}
		return city + "-" + estsInCity;
	}
}
