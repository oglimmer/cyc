package de.oglimmer.cyc.api;

public class Employee {

	private String name;

	private int qualification;
	private JobPosition jobPosition;

	private int salary;

	private Establishment establishment;

	public Employee(String name, Establishment est, int qualification, JobPosition jobPosition, int salary) {
		super();
		this.name = name;
		this.qualification = qualification;
		this.jobPosition = jobPosition;
		this.salary = salary;
		this.establishment = est;
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

	public int getSalary() {
		return salary;
	}

	public Establishment getEstablishment() {
		return establishment;
	}

	public void setEstablishment(Establishment establishment) {
		this.establishment = establishment;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", qualification=" + qualification + ", jobPosition=" + jobPosition
				+ ", salary=" + salary + ", establishment=" + establishment.getAddress() + "]";
	}

}
