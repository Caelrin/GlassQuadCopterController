package caelrin.GlassGesturesInMotion;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import caelrin.GlassGesturesInMotion.sensor.Orientation;
import caelrin.GlassGesturesInMotion.sensor.OrientationListener;
import caelrin.GlassGesturesInMotion.sensor.SensorListener;

/**
 * Created by Caelrin on 1/26/14.
 */
public class GesturesView extends FrameLayout {
    private static final long DELAY_MILLIS = 41;
    private final TextView keyPressView;
    private final TextView orientationView;
    private final Handler mHandler = new Handler();
    private SensorListener sensorListener;
    private String displayText = "Touch the touchpad to begin!";
    private Orientation lastUpdatedOrientation = new Orientation();


    public void setListener(GesturesListener mListener) {
        this.mListener = mListener;
    }

    private GesturesListener mListener;

    private Runnable mUpdateViewRunnable= new Runnable() {
        @Override
        public void run() {
            keyPressView.setText("Gesture Made: " + displayText);
            orientationView.setText(lastUpdatedOrientation.toString());
            mListener.onTick(500);
            mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
        }
    };

    private OrientationListener orientationListener = new OrientationListener() {
        @Override
        public void orientationChanged(Orientation newOrientation) {
            lastUpdatedOrientation = newOrientation;
        }
    };

    private boolean mStarted = false;

    public GesturesView(Context context, SensorListener sensorListener) {
        this(context, null, 0);
        this.sensorListener = sensorListener;
        sensorListener.addListener(orientationListener);
    }

    public GesturesView(Context context) {
        this(context, null, 0);
    }

    public GesturesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GesturesView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        Log.e("Huh?", "Constructing");
        LayoutInflater.from(context).inflate(R.layout.text_view, this);

        keyPressView =  (TextView) findViewById(R.id.keypress);
        orientationView =  (TextView) findViewById(R.id.orientation);

        mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
    }

    public void start() {
        Log.e("Huh?", "I Started?");
        if (!mStarted) {
            mStarted = true;
            mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
        }
    }

    public void setDisplayText(String text) {
        this.displayText = text;
    }

    public interface GesturesListener {
        /**
         * Notified of a tick, indicating a layout change.
         */
        public void onTick(long millisUntilFinish);

        /**
         * Notified when the countdown is finished.
         */
        public void onFinish();
    }
}
