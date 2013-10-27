var c = company;
c.launch = function() {
	var menu = company.menu;
	menu.add("kebab", ["CHICKEN_MEAT", "SALAD", "BREAD"], 5);	
	menu.add("kebab-extra", ["CHICKEN_MEAT", "SALAD", "BREAD", "SPICES"], 15);	
};

c.realEstateAgent = function(realEstateProfiles) {
	if(c.establishments.size() < 1) {
		realEstateProfiles.sortByLeaseCost().get(0).tryLease(100);
	}
};

c.humanResources.hiringProcess = function(applicationProfiles) {
    
    var chef=false,waiter=false;
	applicationProfiles.each(function(applicationProfile) {
	   
		if (c.establishments.size() > 0 && applicationProfile.qualification > 0 ) {
			 
			if(!chef&&c.humanResources.getEmployees("CHEF").size() ==0 &&
			    applicationProfile.jobPosition == "CHEF"){
			    applicationProfile.offer(c.establishments.get(0), applicationProfile.desiredSalary);    
			    chef=true;
			}
			if(!waiter&&c.humanResources.getEmployees("WAITER").size() ==0 &&
			    applicationProfile.jobPosition == "WAITER"){
			    applicationProfile.offer(c.establishments.get(0), applicationProfile.desiredSalary);    
			    waiter=true;
			}
			
		}		
	});
};

c.doMonthly = function() {
	if(company.establishments.size()==1) {
		var est = company.establishments.iterator().next();
		est.buyInteriorAccessoriesNotExist("COUNTER");
	}	
};

c.doWeekly = function() {
	out.println("my money:"+company.cash);
	var ep = c.humanResources.getEmployees("CHEF");
	out.println(ep.size());
};

c.doDaily = function() {
	company.grocer.order("CHICKEN_MEAT", 100);
	company.grocer.order("SALAD", 100);
	company.grocer.order("BREAD", 100);	
	company.grocer.order("SPICES", 50);	
};

c.foodDelivery = function(foodUnits) {
	foodUnits.each(function(foodUnit) {
	    foodUnit.distributeEqually();
	});
};