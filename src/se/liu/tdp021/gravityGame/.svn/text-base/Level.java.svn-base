package se.liu.tdp021.gravityGame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;


/*
 * May want to scrap the 'colours' hashmap in favour of int cl_<wall/spike/etc> public variables for faster lookup
 * Either that or have the "level builder" function extract the values as (effectively) constants before drawing things. 
 * 
 */

public class Level {
	
	protected static String TAG = "Level";
	
	public HashMap<String, Paint> colours = null;
	public Vector<StaticGameObject> walls = new Vector<StaticGameObject>();
	public Vector<StaticGameObject> spikes = new Vector<StaticGameObject>();
	public Vector<StaticGameObject> barriers = new Vector<StaticGameObject>();
	public Vector<StaticGameObject> flippers = new Vector<StaticGameObject>();
	public Vector<StaticGameObject> blockers = new Vector<StaticGameObject>();
	public Vector<StaticGameObject> messages = new Vector<StaticGameObject>();
	public Vector<String> textureNames = new Vector<String>();
	public String backgroundTextureName = null;
	public PointF bounds = null;
	public Point playerStartingLocation = null;
	public Direction playerStartingGravity = Direction.DOWN;
	public int playerFlipsAllowed = 2;
	public Vector<StaticGameObject> goals = new Vector<StaticGameObject>();
	public Vector<StaticGameObject> collectibles = new Vector<StaticGameObject>();

	
	
