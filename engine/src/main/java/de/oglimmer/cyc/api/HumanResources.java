package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.mozilla.javascript.RhinoException;

import de.oglimmer.cyc.collections.CycCollections;
import de.oglimmer.cyc.collections.JavaScriptList;
import de.oglimmer.cyc.util.CountMap;

@Slf4j
public class HumanResources {

	private List<Employee> employees = new ArrayList<>();

	public CallbackFunction hiringProcess;

	List<Employee> getEmployeesInt() {
		return employees;
	}

	public JavaScriptList<Employee> getEmployees() {
		return CycCollections.unmodifiableList(employees);
	}

	public JavaScriptList<Employee> getEmployees(String jp) {
		List<Employee> subList = new ArrayList<>();
		for (Employee e : employees) {
			if (e.getJobPosition().toString().equalsIgnoreCase(jp)) {
				subList.add(e);
			}
		}
		return CycCollections.unmodifiableList(subList);
	}

	Map<JobPosition, Long> getSummary(Establishment est) {
		CountMap<JobPosition> summary = new CountMap<>();
		for (JobPosition jp : JobPosition.values()) {
			summary.add(jp, 0);
		}
		for (Employee emp : est.getEmployees()) {
			summary.add(emp.getJobPosition(), emp.getQualification());
		}
		return summary;
	}

	public void layOff(Employee emp) {
		employees.remove(emp);
	}

	public void layOffAll(Establishment establishment) {
		for (Iterator<Employee> it = employees.iterator(); it.hasNext();) {
			Employee e = it.next();
			if (e.getEstablishment() == establishment) {
				it.remove();
			}
		}
	}

	void callHiringProcessCompany(Company company, ApplicationProfiles ap) {
		if (hiringProcess != null) {
			try {
				ThreadLocal.setCompany(company);
				hiringProcess.run(ap);
			} catch (RhinoException e) {
				if (!(e.getCause() instanceof GameException)) {
					company.getGame().getResult().addError(e);
					log.error("Failed to call the company.hiringProcess handler. Player " + company.getName()
							+ " bankrupt", e);
					company.setBankruptFromError(e);
				}
			}
		}
		ThreadLocal.resetCompany();
	}

	@Override
	public String toString() {
		return "HumanResources [employees=" + Arrays.toString(employees.toArray()) + "]";
	}

}
