package de.oglimmer.cyc.api;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.oglimmer.cyc.DataProviderMemory;
import de.oglimmer.cyc.api.Constants.Mode;

public class HumanResourcesTest {

	@Test
	public void summaryTest() {
		Game game = new Game(Mode.FULL, DataProviderMemory.INSTANCE);
		Company comp = new Company(game, "comp", new Grocer(game));
		HumanResources hr = comp.getHumanResources();
		Establishment est = new Establishment(comp, "city", 5, 5, 1000, 2000);
		hr.getEmployeesInt().add(new Employee("a", est, 5, JobPosition.CHEF, 1000));
		hr.getEmployeesInt().add(new Employee("b", est, 7, JobPosition.CHEF, 1000));
		hr.getEmployeesInt().add(new Employee("c", est, 9, JobPosition.CHEF, 1000));
		hr.getEmployeesInt().add(new Employee("d", est, 11, JobPosition.WAITER, 1000));
		Map<JobPosition, Long> sum = hr.getSummary(est);
		Assert.assertEquals(21, (long) sum.get(JobPosition.CHEF));
		Assert.assertEquals(11, (long) sum.get(JobPosition.WAITER));
	}
}
