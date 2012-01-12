package se.liu.tdp021.gravityGame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class RotationSensor {
	protected static String TAG = "RotationSensor";
	private GameplayActivity gameplayActivity = null;
	private SensorManager sensorManager = null;
	
	public RotationSensor(GameplayActivity activity) {
		gameplayActivity = activity;
	}
	
	public RotationSensor(Context context) {
		//gameplayView = view;
		Log.e(TAG, "Wrong constructor called!");
	}
	
	public void startSensor(Context context) {
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(listener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
	}
	
	
	public void stopSensor() {
		sensorManager.unregisterListener(listener);
	}
	
	
	private SensorEventListener listener = new SensorEventListener() {
	    public void onSensorChanged(SensorEvent e) {
	    	// Call back to GameplayActivity // TODO: Proper callbacks
	    	gameplayActivity.updateSensorValues(e.values[1], e.values[2]);
	    }
	    
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	};
}