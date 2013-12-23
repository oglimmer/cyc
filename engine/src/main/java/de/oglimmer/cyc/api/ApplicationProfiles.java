package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.oglimmer.cyc.api.ApplicationProfile.Offer;
import de.oglimmer.cyc.collections.ForEach;
import de.oglimmer.cyc.collections.SortableContainer;
import de.oglimmer.cyc.collections.UnmodifiableIterator;

public class ApplicationProfiles implements Iterable<ApplicationProfile>, SortableContainer<ApplicationProfile> {

	private List<ApplicationProfile> profiles;

	public ApplicationProfiles(Game game, int noCompanies) {
		profiles = new ArrayList<>();
		createApplications(game, noCompanies, JobPosition.CHEF);
		createApplications(game, noCompanies, JobPosition.WAITER);
		createApplications(game, noCompanies, JobPosition.MANAGER);
	}

	private ApplicationProfiles(List<ApplicationProfile> profiles) {
		this.profiles = profiles;
	}

	private void createApplications(Game game, int noCompanies, JobPosition jp) {
		Constants consts = game.getConstants();
		int count = consts.getNumberApplicationProfiles(jp.toString(), noCompanies);
		for (int i = 0; i < count; i++) {
			int quali = consts.getQualification();
			int salary = consts.getSalary(quali);
			ApplicationProfile p = new ApplicationProfile(consts.getEmployeesFirstname() + ", " + consts.getEmployeesLastname(), quali, jp,
					salary);
			profiles.add(p);
		}
	}

	static Entry<Company, Offer> getMaxOfferFor(ApplicationProfile p) {
		Map<Company, Offer> offeredSal = p.getOfferedSalary();
		int maxOff = -1;
		List<Entry<Company, Offer>> goodOffers = new ArrayList<>();
		for (Entry<Company, Offer> en : offeredSal.entrySet()) {
			if (maxOff < (Integer) en.getValue().getSalary()) {
				goodOffers.clear();
				goodOffers.add(en);
				maxOff = (Integer) en.getValue().getSalary();
			} else if (maxOff == (Integer) en.getValue().getSalary()) {
				goodOffers.add(en);
				maxOff = (Integer) en.getValue().getSalary();
			}
		}
		if (goodOffers.size() == 1) {
			return goodOffers.iterator().next();
		} else if (goodOffers.size() > 1) {
			return goodOffers.get((int) (Math.random() * goodOffers.size()));
		}
		return null;
	}

	public ApplicationProfile get(int index) {
		try {
			return profiles.get(index);
		} catch (IndexOutOfBoundsException e) {
			return new ApplicationProfile("Nobody", 0, null, 0);
		}
	}

	public ApplicationProfiles subList(String jobPosition) {
		List<ApplicationProfile> newList = new ArrayList<>();
		for (ApplicationProfile ap : profiles) {
			if (ap.getJobPosition().toString().equalsIgnoreCase(jobPosition)) {
				newList.add(ap);
			}
		}
		return new ApplicationProfiles(newList);
	}

	public int size() {
		return profiles.size();
	}

	public void each(ForEach r) {
		for (ApplicationProfile ap : profiles) {
			r.run(ap);
		}
	}

	@Override
	public ApplicationProfile getLowest() {
		return get(0);
	}

	@Override
	public ApplicationProfile getHighest() {
		return get(size() - 1);
	}
	
	public ApplicationProfiles sortByQualification() {
		Collections.sort(profiles, new Comparator<ApplicationProfile>() {
			@Override
			public int compare(ApplicationProfile o1, ApplicationProfile o2) {
				return Integer.valueOf(o1.getQualification()).compareTo(Integer.valueOf(o2.getQualification()));
			}
		});
		return this;
	}

	public ApplicationProfiles sortBySalary() {
		Collections.sort(profiles, new Comparator<ApplicationProfile>() {
			@Override
			public int compare(ApplicationProfile o1, ApplicationProfile o2) {
				return Integer.valueOf(o1.getDesiredSalary()).compareTo(Integer.valueOf(o2.getDesiredSalary()));
			}
		});
		return this;
	}

	@Override
	public Iterator<ApplicationProfile> iterator() {
		return new UnmodifiableIterator<ApplicationProfile>(profiles.iterator());
	}

	public Iterator<ApplicationProfile> iteratorInt() {
		return profiles.iterator();
	}

	@Override
	public String toString() {
		return "ApplicationProfiles [profiles=" + Arrays.toString(profiles.toArray()) + "]";
	}

}
