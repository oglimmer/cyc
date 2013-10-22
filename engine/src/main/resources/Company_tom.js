var c = company;

c.launch = function() {
	var menu = company.menu;
	menu.add("doner", ["CHICKEN_MEAT", "SALAD", "BREAD"], 6);	
	menu.add("double-doner", 
	    ["CHICKEN_MEAT", "CHICKEN_MEAT", "SALAD", "BREAD"], 12);	
};

c.realEstateAgent = function(realEstateProfiles) {
	if (c.establishments.size() === 0) {
		realEstateProfiles.sortByLeaseCost().get(0).tryLease(100);
	}
};

c.humanResources.hiringProcess = function(applicationProfiles) {
    
	if (c.humanResources.getEmployees("WAITER").size() < 1) {
		applicationProfiles.subList("WAITER").sortBySalary().get(0).offer(
		    c.establishments.get(0));
	}
	if (c.humanResources.getEmployees("CHEF").size() < 1) {
		applicationProfiles.subList("CHEF").sortBySalary().get(0).offer(
		    c.establishments.get(0));
	}
};

c.doMonthly = function() {
	if(company.establishments.size()==1) {
		var est = c.establishments.iterator().next();
		est.buyInteriorAccessoriesNotExist("COUNTER");
	}	
};

c.doWeekly = function() {
	out.println("my money:"+company.cash);
};

c.doDaily = function() {
    c.grocer.order("CHICKEN_MEAT", 100);
    c.grocer.order("SALAD", 100);
    c.grocer.order("BREAD", 100);	
};

c.foodDelivery = function(foodUnits) {
	foodUnits.each(function(foodUnit) {
        foodUnit.distributeEqually();
	});
};