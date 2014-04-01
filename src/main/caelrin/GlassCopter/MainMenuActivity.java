/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package caelrin.GlassCopter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import caelrin.GlassCopter.copter.CopterController;
import caelrin.GlassCopter.sensor.*;
import caelrin.GlassCopter.sensor.OrientationListener;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

/**
 * Activity showing the options menu.
 */
public class MainMenuActivity extends Activity {
    private GestureDetector mGestureDetector;
    private CopterController copterController;
    private GesturesView view ;
    private boolean isBound = false;
    private SensorListener sensorListener;
    private Orientation baseOrientation;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof GesturesInMotionService.GesturesBinder) {
//                gesturesBinder = (GesturesInMotionService.GesturesBinder) service;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Do nothing.
        }
    };

    private OrientationListener orientationListener = new OrientationListener() {
        @Override
        public void orientationChanged(Orientation newOrientation) {
            if(baseOrientation == null) {
                baseOrientation = newOrientation;
            }
            if(newOrientation.pitch.x - baseOrientation.pitch.x > .02f) {
                copterController.turnLeft();
                Log.e("Activity", "Turn left a bit");
            }
            if(newOrientation.pitch.x - baseOrientation.pitch.x < -.02f) {
                copterController.turnRight();
                Log.e("Activity", "Turn right a bit");
            }
            Vector3 roll = newOrientation.roll;
            if(roll.x > .05) {
                copterController.goLeft(calculateMoveSpeed(roll.x));
            } else if(roll.x < -.05){
                copterController.goRight(calculateMoveSpeed(roll.x));
            }
            if(roll.y > .05) {
                copterController.goBackward(calculateMoveSpeed(roll.x));
            } else if(roll.y < -.05){
                copterController.goForward(calculateMoveSpeed(roll.x));
            }
            if(roll.x < .05 && roll.x > -.05 && roll.y < .05 && roll.y > -.05){
                copterController.hover();
            }
            baseOrientation = newOrientation;
        }

        private Integer calculateMoveSpeed(float f) {
            return Math.abs((int) (f * 50f));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        copterController = new CopterController();
        if(!isBound) {
            SensorManager sensorManager =
                    (SensorManager) getSystemService(Context.SENSOR_SERVICE);

            sensorListener = new SensorListener(sensorManager);
            sensorListener.addListener(orientationListener);
            view = new GesturesView(getBaseContext());
            sensorListener.addListener(view.getOrientationListener());
            sensorListener.start();
            setContentView(view);
            copterController.start();
            isBound = true;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hello_menu, menu);
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }


    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(gesture == Gesture.THREE_LONG_PRESS) {
                    openOptionsMenu();
                    return false;
                } else if(gesture == Gesture.TWO_LONG_PRESS){
                    copterController.land();
                }
//                gesturesBinder.setDisplayText(gesture.name());
                return true;
            }
        });
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
                // do something on finger count changes
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                return true;
            }
        });
        return gestureDetector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop:
                stop();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void stop() {
        if(isBound) {
            Log.e("STOP", "Stopping");
            stopService(new Intent(this, GesturesInMotionService.class));
            sensorListener.stop();
            isBound = false;
        }

    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }
}
