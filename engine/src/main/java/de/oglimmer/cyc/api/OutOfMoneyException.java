package de.oglimmer.cyc.api;

public class OutOfMoneyException extends GameException {

	private static final long serialVersionUID = 1L;
	private final Company company;

	public OutOfMoneyException(Company company) {
		super();
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

}
