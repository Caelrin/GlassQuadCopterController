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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.*;
import caelrin.GlassCopter.copter.CopterController;
import caelrin.GlassCopter.sensor.SensorListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = createGestureDetector(this);
        copterController = new CopterController();
        if(!isBound) {
            view = new GesturesView(getBaseContext());
            setContentView(view);
            copterController.start();
//            bindService(new Intent(this, GesturesInMotionService.class), mConnection, 0);
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
            unbindService(mConnection);
            stopService(new Intent(this, GesturesInMotionService.class));
            isBound = false;
        }

    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }
}
