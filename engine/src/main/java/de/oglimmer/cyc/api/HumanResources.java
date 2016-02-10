package de.oglimmer.cyc.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.RhinoException;

import de.oglimmer.cyc.collections.CycCollections;
import de.oglimmer.cyc.collections.JavaScriptList;
import de.oglimmer.cyc.util.CountMap;
import de.oglimmer.cyc.util.ExceptionConverter;
import de.oglimmer.cyc.util.LongMutable;
import de.oglimmer.cyc.util.PublicAPI;
import de.oglimmer.cyc.util.UndocumentedAPI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HumanResources implements IHumanResources {

	private List<Employee> employees = new ArrayList<>();

	public CallbackFunction hiringProcess;

	List<Employee> getEmployeesInt() {
		return employees;
	}

	@PublicAPI
	public JavaScriptList<Employee> getEmployees() {
		return CycCollections.unmodifiableList(employees);
	}

	@PublicAPI
	public JavaScriptList<Employee> getEmployees(String jp) {
		List<Employee> subList = new ArrayList<>();
		for (Employee e : employees) {
			if (e.getJobPosition().toString().equalsIgnoreCase(jp)) {
				subList.add(e);
			}
		}
		return CycCollections.unmodifiableList(subList);
	}

	@Override
	@UndocumentedAPI
	public Map<JobPosition, Long> getSummary(IEstablishment est) {
		CountMap<JobPosition> summary = new CountMap<>();
		for (JobPosition jp : JobPosition.values()) {
			summary.add(jp, 0);
		}
		for (IEmployee emp : est.getEmployees()) {
			summary.add(emp.getJobPosition(), emp.getQualification());
		}
		Map<JobPosition, Long> apiMap = new HashMap<>();
		for (Map.Entry<JobPosition, LongMutable> en : summary.entrySet()) {
			apiMap.put(en.getKey(), en.getValue().val);
		}
		return apiMap;
	}
	
	@PublicAPI
	public void layOff(Employee emp) {
		employees.remove(emp);
	}

	@PublicAPI
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
				long time = System.nanoTime();
				hiringProcess.run(ap);
				company.getGame().getResult().getCreateNotExists(company.getName())
						.addRunTime("hiringProcess", System.nanoTime() - time);
			} catch (RhinoException e) {
				if (!(e.getCause() instanceof GameException)) {
					String formattedStackTrace = ExceptionConverter.convertToString(e);
					company.getGame().getResult().addError(formattedStackTrace);
					log.error("Failed to call the company.hiringProcess handler. Player " + company.getName()
							+ " bankrupt", e);
					company.setBankruptFromError(formattedStackTrace);
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
