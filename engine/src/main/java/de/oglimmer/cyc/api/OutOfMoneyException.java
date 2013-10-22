package de.oglimmer.cyc.api;

public class OutOfMoneyException extends GameException {

	private static final long serialVersionUID = -7880909137876706650L;
	private Company company;

	public OutOfMoneyException(Company company) {
		super();
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

}
