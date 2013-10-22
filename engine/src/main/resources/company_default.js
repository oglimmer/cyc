company.launch = function() {
	company.menu.add("Kebab", ["CHICKEN_MEAT", "BREAD"], 8);	
};

company.realEstateAgent = function(realEstateProfiles) {
	if(company.establishments.size() < 1) {
		realEstateProfiles.get(0).tryLease(0);
	}
};

company.humanResources.hiringProcess = function(applicationProfiles) {    
	if (company.humanResources.getEmployees("WAITER").size() < 1) {
		applicationProfiles.subList("WAITER").get(0).offer(
			company.establishments.get(0));
	}
	if (company.humanResources.getEmployees("CHEF").size() < 1) {
		applicationProfiles.subList("CHEF").get(0).offer(
			company.establishments.get(0));
	}
};

company.doMonthly = function() {
	if(company.establishments.size()==1) {
		company.establishments.get(0).buyInteriorAccessoriesNotExist("COUNTER");
	}	
};

company.doDaily = function() {
	company.grocer.order("CHICKEN_MEAT", 150);
	company.grocer.order("BREAD", 150);	
};


company.foodDelivery = function(foodDelivery) {
	foodDelivery.each(function(foodUnit) {
		foodUnit.distributeEqually();
	});
};