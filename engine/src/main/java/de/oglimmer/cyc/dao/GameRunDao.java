package de.oglimmer.cyc.dao;

import java.util.List;

import org.ektorp.support.GenericRepository;

import de.oglimmer.cyc.model.GameRun;

public interface GameRunDao extends GenericRepository<GameRun> {

	List<GameRun> findAllGameRun(int limit);

	int sizeAllGameRun();

}
