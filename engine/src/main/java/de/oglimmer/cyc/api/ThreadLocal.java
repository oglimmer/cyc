package de.oglimmer.cyc.api;

public class ThreadLocal {

	private ThreadLocal() {
		// no code here
	}

	private static java.lang.ThreadLocal<Company> threadLocal = new java.lang.ThreadLocal<>();

	public static Company getCompany() {
		Company company = threadLocal.get();
		assert company != null;
		return company;
	}

	public static void setCompany(Company company) {
		assert company != null;
		threadLocal.set(company);
	}

	public static void resetCompany() {
		threadLocal.set(null);
	}

}
