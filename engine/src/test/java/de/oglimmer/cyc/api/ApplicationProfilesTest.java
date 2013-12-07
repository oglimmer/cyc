package de.oglimmer.cyc.api;

import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.api.ApplicationProfile.Offer;

public class ApplicationProfilesTest {

	@Test
	public void getMaxOfferForTestSingle() {
		Game game = new Game(Constants.Mode.FULL);
		Company comp = new Company(game, "compA", null);
		Establishment est = new Establishment(comp, "cityA", 5, 10, 500, 2000);
		ApplicationProfile p = new ApplicationProfile("appA", 5, JobPosition.CHEF, 2000);
		ThreadLocal.setCompany(comp);
		int sal = p.getDesiredSalary() + 10;
		p.offer(est, sal);
		Entry<Company, Offer> en = ApplicationProfiles.getMaxOfferFor(p);
		Assert.assertSame(comp, en.getKey());
		Assert.assertSame(est, en.getValue().getEstablishment());
		Assert.assertEquals(sal, en.getValue().getSalary());
	}

	@Test
	public void getMaxOfferForTestDuo() {
		Game game = new Game(Constants.Mode.FULL);
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
		Entry<Company, Offer> en = ApplicationProfiles.getMaxOfferFor(p);
		Assert.assertSame(comp2, en.getKey());
		Assert.assertSame(est2, en.getValue().getEstablishment());
		Assert.assertEquals(sal2, en.getValue().getSalary());
	}

}
