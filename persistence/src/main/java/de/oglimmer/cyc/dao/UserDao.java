package de.oglimmer.cyc.dao;

import java.util.List;

import org.ektorp.support.GenericRepository;

import de.oglimmer.cyc.model.User;

public interface UserDao extends GenericRepository<User> {

	List<User> findByUsername(String username);

	List<User> findAllUser();

	List<User> findByEmail(String email);

	/**
	 * Number of rows with "openSource==1" and a matching username
	 * 
	 * @param username
	 * @return
	 */
	int findByOpenSource(String username);

	List<User> findByFBUserId(String userId);

	List<User> findByGoogleUserId(String userId);
}
