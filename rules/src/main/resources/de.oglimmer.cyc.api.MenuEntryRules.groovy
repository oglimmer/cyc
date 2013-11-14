import de.oglimmer.cyc.api.Food;

public class Testablishmenter implements de.oglimmer.cyc.api.IMenuEntryRule {
	
	public org.slf4j.Logger log;
	
	public int getDeliciousness(Object ingredients, Object price) {		
		return ingredients.size();
	}
	
};