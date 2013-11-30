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

public class ApplicationProfiles implements Iterable<ApplicationProfile>, Container<ApplicationProfile> {

	private String[] surnames = { "Smith", "Jones", "Taylor", "Brown", "Williams", "Wilson", "Johnson", "Davies",
			"Robinson", "Wright", "Thompson", "Evans", "Walker", "White", "Roberts", "Green", "Hall", "Wood",
			"Jackson", "Clarke" };

	private String[] forenames = { "Jacob", "Oliver", "Riley", "Jack", "Alfie", "Harry", "Charlie", "Dylan", "William",
			"Mason", "Amelia", "Ava", "Mia", "Lily", "Olivia", "Ruby", "Seren", "Evie", "Ella", "Grace", "Emily" };

	private List<ApplicationProfile> profiles;

	public ApplicationProfiles(int noCompanies) {
		profiles = new ArrayList<>();
		createApplications(noCompanies * 2, noCompanies, JobPosition.CHEF);
		createApplications(noCompanies * 2, noCompanies, JobPosition.WAITER);
		createApplications(noCompanies, 0, JobPosition.MANAGER);
	}

	private ApplicationProfiles(List<ApplicationProfile> profiles) {
		this.profiles = profiles;
	}

	private void createApplications(int m, int n, JobPosition jp) {
		int count = (int) (Math.random() * m + n);
		for (int i = 0; i < count; i++) {
			int quali = (int) (Math.random() * 10) + 1;
			int salary = (int) (Math.random() * 1000 * quali) + 200 * quali;
			ApplicationProfile p = new ApplicationProfile(surnames[(int) (surnames.length * Math.random())] + ", "
					+ forenames[(int) (forenames.length * Math.random())], quali, jp, salary);
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
