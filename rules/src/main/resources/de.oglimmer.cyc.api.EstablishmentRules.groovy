def summary = thiz.getParent().getHumanResources().getSummary(thiz);
if (summary.get(JobPosition.CHEF) == 0 || summary.get(JobPosition.WAITER) == 0) {
	log.debug("No chef or waiter at {}", thiz.getAddress());
	return 0;
}
if (!InteriorAccessory.check(thiz.getInteriorAccessories(), InteriorAccessory.COUNTER)) {
	log.debug("No counter at {}", thiz.getAddress());
	return 0;
}

return 20;