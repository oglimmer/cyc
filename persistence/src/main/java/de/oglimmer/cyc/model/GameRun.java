package de.oglimmer.cyc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ektorp.support.CouchDbDocument;

import de.oglimmer.cyc.api.GameResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents one execution of a single/competitive game. Information influenced by coding from players are all in
 * "result".
 * 
 * @author oli
 * 
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class GameRun extends CouchDbDocument {

	private static final long serialVersionUID = 1L;

	private GameResult result = new GameResult();
	private Date startTime;
	private Date endTime;
	private long memUsed;
	private List<String> participants = new ArrayList<>();

}
