package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Slf4j
@SuppressWarnings("unused")
@Ignore
public class EstablishmentRulesTest {

	@Test
	public void testWorstRestaurant() {
		Assert.assertEquals(3,
				(int) EstablishmentRule.INSTACE.getScore(new MokEstablishment(25, 0.0001, 1, 1, 1, 0), log));
	}

	@Test
	public void testAvgRestaurant() {
		Assert.assertEquals(25,
				(int) EstablishmentRule.INSTACE.getScore(new MokEstablishment(75, 0.8, 5, 3, 3, 0), log));
	}

	@Test
	public void testGoodRestaurant() {
		Assert.assertEquals(52, (int) EstablishmentRule.INSTACE.getScore(new MokEstablishment(175, 1, 7, 6, 6, 6), log));
	}

	@Test
	public void testTopRestaurant() {
		Assert.assertEquals(142,
				(int) EstablishmentRule.INSTACE.getScore(new MokEstablishment(250, 1.5, 10, 30, 30, 20), log));
	}

	class MokEstablishment {

		private int locationSize;
		private double valueForMoney;
		private int deliciousness;
		private int qualChef;
		private int qualWaiter;
		private int qualManager;

		public MokEstablishment(int locationSize, double valueForMoney, int deliciousness, int qualChef,
				int qualWaiter, int qualManager) {
			super();
			this.locationSize = locationSize;
			this.valueForMoney = valueForMoney;
			this.deliciousness = deliciousness;
			this.qualChef = qualChef;
			this.qualWaiter = qualWaiter;
			this.qualManager = qualManager;
		}

		public String getAddress() {
			return "address";
		}

		public Object getInteriorAccessories() {
			List<InteriorAccessory> list = new ArrayList<>();
			list.add(InteriorAccessory.COUNTER);
			return list;
		}

		public int getLocationSize() {
			return locationSize;
		}

		public Object getParent() {
			return new Object() {

				Object getMenu() {
					List<Object> list = new ArrayList<>();
					list.add(new Object() {

						double getValueForMoneyScore() {
							return valueForMoney;
						}

						int getDeliciousness() {
							return deliciousness;
						}

					});
					return list;
				}

				Object getHumanResources() {
					return new Object() {

						Object getSummary(Object est) {
							return new Object() {
								long get(JobPosition jobPosition) {
									switch (jobPosition) {
									case CHEF:
										return qualChef;
									case WAITER:
										return qualWaiter;
									case MANAGER:
										return qualManager;
									}
									return 0;
								}
							};
						}

					};
				}

			};
		}
	}
}
