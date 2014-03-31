package caelrin.GlassCopter;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import caelrin.GlassCopter.sensor.Orientation;
import caelrin.GlassCopter.sensor.OrientationListener;
import caelrin.GlassCopter.sensor.SensorListener;

/**
 * Created by Caelrin on 1/26/14.
 */
public class StartupView extends FrameLayout {
    private static final long DELAY_MILLIS = 41;
    private final Handler mHandler = new Handler();
    private SensorListener sensorListener;

    public void setListener(GesturesListener mListener) {
        this.mListener = mListener;
    }

    private GesturesListener mListener;

    private Runnable mUpdateViewRunnable= new Runnable() {
        @Override
        public void run() {
            mListener.onTick(500);
            mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
        }
    };

    private boolean mStarted = false;

    public StartupView(Context context) {
        this(context, null, 0);
    }

    public StartupView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        LayoutInflater.from(context).inflate(R.layout.text_view, this);
    }

    public void start() {
        if (!mStarted) {
            mStarted = true;
            mHandler.postDelayed(mUpdateViewRunnable, DELAY_MILLIS);
        }
    }

    public interface GesturesListener {
        public void onTick(long millisUntilFinish);
        public void onFinish();
    }
}
