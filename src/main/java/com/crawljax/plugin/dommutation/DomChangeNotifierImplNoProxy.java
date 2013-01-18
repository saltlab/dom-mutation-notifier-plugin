/**
 * An implementation of the crwaljax's DomChangeNotifierPlugin
 * 
 * This implementation utilizes mutation Summary library to improve the performance of the crawling.
 */
package com.crawljax.plugin.dommutation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.DomChangeNotifierPlugin;
import com.crawljax.core.state.Eventable;

/**
 * @author alireza
 * 
 */
public class DomChangeNotifierImplNoProxy implements DomChangeNotifierPlugin {
	private static final Logger LOGGER = Logger
			.getLogger(DomChangeNotifierImplNoProxy.class.getName());
	public static String mutationSummary;
	public static String JavaScritpCode;

	public DomChangeNotifierImplNoProxy(
			CrawljaxConfiguration crawljaxConfiguration) {

		// reading mutation_summary library so as to inject it to Web pages.
		try {
			mutationSummary = readFileAsString("target/classes/mutation_summary.js");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer jsElementContent = new StringBuffer(
				"\n\n console.log(\"inside javaScriopt injecting code: Saltlab\");");

		// Storing the mutation flag in browser for duration of the session
		jsElementContent
		.append("\n\n sessionStorage.setItem('domHasChanged', 'false');");
		jsElementContent.append("\n\n reportCounter = 0;");
		jsElementContent
		.append("\n\n var observer = new MutationSummary({ callback : handleChanges,\n queries : [{ all : true }]}); ");
		jsElementContent
		.append("\n\n function handleChanges(summaries) {\n	sessionStorage.setItem('domHasChanged', 'true');\n reportCounter++;\n console.log(\"domChangeReport\");\n } ");

		JavaScritpCode = jsElementContent.toString();

		OnUrlLoadImplforInjactingJS pl = new OnUrlLoadImplforInjactingJS();

		crawljaxConfiguration.addPlugin(pl);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.crawljax.core.plugin.DomChangeNotifierPlugin#isDomChanged(java.lang
	 * .String, com.crawljax.core.state.Eventable, java.lang.String,
	 * com.crawljax.browser.EmbeddedBrowser)
	 */
	@Override
	public boolean isDomChanged(String domBefore, Eventable e, String domAfter,
			EmbeddedBrowser browser) {

		LOGGER.warn("retrieving DOM mutation flag by executing JS ");

		// code to attach Mutation Summary and our JS code to the Head Element
		StringBuffer buffer = new StringBuffer(
				" if(!document.getElementById(\"SaltLabJavaScript\")){\n ");
		buffer.append(" var jsContent = document.createTextNode(\""
				+ mutationSummary + "\");\n");
		buffer.append("\t var JsElement = document.createElement(\'script\');\n");
		buffer.append("\t JsElement.setAttribute(\'type\',\'text/javascript\');\n");
		buffer.append("\t JsElement.setAttribute(\'id\',\'SaltLabJavaScript\');\n");
		buffer.append("\t JsElement.appendChild(jsContent);\n");
		buffer.append("\t document.getElementsByTagName(\"head\")[0].appendChild(JsElement);\n");

		buffer.append(" var jsContent2 = document.createTextNode(\""
				+ JavaScritpCode + "\");\n");
		buffer.append("\t var JsElement2 = document.createElement(\'script\');\n");
		buffer.append("\t JsElement2.setAttribute(\'type\',\'text/javascript\');\n");
		buffer.append("\t JsElement2.setAttribute(\'id\',\'SaltLabJavaScript2\');\n");
		buffer.append("\t JsElement2.appendChild(jsContent2);\n");
		buffer.append("\t document.getElementsByTagName(\"head\")[0].appendChild(JsElement2);\n");

		buffer.append("}");

		String script = buffer.toString();
		try {
			Object object = browser.executeJavaScript(script);

			String res = new String((String) object);
			System.out.println(res);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		boolean domHasChanged = true;
		String result = null;

		try {

			Object obj = browser
					.executeJavaScript(" return sessionStorage.getItem('domHasChanged');");
			browser.executeJavaScript("sessionStorage.setItem('domHasChanged', 'false');");

			if (obj != null) {
				result = obj.toString();
			} else {

				LOGGER.warn("retrieving falg by executing JS returned null!! ");
			}

		} catch (CrawljaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (result != null) {
			if (result.toLowerCase().equals("false")) {
				domHasChanged = false;
				LOGGER.warn("MutationPlugin: No mutation in DOM");
			} else {

				domHasChanged = true;
				LOGGER.warn("Dom has been mutated.");

			}

		}
		
		if (domHasChanged == false)

			// no change reported by mutation summary library api

			return false;
		else {
			// mutation summary library reports some change so in order to make
			// sure the degree to which dom has
			// changed is large enough to consider a new state, default dom
			// comparison is required
			// however, as we pass only raw DOM objects to this method and we do not pass the states to this method, (we deprived the implementors by choice )
			// the follwoing compariosn might be less efficient than crawljax default comparison.
			
			// to do
			
			// check if we can implement the default comparison here without changing the method interface 
			
			
			boolean isChanged = false;

			isChanged = !domAfter.equals(domBefore);
			if (isChanged) {
				LOGGER.info("Dom is Changed!");
			} else {
				LOGGER.info("Dom Not Changed!");
			}

			return isChanged;
	
		}

	}

	public static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

}
