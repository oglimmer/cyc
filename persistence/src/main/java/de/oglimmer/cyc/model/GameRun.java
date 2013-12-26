package de.oglimmer.cyc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.annotate.JsonProperty;

import de.oglimmer.cyc.api.GameResult;

/**
 * Represents one execution of a single/competitive game. Information influenced by coding from players are all in
 * "result".
 * 
 * @author oli
 * 
 */
@NoArgsConstructor
@Data
public class GameRun {

	private String id;
	private String revision;
	private GameResult result = new GameResult();
	private Date startTime;
	private Date endTime;
	private long memUsed;
	private List<String> participants = new ArrayList<>();

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
