package de.oglimmer.cyc.api;

import java.util.HashMap;
import java.util.Map;

import lombok.Value;

public class ApplicationProfile {

	private String name;

	private int qualification;
	private JobPosition jobPosition;

	private int desiredSalary;
	private Map<Company, Offer> offeredSalary;

	public ApplicationProfile(String name, int qualification, JobPosition jobPosition, int desiredSalary) {
		super();
		this.name = name;
		this.qualification = qualification;
		this.jobPosition = jobPosition;
		this.desiredSalary = desiredSalary;
		init();
	}

	void init() {
		offeredSalary = new HashMap<>();
	}

	public void offer(Establishment est) {
		offer(est, desiredSalary);
	}

	public void offer(Establishment est, int salary) {
		Company company = ThreadLocal.getCompany();
		assert est.getParent() == company;
		this.offeredSalary.put(company, new Offer(est, salary));
	}

	Map<Company, Offer> getOfferedSalary() {
		return offeredSalary;
	}

	public String getName() {
		return name;
	}

	public int getQualification() {
		return qualification;
	}

	public JobPosition getJobPosition() {
		return jobPosition;
	}

	public int getDesiredSalary() {
		return desiredSalary;
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
