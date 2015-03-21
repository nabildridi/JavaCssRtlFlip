package css.rtl.CssRtlSwitcher;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class Main {
	
	static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {		
		
		File file=new File("test-css"+File.separator+"test.css");		
		
		ParserService parserService=new ParserService();
		
		String css=null;
		try {
			css = FileUtils.readFileToString(file);
			
			log.debug("before : ******************************************************************");
			log.debug(css);
			log.debug("******************************************************************");		
			
			String processedCss = parserService.process(css);
			
			log.debug("after : ******************************************************************");
			log.debug(processedCss);
			log.debug("******************************************************************");
		} catch (IOException e1) {}

	}

}
