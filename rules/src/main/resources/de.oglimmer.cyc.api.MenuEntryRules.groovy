import de.oglimmer.cyc.api.Food;

public class MenuEntryRuleImpl implements de.oglimmer.cyc.api.IMenuEntryRule {
	
	public org.slf4j.Logger log;
	
	public int getDeliciousness(Object ingredients) {		
		return getDeliciousness(ingredients, 0);
	}

	public int getDeliciousness(Object ingredients, int type) {		
		return ingredients.size();
	}
	
};