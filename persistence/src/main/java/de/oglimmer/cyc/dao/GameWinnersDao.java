package de.oglimmer.cyc.dao;

import java.util.Date;
import java.util.List;

import org.ektorp.support.GenericRepository;

import de.oglimmer.cyc.model.GameWinners;

public interface GameWinnersDao extends GenericRepository<GameWinners> {

	List<GameWinners> findAllGameWinners(int limit, Date maxDate);

}
