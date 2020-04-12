package org.nd.services;

import java.util.concurrent.Callable;

import org.nd.css.ParserService;
import org.nd.models.Css;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CssThread implements Callable<Css>{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ParserService parserService= new ParserService();
	
	private Css css;	
	
	public CssThread(Css css) {
		this.css = css;
	}

	
	@Override
	public Css call() { 	
		
		String processedCss = parserService.process(css.getOriginal());
		css.setConverted(processedCss);
		
		return css;
		
	}
	


	
	
	
}
