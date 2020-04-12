package org.nd.css;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

public class ParserService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
		
	CSSWriterSettings settings = new CSSWriterSettings (ECSSVersion.CSS30, false);
	CSSWriter cssWriter = new CSSWriter (settings);
	
	CSSDeclarationList decList;
	CSSDeclaration dec;
	
	
	public String process(String cssString){
		
		if(cssString==null || cssString.isEmpty())return cssString;		
		
		
		// prepare CascadingStyleSheet
		CascadingStyleSheet cascadingStyleSheet=null;		
		try {
			cascadingStyleSheet = CSSReader.readFromString(cssString, ECSSVersion.CSS30);
		} catch (Throwable e) {log.warn("CSS30 parsing failed");}
		
		if(cascadingStyleSheet==null){
			try {
				cascadingStyleSheet = CSSReader.readFromString(cssString, ECSSVersion.CSS21);
			} catch (Throwable e) {log.warn("CSS21 parsing failed");return cssString;}
		}

		
		DeclarationVisitor visitor = new DeclarationVisitor ();
		CSSVisitor.visitCSS(cascadingStyleSheet, visitor);
		
		try {
			cssString = cssWriter.getCSSAsString(cascadingStyleSheet);
		} catch (Throwable e) {log.error("failed to write new css");}	
	
	
		return cssString;
	}
	
	
	
	
	
}
