package de.oglimmer.cyc.dao;

import java.util.Collection;
import java.util.List;

import org.ektorp.support.GenericRepository;

import de.oglimmer.cyc.model.User;

public interface UserDao extends GenericRepository<User> {

	List<User> findByUsername(String username);

	List<User> findAllUser();

	List<User> findByEmail(String email);
	
	Collection<String> findByOpenSource(Collection<String> usernames);

	List<User> findByFBUserId(String userId);

	List<User> findByGoogleUserId(String userId);

	List<User> findByDiscordUserId(String userId);
}
