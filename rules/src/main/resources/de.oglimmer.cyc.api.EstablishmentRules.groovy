import de.oglimmer.cyc.api.JobPosition;
import de.oglimmer.cyc.api.InteriorAccessory;

public class Testablishmenter implements de.oglimmer.cyc.api.IEstablishmentRule {

	public org.slf4j.Logger log;

	public int getScore(Object establishment) {

		def summary = establishment.getParent().getHumanResources().getSummary(establishment);
		if (summary.get(JobPosition.CHEF) == 0 || summary.get(JobPosition.WAITER) == 0) {
			log.debug("No chef or waiter at {}", establishment.getAddress());
			return 0;
		}
		if (!InteriorAccessory.check(establishment.getInteriorAccessories(), InteriorAccessory.COUNTER)) {
			log.debug("No counter at {}", establishment.getAddress());
			return 0;
		}
		
		return 20;
	}

}

