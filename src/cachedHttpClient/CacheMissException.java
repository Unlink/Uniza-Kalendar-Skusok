/*
 *  Copyright (c) 2015 Michal Ďuračík
 */
package cachedHttpClient;

/**
 *
 * @author Unlink
 */
class CacheMissException extends RuntimeException {

	public CacheMissException(String paString) {
		super(paString);
	}
	
}
