package se.liu.tdp021.gravityGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class LevelSelector {

	protected static String TAG = "LevelSelector";

	public ArrayList<HashMap<String,String>> levelInfo = null;
	
	//Note about both getLevel... functions; just pass 'this' from an Activity as resources
	
	public ArrayList<HashMap<String,String>> getLevelList(Resources resources) {
		
		try
		{
			XmlResourceParser parser = resources.getXml(R.xml.levels);  //Hard-coded 'levels' file, static file
			int levelcounter = 0;

			levelInfo = new ArrayList<HashMap<String,String>>();

			
			while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				
				if (parser.getEventType() == XmlResourceParser.START_TAG) {
					
					if (parser.getName().equals("level")) {
						HashMap<String,String> levelEntry = new HashMap<String,String>();
						levelEntry.put("name", parser.getAttributeValue(null, "name"));
						levelEntry.put("description", parser.getAttributeValue(null, "description"));
						levelEntry.put("data", parser.getAttributeValue(null, "data"));
						
						levelcounter++;
						
						Log.e(TAG, ("Tag '" + parser.getName() + "': name = " + parser.getAttributeValue(null, "name") + ", desc = " + parser.getAttributeValue(null, "description") + ", data = " + parser.getAttributeValue(null, "data") + ", type = " + parser.getAttributeValue(null, "data").getClass().getName()));
						
						
						if (levelEntry.containsValue(null))  //getAttributeValue returns null on failed data get
						{	
							Log.e(TAG, "NULL found in levelEntry, discarding level #" + Integer.toString(levelcounter) + "' (index i-1)");
						}
						else
						{
							levelInfo.add(levelEntry);  //Seemingly successful read, add completed entry to list
							Log.e(TAG, "Successful read at index " + Integer.toString(levelcounter) + ", " + Integer.toString(levelInfo.size()) + " items so far");
						}
						
					}
				} //close start_tag event
				
				parser.next();
			}
			
			parser.close();
			
		} catch (XmlPullParserException parserError) {
			Log.e(TAG, ".getEventType or .next failed, 'probably bad file format' according to tutorial");
			parserError.toString();
		} catch (IOException IOerror) {
			Log.e(TAG, "Unable to read 'levels.xml', something's gone horribly wrong");
			IOerror.printStackTrace();
		}
		
		return levelInfo;
		
	}
	
	
}
