import de.oglimmer.cyc.api.JobPosition;
import de.oglimmer.cyc.api.InteriorAccessory;

public class EstablishmentRuleImpl implements de.oglimmer.cyc.api.IEstablishmentRule {

	public org.slf4j.Logger log;

	int getScore(Object establishment) {
		if(!checkMinRequirements(establishment)) {
			return 0;
		}				
		return getScore(establishment,0);
	}

	double getScore(Object establishment, int type) {
		return 20;
	}
	
	boolean checkMinRequirements(Object establishment) {
		def summary = establishment.getParent().getHumanResources().getSummary(establishment);
		if (summary.get(JobPosition.CHEF) == 0 || summary.get(JobPosition.WAITER) == 0) {
			log.debug("No chef or waiter at {}", establishment.getAddress());
			return false;
		}
		if (!InteriorAccessory.check(establishment.getInteriorAccessories(), InteriorAccessory.COUNTER)) {
			log.debug("No counter at {}", establishment.getAddress());
			return false;
		}
		return true;
	}
	
}

