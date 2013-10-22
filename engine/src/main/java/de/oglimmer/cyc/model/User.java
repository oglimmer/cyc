package de.oglimmer.cyc.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {

	private String id;
	private String revision;

	private String username;
	private String password;
	private String email;
	private boolean active;

	private String mainJavaScript;

	public User() {
	}

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	@JsonProperty("_id")
	public void setId(String s) {
		id = s;
	}

	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}

	@JsonProperty("_rev")
	public void setRevision(String s) {
		revision = s;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMainJavaScript() {
		return mainJavaScript;
	}

	public void setMainJavaScript(String mainJavaScript) {
		this.mainJavaScript = mainJavaScript;
	}

	public void setActive(boolean b) {
		this.active = b;
	}

	public boolean isActive() {
		return active;
	}

}
