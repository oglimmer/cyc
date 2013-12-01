package de.oglimmer.cyc.api;

import lombok.Getter;
import lombok.Setter;

public class Employee {

	@Getter
	private String name;

	@Getter
	private int qualification;

	@Getter
	private JobPosition jobPosition;

	@Getter
	private int salary;

	@Getter
	@Setter
	private Establishment establishment;

	public Employee(String name, Establishment est, int qualification, JobPosition jobPosition, int salary) {
		super();
		this.name = name;
		this.qualification = qualification;
		this.jobPosition = jobPosition;
		this.salary = salary;
		this.establishment = est;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", qualification=" + qualification + ", jobPosition=" + jobPosition
				+ ", salary=" + salary + ", establishment=" + establishment.getAddress() + "]";
	}

}
