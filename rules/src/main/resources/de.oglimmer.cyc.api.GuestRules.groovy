public class GuestRuleImpl implements de.oglimmer.cyc.api.IGuestRule {
    
	public org.slf4j.Logger log;
	
 	public java.util.Collection selectMenu(Object c) {
		def basePerc = 100;
		def ret = new ArrayList<>();
		for (def i = 0; i < 3; i++) {
			def me = selectSingleDishMenu(c, basePerc);
			if (me != null) {
				ret.add(me);
			}
		}
		return ret;
	}

	public def selectSingleDishMenu(def c, def basePerc) {
		def retry = 0;
		while (retry < 2) {
			def ind =(int)(c.getMenu().size() * Math.random());
			def foodSel = c.getMenu().get(ind);

			def base = basePerc * foodSel.getSecret().getValueForMoneyScore();
			def deli = foodSel.getSecret().getDeliciousness();
			base += deli;

			if (Math.random() * 100 < base) {
				return foodSel;
			}
			retry++;
		}
		return null;
	}

}