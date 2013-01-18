package com.crawljax.plugin.dommutation;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawljaxException;
import com.crawljax.core.plugin.OnUrlLoadPlugin;

public class OnUrlLoadImplforInjactingJS implements OnUrlLoadPlugin {
	

	@Override
	public void onUrlLoad(EmbeddedBrowser browser) {
		// TODO Auto-generated method stub
		
		
		// code to attach Mutation Summary and our JS code to the Head Element
		StringBuffer buffer = new StringBuffer( " if(!document.getElementById(\"SaltLabJavaScript\")){\n "); 
		buffer.append(" var jsContent = document.createTextNode(\"" + DomChangeNotifierImplNoProxy.mutationSummary + "\");\n");				
		buffer.append("\t var JsElement = document.createElement(\'script\');\n");
		buffer.append("\t JsElement.setAttribute(\'type\',\'text/javascript\');\n");
		buffer.append("\t JsElement.setAttribute(\'id\',\'SaltLabJavaScript\');\n");
		buffer.append("\t JsElement.appendChild(jsContent);\n");
		buffer.append("\t document.getElementsByTagName(\"head\")[0].appendChild(JsElement);\n");
		
		buffer.append(" var jsContent2 = document.createTextNode(\"" + DomChangeNotifierImplNoProxy.JavaScritpCode + "\");\n");				
		buffer.append("\t var JsElement2 = document.createElement(\'script\');\n");
		buffer.append("\t JsElement2.setAttribute(\'type\',\'text/javascript\');\n");
		buffer.append("\t JsElement2.setAttribute(\'id\',\'SaltLabJavaScript2\');\n");
		buffer.append("\t JsElement2.appendChild(jsContent2);\n");
		buffer.append("\t document.getElementsByTagName(\"head\")[0].appendChild(JsElement2);\n");
		
		buffer.append("}");

		
		String script = buffer.toString();
		try {
			Object object =
					browser.executeJavaScript(script);

		
		} catch (CrawljaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
