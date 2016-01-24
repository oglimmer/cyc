package de.oglimmer.cyc.api;

import java.util.Map;

public interface IHumanResources {

	Map<JobPosition, Long> getSummary(IEstablishment establishment);

}
