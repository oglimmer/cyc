package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	Map<JobPosition, Integer> getSummary(Establishment est) {
		Map<JobPosition, Integer> summary = new HashMap<>();
		for (JobPosition jp : JobPosition.values()) {
			summary.put(jp, 0);
		}
		for (Employee emp : employees) {
			if (emp.getEstablishment() == est) {
				int i = summary.get(emp.getJobPosition());
				i += emp.getQualification();
				summary.put(emp.getJobPosition(), i);
			}
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

	@Override
	public String toString() {
		return "HumanResources [employees=" + Arrays.toString(employees.toArray()) + "]";
	}

}
