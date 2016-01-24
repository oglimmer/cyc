package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.oglimmer.cyc.collections.ForEach;
import de.oglimmer.cyc.collections.SortableContainer;
import de.oglimmer.cyc.collections.UnmodifiableIterator;
import de.oglimmer.cyc.util.PublicAPI;

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
	
	@PublicAPI
	public ApplicationProfile get(int index) {
		try {
			return profiles.get(index);
		} catch (IndexOutOfBoundsException e) {
			return new ApplicationProfile("Nobody", 0, null, 0);
		}
	}

	@PublicAPI
	public ApplicationProfiles subList(String jobPosition) {
		List<ApplicationProfile> newList = new ArrayList<>();
		for (ApplicationProfile ap : profiles) {
			if (ap.getJobPosition().toString().equalsIgnoreCase(jobPosition)) {
				newList.add(ap);
			}
		}
		return new ApplicationProfiles(newList);
	}

	@PublicAPI
	@Override
	public int size() {
		return profiles.size();
	}

	@PublicAPI
	@Override
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
	
	@PublicAPI
	public ApplicationProfiles sortByQualification() {
		Collections.sort(profiles, new Comparator<ApplicationProfile>() {
			@Override
			public int compare(ApplicationProfile o1, ApplicationProfile o2) {
				return Integer.valueOf(o1.getQualification()).compareTo(Integer.valueOf(o2.getQualification()));
			}
		});
		return this;
	}

	@PublicAPI
	public ApplicationProfiles sortBySalary() {
		Collections.sort(profiles, new Comparator<ApplicationProfile>() {
			@Override
			public int compare(ApplicationProfile o1, ApplicationProfile o2) {
				return Integer.valueOf(o1.getDesiredSalary()).compareTo(Integer.valueOf(o2.getDesiredSalary()));
			}
		});
		return this;
	}

	@PublicAPI
	@Override
	public Iterator<ApplicationProfile> iterator() {
		return new UnmodifiableIterator<ApplicationProfile>(profiles.iterator());
	}

	Iterator<ApplicationProfile> iteratorInt() {
		return profiles.iterator();
	}

	@Override
	public String toString() {
		return "ApplicationProfiles [profiles=" + Arrays.toString(profiles.toArray()) + "]";
	}

}
