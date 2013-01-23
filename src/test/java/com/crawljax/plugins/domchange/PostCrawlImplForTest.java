/**
 * 
 */
package com.crawljax.plugins.domchange;

import com.crawljax.core.CrawlSession;
import com.crawljax.core.plugin.PostCrawlingPlugin;

/**
 * @author arz
 *
 */
public class PostCrawlImplForTest implements PostCrawlingPlugin {
	
	private static int NumberOfStates = 0;

	/* (non-Javadoc)
	 * @see com.crawljax.core.plugin.PostCrawlingPlugin#postCrawling(com.crawljax.core.CrawlSession)
	 */
	@Override
	public void postCrawling(CrawlSession session) {
		// TODO Auto-generated method stub
		int nS = session.getStateFlowGraph().getAllStates().size();
		setNumberOfStates( nS);
				

	}
	
	
	public static int getNumberOfStates() {
		return NumberOfStates;
	}
	
	public static void setNumberOfStates(int numberOfStates) {
		NumberOfStates = numberOfStates;
	}


}