	//levelID is obtained from LevelSelector's results' "data" field
	public Level GetLevel(Resources resources, String levelID) {
		colours = new HashMap<String, Paint>();
		int debug_itemcounter = 0;
		try
		{
			Log.v(TAG, ("LevelID is " + levelID + ", result of getIdentifier is " + resources.getIdentifier(("se.liu.tdp021.gravityGame:xml/" + levelID), null, null)));
			XmlResourceParser parser = resources.getXml(resources.getIdentifier(("se.liu.tdp021.gravityGame:xml/" + levelID), null, null));  //Open the XML file containing the level data
			
			while (parser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				
					if (parser.getEventType() == XmlResourceParser.START_TAG) {
					
						if (parser.getName().equals("colour")) {
							/*TODO: When we have established a set of default colours (for the colour blind mode),
							 * alter the else condition for each colour below to set the default colour instead.
							 */							
							
							// Get a name for the background texture, if such is defined.
							backgroundTextureName = parser.getAttributeValue(null, "background_texture");
							
							
							Paint paint = new Paint();
							
							String cl = (String)parser.getAttributeValue(null, "wall");
							if (cl.length() == 9) {
								/*
								int temp = Integer.parseInt(cl.substring(0,3));
								String tempind = "ABCDEFGHI";
								Log.d(TAG, "Cl is " + cl + " class = " + cl.getClass().getName() + ", parsed is: " + temp + ", (" + tempind.substring(0,3));// + ", class " + (Integer.parseInt(cl.substring(0,3).getClass().getName()))));
								Log.d(TAG, "Sub2: " + (cl.substring(3,6)) + ", (" + tempind.substring(3,6));
								Log.d(TAG, "Sub2 parsed: " + Integer.parseInt(cl.substring(3,6))); 
								Log.d(TAG, "Sub3: " + (cl.substring(6,9)) + ", (" + tempind.substring(6,9));
								Log.d(TAG, "Sub3 parsed: " + Integer.parseInt(cl.substring(6,9))); 
								
								Log.w(TAG, "First: " + Integer.parseInt(cl.substring(0,3)));
								Log.w(TAG, "Second: " + Integer.parseInt(cl.substring(3,6)));
								Log.w(TAG, "Last: " + Integer.parseInt(cl.substring(6,9)));
								*/
								//paint.setARGB(255, 255, 0, 0);
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("wall", paint);
								Log.d(TAG, ("Colour for wall set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid wall colour parsed"); }
							
							cl = parser.getAttributeValue(null, "bg");
							if (cl.length() == 9) {
								paint = new Paint();
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("background", paint);
								Log.d(TAG, ("Colour for background set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid background colour parsed"); }
							
							
							cl = parser.getAttributeValue(null, "spike");
							if (cl.length() == 9) {
								paint = new Paint();
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("spike", paint);
								Log.d(TAG, ("Colour for spike set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid spike colour parsed"); }
							
			
							cl = parser.getAttributeValue(null, "player");
								if (cl.length() == 9) {
								paint = new Paint();
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("player", paint);
								Log.d(TAG, ("Colour for player set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid player colour parsed"); }
							
							
							cl = parser.getAttributeValue(null, "goal");
							if (cl.length() == 9) {
								paint = new Paint();
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("goal", paint);
								Log.d(TAG, ("Colour for goal set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid goal colour parsed"); }
							
							
							cl = parser.getAttributeValue(null, "barrier");
							if (cl.length() == 9) {
								paint = new Paint();
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("barrier", paint);
								Log.d(TAG, ("Colour for barrier set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid barrier colour parsed"); }
							
							
							cl = parser.getAttributeValue(null, "flipper");
							if (cl.length() == 9) {
								paint = new Paint();  //TODO: Remember:: Alpha value is not 255 for flippers
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
								colours.put("flipper", paint);
								Log.d(TAG, ("Colour for flipper set to " + cl));
							}
							else {Log.e(TAG, "WARNING: invalid flipper colour parsed"); }
						}
						
						else if (parser.getName().equals("settings")) {
							playerFlipsAllowed = parser.getAttributeIntValue(null, "flipsAllowed", playerFlipsAllowed);
						}
						
						else if (parser.getName().equals("message")) {
							String tc = (String)parser.getAttributeValue(null, "text_color");
							String bc = (String)parser.getAttributeValue(null, "background_color");
							
							Paint textColor = new Paint();
							textColor.setARGB(255, Integer.parseInt(tc.substring(0,3)), Integer.parseInt(tc.substring(3,6)), Integer.parseInt(tc.substring(6,9)));
							
							Paint bgColor = new Paint();
							bgColor.setARGB(255, Integer.parseInt(bc.substring(0,3)), Integer.parseInt(bc.substring(3,6)), Integer.parseInt(bc.substring(6,9)));
							
							PointF top = new PointF(parser.getAttributeFloatValue(null, "x1", 0.0f), parser.getAttributeFloatValue(null, "y1", 0.0f));
							//PointF bottom = new PointF(parser.getAttributeFloatValue(null, "x2", 0.0f), parser.getAttributeFloatValue(null, "y2", 0.0f));
							
							Direction dir = Direction.UNDEFINED;
							messages.add(new MessageBox(top, parser.getAttributeValue(null, "text"), textColor, bgColor, dir.toEnum(parser.getAttributeIntValue(null, "x2", 0))));
						}
						
						else if (parser.getName().equals("bounds")) {
							bounds = new PointF(parser.getAttributeFloatValue(null, "x", 0.0f), parser.getAttributeFloatValue(null, "y", 0.0f));	
						}
						
						else if (parser.getName().equals("player")) {
							playerStartingLocation = new Point(parser.getAttributeIntValue(null, "x", 100), parser.getAttributeIntValue(null, "y", 100));
							playerStartingGravity = GameplayActivity.gravity.toEnum(parser.getAttributeIntValue(null, "gravity", 0));
							Log.v(TAG, ("PStartLocation (" + playerStartingLocation.x + "," + playerStartingLocation.y + ") parsed"));
							Log.v(TAG, ("playerStartingGravity parsed as " + playerStartingGravity));
						}
						else if (parser.getName().equals("goal")) {
							int x = parser.getAttributeIntValue(null, "x", 200);
							int y = parser.getAttributeIntValue(null, "y", 200);
							goals.add(new Goal(x, y));
							//goalLocations = new Point(parser.getAttributeIntValue(null, "x", 200), parser.getAttributeIntValue(null, "y", 200));
							Log.v(TAG, ("GoalLocation (" + x + "," + y + ") parsed"));
						}
						else if (parser.getName().equals("collectible")) {
							Paint paint = new Paint();
							paint.setARGB(255, 0, 255, 0);
							collectibles.add(new Collectible(
									(parser.getAttributeFloatValue(null, "x", 0.0f)),
									(parser.getAttributeFloatValue(null, "y", 0.0f))));
							Log.v(TAG, ("Collectible parsed, " + collectibles.size() + " added so far"));
						}
						else if (parser.getName().equals("wall")) {
							
							String cl = (String)parser.getAttributeValue(null, "colour");
							String textureName = parser.getAttributeValue(null, "texture");
							Paint paint = new Paint();
							if (cl != null) {
								paint.setARGB(255, Integer.parseInt(cl.substring(0,3)), Integer.parseInt(cl.substring(3,6)), Integer.parseInt(cl.substring(6,9)));
							}
							
							if (cl == null) {
								walls.add(new Wall(
										(parser.getAttributeFloatValue(null, "x1", 0.0f)),
										(parser.getAttributeFloatValue(null, "y1", 0.0f)),
										(parser.getAttributeFloatValue(null, "x2", 0.0f)),
										(parser.getAttributeFloatValue(null, "y2", 0.0f)),
										textureName,
										colours.get("wall"),
										null));
							} else {
								walls.add(new Wall(
										(parser.getAttributeFloatValue(null, "x1", 0.0f)),
										(parser.getAttributeFloatValue(null, "y1", 0.0f)),
										(parser.getAttributeFloatValue(null, "x2", 0.0f)),
										(parser.getAttributeFloatValue(null, "y2", 0.0f)),
										textureName,
										colours.get("wall"),
										paint));
							}

							if (!textureNames.contains(textureName)) textureNames.add(textureName);
							
							//TODO: Remove Paint dependency, use level.colours['wall'] instead.
							//Log.d(TAG, ("X1: " + parser.getAttributeFloatValue(null, "x1", 0.0f) + ", X2: " + parser.getAttributeFloatValue(null, "x2", 0.0f) + ", Y1: " + parser.getAttributeFloatValue(null, "y1", 0.0f) + ", Y2: " + parser.getAttributeFloatValue(null, "y2", 0.0f)));
							Log.v(TAG, ("Wall parsed, " + walls.size() + " added so far"));
						} else if (parser.getName().equals("spike")) {
							spikes.add(new Spike(
									parser.getAttributeFloatValue(null, "x1", 0.0f),
									parser.getAttributeFloatValue(null, "y1", 0.0f),
									parser.getAttributeFloatValue(null, "x2", 0.0f),
									parser.getAttributeFloatValue(null, "y2", 0.0f),
									parser.getAttributeIntValue(null, "rot", 0)));
							Log.v(TAG, ("Spike parsed, " + spikes.size() + " added so far"));
							//Log.v(TAG,  ("Spike added from (" + parser.getAttributeFloatValue(null, "x", 0.0f) + "," + parser.getAttributeFloatValue(null, "y", 0.0f) + "), " + spikes.size() + " added so far" ));
						} else if (parser.getName().equals("barrier")) {
							barriers.add(new Barrier(
									(parser.getAttributeFloatValue(null, "x1", 0.0f)),
									(parser.getAttributeFloatValue(null, "y1", 0.0f)),
									(parser.getAttributeFloatValue(null, "x2", 0.0f)),
									(parser.getAttributeFloatValue(null, "y2", 0.0f)),
									(parser.getAttributeIntValue(null, "rot", 0))));
							Log.v(TAG, ("Barrier parsed, " + barriers.size() + " added so far"));
						} else if (parser.getName().equals("flipper")) {
							flippers.add(new Flipper(
									(parser.getAttributeFloatValue(null, "x1", 0.0f)),
									(parser.getAttributeFloatValue(null, "y1", 0.0f)),
									(parser.getAttributeFloatValue(null, "x2", 0.0f)),
									(parser.getAttributeFloatValue(null, "y2", 0.0f)),
									(parser.getAttributeIntValue(null, "rot", 0))));
							Log.v(TAG, ("flipper parsed, " + flippers.size() + " added so far"));
						} else if (parser.getName().equals("blocker")) {
							blockers.add(new FlipBlocker(
									parser.getAttributeFloatValue(null, "x1", 0.0f),
									parser.getAttributeFloatValue(null, "y1", 0.0f),
									parser.getAttributeFloatValue(null, "x2", 0.0f),
									parser.getAttributeFloatValue(null, "y2", 0.0f)));
							Log.v(TAG, ("Blocker parsed, " + blockers.size() + " added so far"));
						}
						else
						{
							//Debug field for the moment, simply counteracts debug counter increment at end
							debug_itemcounter--;  
						}
						{						
												
						Log.e(TAG, ("Tag '" + parser.getName() + "' read"));
						
						
						/*  // NEED SOME SORT OF INTACT ELEMENT VERIFICATION HERE, MAY HAVE TO PLACE IT INSIDE EACH CASE AND ADAPT
						if (levelEntry.indexOf(null) != -1)  //getAttributeValue returns null on failed data get
						{	
							Log.e(TAG, "NULL found in levelEntry, discarding level #" + Integer.toString(levelcounter) + "' (index i-1)");
						}
						else
						{
							levelInfo.add(levelEntry);  //Seemingly successful read, add completed entry to list
							Log.e(TAG, "Successful read at index " + Integer.toString(levelcounter) + ", " + Integer.toString(levelInfo.size()) + " items so far");
						}
						*/
						
					}
						
						debug_itemcounter++;
				} //close start_tag event
				
				
				parser.next();
			}
			
			parser.close();
			
			Log.v(TAG, ("Parse complete: " + debug_itemcounter + " items parsed"));
			
		} catch (XmlPullParserException parserError) {
			Log.e(TAG, ".getEventType or .next failed, 'probably bad file format' according to tutorial");
			parserError.toString();
		} catch (IOException IOerror) {
			Log.e(TAG, "Unable to read resource file");
			IOerror.printStackTrace();
		}
		
		return null;

	}
}
