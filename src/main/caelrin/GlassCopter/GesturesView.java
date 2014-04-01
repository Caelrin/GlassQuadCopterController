package caelrin.GlassCopter;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import caelrin.GlassCopter.copter.CopterController;
import caelrin.GlassCopter.sensor.Orientation;
import caelrin.GlassCopter.sensor.OrientationListener;
import caelrin.GlassCopter.sensor.SensorListener;

/**
 * Created by Caelrin on 1/26/14.
 */
public class GesturesView extends FrameLayout {
    private static final long DELAY_MILLIS = 41;
    private final TextView keyPressView;
    private final TextView orientationView;
    private final Handler mHandler = new Handler();
    private String displayText = "Touch the touchpad to begin!";
    private Orientation lastUpdatedOrientation = new Orientation();


    private Runnable mUpdateViewRunnable= new Runnable() {
        @Override
        public void run() {
            keyPressView.setText("Gesture Made: " + displayText);
            orientationView.setText(lastUpdatedOrientation.toString());
            mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
        }
    };

    private OrientationListener orientationListener = new OrientationListener() {
        @Override
        public void orientationChanged(Orientation newOrientation) {
            lastUpdatedOrientation = newOrientation;
            orientationView.setText(lastUpdatedOrientation.toString());
        }
    };

    private boolean mStarted = false;

    public GesturesView(Context context) {
        this(context, null, 0);
    }

    public GesturesView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        Log.e("Huh?", "Constructing");
        LayoutInflater.from(context).inflate(R.layout.copter_view, this);

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

    public OrientationListener getOrientationListener() {
        return orientationListener;
    }
}
