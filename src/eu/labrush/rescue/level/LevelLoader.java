package eu.labrush.rescue.level;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * @author adrienbocquet
 *
 */
public class LevelLoader {

	public void load(Level level, String file) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
        //factory.setValidating(true);
		
		try {
			SAXParser parser = factory.newSAXParser();
	         parser.parse("resources/"+file, new XMLHandler(level));

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
}