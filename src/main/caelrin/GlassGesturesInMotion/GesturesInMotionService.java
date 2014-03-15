package caelrin.GlassGesturesInMotion;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import caelrin.GlassGesturesInMotion.sensor.SensorListener;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

/**
 * Created by agibson on 1/11/14.
 */
public class GesturesInMotionService extends Service {

    private GesturesBinder binder = new GesturesBinder();

    private static final String TAG = "GesturesTag";
    private static final String LIVE_CARD_TAG = "gestures";

    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;
    private GestureHolder mCallback;
    private SensorListener sensorListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimelineManager = TimelineManager.from(this);

        SensorManager sensorManager =
                (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorListener = new SensorListener(sensorManager);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>> Starting UP!");
            mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_TAG);

            mCallback = new GestureHolder(this, sensorListener);
            mLiveCard.setDirectRenderingEnabled(true).getSurfaceHolder().addCallback(mCallback);


            Intent voiceIntent = new Intent(this, MainMenuActivity.class);
            voiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, voiceIntent, 0));
            mLiveCard.publish(LiveCard.PublishMode.REVEAL);
            Log.d(TAG, "Done publishing LiveCard");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            if (mCallback != null) {
                mLiveCard.getSurfaceHolder().removeCallback(mCallback);
            }
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }

    public class GesturesBinder extends Binder {
        public void setDisplayText(String text){
            mCallback.setDisplayText(text);
        }
    }
}
