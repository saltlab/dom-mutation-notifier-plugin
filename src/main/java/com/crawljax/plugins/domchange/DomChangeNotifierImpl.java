/**
 * 
 */
package com.crawljax.plugins.domchange;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.plugin.DomChangeNotifierPlugin;
import com.crawljax.core.state.Eventable;

/**
 * @author arz
 * 
 */
public class DomChangeNotifierImpl implements DomChangeNotifierPlugin {

	public DomChangeNotifierImpl(CrawljaxConfiguration crawljaxConfiguration) {

		OnUrlLoadJsInjectoinNoProxy pl = new OnUrlLoadJsInjectoinNoProxy();
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
	public boolean isDomChanged(String domBefore, Eventable eventable, String domAfter,
			EmbeddedBrowser browser) {

		//attaching Mutation Summary and our JS code to the Head Element		
		// checking if our code is present. if not we will attach it and perform the 1 dom comparison
		StringBuffer buffer = new StringBuffer( " if(!document.getElementById(\"SaltLabJavaScript\")){ "); 
		buffer.append("  return true;} else { return false;} ");
		/**
		 * document.getElementById(\"SaltLabJavaScript\") will be null if our code is not present
		 * thus
		 * !document.getElementById(\"SaltLabJavaScript\") == true  will deduce that our code is not present hence, append it again
		 */
		String script = buffer.toString();
		String results =null;
		try {
			Object object =
					browser.executeJavaScript(script);

			if (object!= null)
			{	// the execution was successful		
				results =  object.toString();
//				LOGGER.debug("Js Injection falg: " + results);
				
				if (results.equalsIgnoreCase("false"))
				{
					// our code exists and we just need to retrieve the mutation flag
					String result = null;
					Object obj = browser.executeJavaScript(" return sessionStorage.getItem('domHasChanged');");
					browser.executeJavaScript("sessionStorage.setItem('domHasChanged', 'false');");
					
					if (obj!= null)
					{			
						result =  obj.toString();						
						if (result.toLowerCase().equals("false"))
						{
							// if Mutation summary reports no change then do not compare DOMs

							//							LOGGER.warn("MutationPlugin: No mutation in DOM");
							
							return false;
						}
						else{// if mutation falg is true
							// if our code exists but the mutation falg is true then we need to perform dom comparison without reinjection
							boolean defualt =
									defaultComparison(domBefore,eventable,domAfter);
							return defualt;							
						}
					}
				}
			}
			
			
			// our code does not exist so we need to append it and run the default domComparison
			buffer = new StringBuffer();
			buffer.append(" var JsElement = document.createElement(\'script\'); ");
			buffer.append(" JsElement.setAttribute(\'type\',\'text/javascript\'); ");
			buffer.append(" JsElement.setAttribute(\'id\',\'SaltLabJavaScript\'); ");
			buffer.append(" JsElement.setAttribute(\'src\',\'http://localhost/js/test.js\'); ");
			buffer.append(" document.getElementsByTagName(\"head\")[0].appendChild(JsElement); ");
			String string = buffer.toString();
			browser.executeJavaScript(string);

			buffer.append(" var JsElement2 = document.createElement('script'); ");
			buffer.append(" JsElement2.setAttribute('type','text/javascript'); ");
			buffer.append(" JsElement2.setAttribute('id','SaltLabJavaScript2'); ");
			buffer.append(" JsElement2.innerHTML = ' sessionStorage.setItem(\\'domHasChanged\\' , \\'false\\');	var observer = new MutationSummary({ callback : handleChanges, queries : [{ all : true }]}); function handleChanges(summaries) {	sessionStorage.setItem(\\'domHasChanged\\', \\'true\\');  }	' ; ");		

			buffer.append(" document.getElementsByTagName('head')[0].appendChild(JsElement2); ");

			buffer.append(" document.getElementsByTagName(\"head\")[0].appendChild(JsElement2); ");
			string = buffer.toString();
			browser.executeJavaScript(string);			

		} catch (CrawljaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}


		boolean defualt =
				defaultComparison(domBefore,eventable,domAfter);
		return defualt;




	}

	private boolean defaultComparison(String domBefore, Eventable eventable,
			String domAfter) {
	
		boolean isChanged = false;

		isChanged = !domAfter.equals(domBefore);
		if (isChanged) {
//			LOGGER.info("Dom is Changed!");
		} else {
//			LOGGER.info("Dom Not Changed!");
		}

		return isChanged;

	}

}
