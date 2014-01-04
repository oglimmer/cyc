package de.oglimmer.cyc.api;

import java.util.Collection;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Guest {

	public abstract Set<Company> getAlreadyVisited();

	public abstract void addAlreadyVisited(Company company);

	public void serve(CityProcessor cityProcessor) {
		GuestDispatcher guestDisp = cityProcessor.getGuestDispMngr().getDispatcher(this);
		if (guestDisp.hasRestaurants()) {
			Establishment est = guestDisp.getRandom();
			log.debug("A guest decided for {} by {}", est.getAddress(), est.getParent().getName());
			addAlreadyVisited(est.getParent());
			walkIntoRestaurant(cityProcessor, est);
		} else {
			log.debug("No restaurant found for {}", guestDisp.getCity());
		}
	}

	private void walkIntoRestaurant(CityProcessor cityProcessor, Establishment est) {
		Company company = est.getParent();
		Game game = company.getGame();
		game.getResult().get(company.getName()).getGuestsYouPerCity().add(cityProcessor.getCity().getName(), 1);
		try {
			if (company.getMenu().size() > 0) {
				selectMenu(cityProcessor, est);
			}
		} catch (MissingIngredient e) {
			game.getResult().get(company.getName()).addGuestsOutOfIngPerCity(cityProcessor.getCity().getName());
			game.getResult().get(company.getName()).addMissingIngredients(e.getMissingIngredients());
			log.debug("Unable to prepare meal, missing {} in {}", e.getMissingIngredients(), est.getAddress());
		}
	}

	private void selectMenu(CityProcessor cityProcessor, Establishment est) throws MissingIngredient {
		Collection<MenuEntry> foodSelCol = selectMenuWrapper(est.getParent());
		if (foodSelCol.isEmpty()) {
			est.getParent().getGame().getResult().get(est.getParent().getName()).getGuestsLeftPerCity()
					.add(cityProcessor.getCity().getName(), 1);
			log.debug("Guest went to {} in {} and ordered nothing", est.getParent().getName(), est.getAddress());
		} else {
			for (MenuEntry menu : foodSelCol) {
				cityProcessor.serveDish(est, menu);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<MenuEntry> selectMenuWrapper(Company c) {
		return (Collection<MenuEntry>) GuestRule.INSTACE.selectMenu(c);
	}

}
