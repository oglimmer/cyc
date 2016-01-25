package de.oglimmer.cyc.api;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.DataProviderMemory;
import de.oglimmer.cyc.api.RealEstateProfile.RealEstateOffer;

public class RealEstateProfilesTest {
	@Test
	public void getMaxOfferForTestSingleLease() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp = new Company(game, "compA", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp);
		int bribe = 10;
		rep.tryLease(bribe);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp, co.getCompany());
		Assert.assertSame(bribe, co.getOffer().getBribe());
		Assert.assertEquals(false, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestTwoLease() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp1);
		int bribe1 = 10;
		rep.tryLease(bribe1);

		ThreadLocal.setCompany(comp2);
		int bribe2 = 20;
		rep.tryLease(bribe2);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp2, co.getCompany());
		Assert.assertSame(bribe2, co.getOffer().getBribe());
		Assert.assertEquals(false, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestThreeLease() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		Company comp3 = new Company(game, "compC", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp1);
		int bribe1 = 10;
		rep.tryLease(bribe1);

		ThreadLocal.setCompany(comp2);
		int bribe2 = 20;
		rep.tryLease(bribe2);

		ThreadLocal.setCompany(comp3);
		int bribe3 = 5;
		rep.tryLease(bribe3);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp2, co.getCompany());
		Assert.assertSame(bribe2, co.getOffer().getBribe());
		Assert.assertEquals(false, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestSingleBuy() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp = new Company(game, "compA", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp);
		int bribe = 10;
		rep.tryAcquisition(bribe);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp, co.getCompany());
		Assert.assertSame(bribe, co.getOffer().getBribe());
		Assert.assertEquals(true, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestTwoBuy() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp1);
		int bribe1 = 10;
		rep.tryAcquisition(bribe1);

		ThreadLocal.setCompany(comp2);
		int bribe2 = 20;
		rep.tryAcquisition(bribe2);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp2, co.getCompany());
		Assert.assertSame(bribe2, co.getOffer().getBribe());
		Assert.assertEquals(true, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestThreeBuy() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		Company comp3 = new Company(game, "compC", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp1);
		int bribe1 = 10;
		rep.tryAcquisition(bribe1);

		ThreadLocal.setCompany(comp2);
		int bribe2 = 20;
		rep.tryAcquisition(bribe2);

		ThreadLocal.setCompany(comp3);
		int bribe3 = 5;
		rep.tryAcquisition(bribe3);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp2, co.getCompany());
		Assert.assertSame(bribe2, co.getOffer().getBribe());
		Assert.assertEquals(true, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestTwoMixed() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp1);
		int bribe1 = 0;
		rep.tryAcquisition(bribe1);

		ThreadLocal.setCompany(comp2);
		int bribe2 = 20;
		rep.tryLease(bribe2);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp1, co.getCompany());
		Assert.assertSame(bribe1, co.getOffer().getBribe());
		Assert.assertEquals(true, co.getOffer().isBuy());
	}

	@Test
	public void getMaxOfferForTestThreeMixed() {
		Game game = new Game(Constants.Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp1 = new Company(game, "compA", null);
		Company comp2 = new Company(game, "compB", null);
		Company comp3 = new Company(game, "compC", null);
		RealEstateProfile rep = new RealEstateProfile("cityA", 1000, 200, 5, 5);

		ThreadLocal.setCompany(comp1);
		int bribe1 = 10;
		rep.tryAcquisition(bribe1);

		ThreadLocal.setCompany(comp2);
		int bribe2 = 20;
		rep.tryLease(bribe2);

		ThreadLocal.setCompany(comp3);
		int bribe3 = 5;
		rep.tryAcquisition(bribe3);

		RealEstateOffer co = rep.getMaxOfferFor();
		Assert.assertSame(comp1, co.getCompany());
		Assert.assertSame(bribe1, co.getOffer().getBribe());
		Assert.assertEquals(true, co.getOffer().isBuy());
	}

}
