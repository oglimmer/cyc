package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

public class ApplicationProfile {

	@Getter
	private String name;

	@Getter
	private int qualification;

	@Getter
	private JobPosition jobPosition;

	@Getter
	private int desiredSalary;

	@Getter(AccessLevel.PACKAGE)
	private Map<Company, Offer> offeredSalary;

	public ApplicationProfile(String name, int qualification, JobPosition jobPosition, int desiredSalary) {
		super();
		this.name = name;
		this.qualification = qualification;
		this.jobPosition = jobPosition;
		this.desiredSalary = desiredSalary;
		this.offeredSalary = new HashMap<>();
	}

	public void offer(Establishment est) {
		offer(est, desiredSalary);
	}

	public void offer(Establishment est, int salary) {
		if (est != null) {
			Company company = ThreadLocal.getCompany();
			assert est.getParent() == company;
			this.offeredSalary.put(company, new Offer(est, salary));
		}
	}

	@Override
	public String toString() {
		return "ApplicationProfile [name=" + name + ", qualification=" + qualification + ", jobPosition=" + jobPosition
				+ ", desiredSalary=" + desiredSalary + "]";
	}

	CompanyOffer getMaxOfferFor() {
		List<Entry<Company, Offer>> maxOffers = calculateMaxOffer();

		CompanyOffer bestOffer = null;
		if (maxOffers.size() == 1) {
			bestOffer = new CompanyOffer(maxOffers.get(0));
		} else if (maxOffers.size() > 1) {
			bestOffer = new CompanyOffer(maxOffers.get((int) (Math.random() * maxOffers.size())));
		}
		return bestOffer;
	}

	private List<Entry<Company, Offer>> calculateMaxOffer() {
		int maxOff = -1;
		List<Entry<Company, Offer>> goodOffers = new ArrayList<>();
		for (Entry<Company, Offer> en : offeredSalary.entrySet()) {
			if (maxOff < en.getValue().getSalary()) {
				goodOffers.clear();
				goodOffers.add(en);
				maxOff = en.getValue().getSalary();
			} else if (maxOff == en.getValue().getSalary()) {
				goodOffers.add(en);
				maxOff = en.getValue().getSalary();
			}
		}
		return goodOffers;
	}

	@Value
	class Offer {
		private Establishment establishment;
		private int salary;
	}

	@Value
	class CompanyOffer {
		CompanyOffer(Entry<Company, Offer> next) {
			this.offer = new Offer(next.getValue().establishment, next.getValue().getSalary());
			this.company = next.getKey();
		}

		private Offer offer;
		private Company company;
	}
}
