package com.crawljax.plugins.domchange;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.plugin.OnUrlLoadPlugin;

public class OnUrlLoadJsInjectoinNoProxy implements OnUrlLoadPlugin {
	
//	private static final Logger LOGGER = Logger.getLogger(OnUrlLoadJsInjectoinNoProxy.class.getName());

	@Override
	public void onUrlLoad(EmbeddedBrowser browser) {

		// attaching Mutation Summary and our JS code to the Head Element
		StringBuffer buffer = new StringBuffer( " if(!document.getElementById(\"SaltLabJavaScript\")){ "); 		
		buffer.append("  return true;} else { return false;} ");
		
		String script = buffer.toString();
		String results = null;
		try {
			Object object =
					browser.executeJavaScript(script);
			
			if (object!= null)
			{
				results =  object.toString();
				if (results.equalsIgnoreCase("true"))
				{
					buffer = new StringBuffer();
					buffer.append(" var JsElement = document.createElement(\'script\'); ");
					buffer.append(" JsElement.setAttribute(\'type\',\'text/javascript\'); ");
					buffer.append(" JsElement.setAttribute(\'id\',\'SaltLabJavaScript\'); ");
					buffer.append(" JsElement.setAttribute(\'src\',\'http://localhost/js/test.js\'); ");			
					buffer.append(" document.getElementsByTagName(\"head\")[0].appendChild(JsElement); ");
					
					String string = buffer.toString();
					browser.executeJavaScript(string);

					buffer = new StringBuffer();
					buffer.append(" var JsElement2 = document.createElement('script'); ");
					buffer.append(" JsElement2.setAttribute('type','text/javascript'); ");
					buffer.append(" JsElement2.setAttribute('id','SaltLabJavaScript2'); ");
					buffer.append(" JsElement2.innerHTML = ' sessionStorage.setItem(\\'domHasChanged\\' , \\'false\\');	var observer = new MutationSummary({ callback : handleChanges, queries : [{ all : true }]}); function handleChanges(summaries) {	sessionStorage.setItem(\\'domHasChanged\\', \\'true\\');  }	' ; ");
					buffer.append(" document.getElementsByTagName('head')[0].appendChild(JsElement2); ");

					
					string = buffer.toString();
					browser.executeJavaScript(string);					
				}
			}
			else{
				
//				LOGGER.warn("retrieving Injectionfalg by executing JS returned null!! ");
			}
		} catch (CrawljaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
