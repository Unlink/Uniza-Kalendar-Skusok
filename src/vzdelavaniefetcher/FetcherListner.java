/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vzdelavaniefetcher;

import java.util.EventListener;

/**
 *
 * @author Unlink
 */
public interface FetcherListner extends EventListener {
    public void updateState(String paMessage, int paPercentage);
}
