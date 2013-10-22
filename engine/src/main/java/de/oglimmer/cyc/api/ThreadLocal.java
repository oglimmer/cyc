package de.oglimmer.cyc.api;

public class ThreadLocal {

	private static java.lang.ThreadLocal<Company> threadLocal = new java.lang.ThreadLocal<>();

	public static Company getCompany() {
		return threadLocal.get();
	}

	public static void setCompany(Company company) {
		threadLocal.set(company);
	}

	public static void resetCompany() {
		threadLocal.set(null);
	}

}
