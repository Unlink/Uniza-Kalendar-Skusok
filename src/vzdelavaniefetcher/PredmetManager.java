/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package vzdelavaniefetcher;

import javax.inject.Inject;
import javax.inject.Named;
import vzdelavaniefetcher.tools.SimpleSerializedMappedSet;

/**
 *
 * @author Unlink
 */
public class PredmetManager {
	
	private Client aVzdelavanieKlient;
	
	private SimpleSerializedMappedSet<String, Integer> aZaznamyTerminov;

	@Inject
	public PredmetManager(Client aVzdelavanieKlient, @Named("predmetManager.storage") SimpleSerializedMappedSet<String, Integer> storage) {
		this.aVzdelavanieKlient = aVzdelavanieKlient;
		this.aZaznamyTerminov = storage;
	}
	
	/*
				if (!aZaznamyTerminov.contains(aMeno, t.getTerminId())) {
					t.setNovy(true);
					aZaznamyTerminov.add(aMeno, t.getTerminId());
				}
	*/
	
}
