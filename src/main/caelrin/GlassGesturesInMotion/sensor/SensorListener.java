package caelrin.GlassGesturesInMotion.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caelrin on 3/13/14.
 */
public class SensorListener {
    private SensorManager sensorManager;

    SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] mRotationMatrix = new float[16];
            float[] mOrientation = new float[9];
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, event.values);
            SensorManager.remapCoordinateSystem(mRotationMatrix, SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, mRotationMatrix);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);

            Orientation orientation = new Orientation(
                    new Vector3(mRotationMatrix[0], mRotationMatrix[1], mRotationMatrix[2]),
                    new Vector3(mRotationMatrix[4], mRotationMatrix[5], mRotationMatrix[6]),
                    new Vector3(mRotationMatrix[8], mRotationMatrix[9], mRotationMatrix[10])
            );

            for(OrientationListener listener : listeners) {
                listener.orientationChanged(orientation);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    private boolean alreadyInitialized = false;
    private List<OrientationListener> listeners;

    public SensorListener(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        listeners = new ArrayList<OrientationListener>();
    }

    public void start() {
        if(!alreadyInitialized) {
            sensorManager.registerListener(mSensorListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                    SensorManager.SENSOR_DELAY_UI);
            alreadyInitialized = true;
            
        }
    }

    public void addListener(OrientationListener listener) {
        listeners.add(listener);
    }
}
