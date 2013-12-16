package de.oglimmer.cyc.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonProperty;

@NoArgsConstructor
@Data
public class User {

	private String id;
	private String revision;
	private String username;
	private String password;
	private String email;
	private boolean active;
	private int permission;
	private String mainJavaScript;
	private String lastError;
	private Date lastPrivateRun;
	private int openSource;
	private Date lastLoginDate;
	private Date createdDate;
	private Date lastCodeChangeDate;

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
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("_rev")
	public String getRevision() {
		return revision;
	}

	@JsonProperty("_rev")
	public void setRevision(String revision) {
		this.revision = revision;
	}

}
