package com.crawljax.plugins.domchange;

import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.configuration.ConfigurationException;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxController;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.CrawlSpecification;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.plugins.domchange.DomChangeNotifierImpl;

public class TestDomChangeNotifierImpl2 {
	
	public TestDomChangeNotifierImpl2() {
		// TODO Auto-generated constructor stub
		String name = "Test4AddingMutipleNodes.html";
		URL = this.getClass().getResource(name).getPath();
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	private static String URL = null;
	
	private static final int MAX_CRAWL_DEPTH = 3;
	private static final int MAX_STATES = 10;

	private static CrawlSpecification getCrawlSpecification() {
		
		

		CrawlSpecification crawler = new CrawlSpecification("file://" + URL);


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
		
	// adding postCrawlingPlugin to check the number of states
		
		PostCrawlImplForTest crawlImpl = new PostCrawlImplForTest();
		crawljaxConfiguration.addPlugin(crawlImpl);
	

		return crawljaxConfiguration;
	}

	/**
	 * @param args
	 *            none.
	 */

	public static void main(String[] args) {

	}

	
	
	
	@Test
	public void test() {
				
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

		assertTrue( com.crawljax.plugins.domchange.PostCrawlImplForTest.getNumberOfStates() == 2);
	}

}
