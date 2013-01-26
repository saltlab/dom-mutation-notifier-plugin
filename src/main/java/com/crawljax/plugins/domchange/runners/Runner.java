package com.crawljax.plugins.domchange.runners;

import org.apache.commons.configuration.ConfigurationException;
import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxController;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.CrawlSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.plugins.domchange.DomChangeNotifierImpl;

public final class Runner {
	
	private static final int MAX_CRAWL_DEPTH = 3;
	private static final int MAX_STATES = 10;

	private static CrawlSpecification getCrawlSpecification() {

		CrawlSpecification crawler = new CrawlSpecification("http://localhost/pluginTestFourth.htm");

		// elements to be clicked on
		crawler.click("div");
		crawler.click("h1");
		crawler.click("h2");
		crawler.click("h3");
		crawler.click("img");
		crawler.click("p");
		crawler.click("td");
		crawler.click("a");
		crawler.click("button");
		crawler.click("input").withAttribute("type", "submit");
		crawler.click("input").withAttribute("type", "button");
		crawler.click("span");

		// limit the crawling scope
		crawler.setMaximumStates(MAX_STATES);
		crawler.setDepth(MAX_CRAWL_DEPTH);
		// crawler.setMaximumRuntime( 10 * 60);

		return crawler;
	}

	private static CrawljaxConfiguration getConfig() {

		CrawljaxConfiguration crawljaxConfiguration = new CrawljaxConfiguration();
		crawljaxConfiguration.setBrowser(BrowserType.firefox);
		crawljaxConfiguration.setCrawlSpecification(getCrawlSpecification());

		// adding the plug-in
		DomChangeNotifierImpl MNPlugin = new DomChangeNotifierImpl(
				crawljaxConfiguration);
		crawljaxConfiguration.addPlugin(MNPlugin);

		return crawljaxConfiguration;
	}

	/**
	 * @param args
	 *            none.
	 */

	public static void main(String[] args) {
		try {
			CrawljaxController crawljax = new CrawljaxController(getConfig());
			crawljax.run();

		} catch (CrawljaxException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(1);

		}

	}

}
