package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.DataProviderMemory;

public class ApplicationProfilesTest {

	@Test
	public void getMaxOfferForTestSingle() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp = new Company(game, "compA", null);
		Establishment est = new Establishment(comp, "cityA", 5, 10, 500, 2000);
		ApplicationProfile p = new ApplicationProfile("appA", 5, JobPosition.CHEF, 2000);
		ThreadLocal.setCompany(comp);
		int sal = p.getDesiredSalary() + 10;
		p.offer(est, sal);
		ApplicationProfile.CompanyOffer co = p.getMaxOfferFor();
		Assert.assertSame(comp, co.getCompany());
		Assert.assertSame(est, co.getOffer().getEstablishment());
		Assert.assertEquals(sal, co.getOffer().getSalary());
	}

	@Test
	public void getMaxOfferForTestDuo() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		Establishment est1 = new Establishment(comp1, "cityA", 5, 10, 500, 2000);
		Establishment est2 = new Establishment(comp2, "cityA", 5, 10, 500, 2000);
		ApplicationProfile p = new ApplicationProfile("appA", 5, JobPosition.CHEF, 2000);
		ThreadLocal.setCompany(comp1);
		int sal1 = p.getDesiredSalary() + 10;
		p.offer(est1, sal1);
		ThreadLocal.setCompany(comp2);
		int sal2 = p.getDesiredSalary() + 20;
		p.offer(est2, sal2);
		ApplicationProfile.CompanyOffer co = p.getMaxOfferFor();
		Assert.assertSame(comp2, co.getCompany());
		Assert.assertSame(est2, co.getOffer().getEstablishment());
		Assert.assertEquals(sal2, co.getOffer().getSalary());
	}

}
