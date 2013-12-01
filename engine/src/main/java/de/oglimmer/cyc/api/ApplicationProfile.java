package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

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
		Company company = ThreadLocal.getCompany();
		assert est.getParent() == company;
		this.offeredSalary.put(company, new Offer(est, salary));
	}

	@Override
	public String toString() {
		return "ApplicationProfile [name=" + name + ", qualification=" + qualification + ", jobPosition=" + jobPosition
				+ ", desiredSalary=" + desiredSalary + "]";
	}

	@Value
	class Offer {
		private Establishment establishment;
		private int salary;
	}
}
