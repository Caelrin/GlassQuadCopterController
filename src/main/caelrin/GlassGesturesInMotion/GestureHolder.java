package caelrin.GlassGesturesInMotion;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import caelrin.GlassGesturesInMotion.sensor.SensorListener;

/**
 * Created by Caelrin on 1/25/14.
 */
public class GestureHolder implements SurfaceHolder.Callback {

    private static final String TAG = "Gesture Holder";
    private final GesturesView gesturesView;
    private final SensorListener sensorListener;
    private SurfaceHolder mHolder;

    public GestureHolder(Context context, SensorListener sensorListener) {
        this.sensorListener = sensorListener;
        Log.e(TAG, "Construct mebbe? Take 3");
        gesturesView = new GesturesView(context, sensorListener);
        gesturesView.setListener(new GesturesView.GesturesListener() {

            @Override
            public void onTick(long millisUntilFinish) {
                draw(gesturesView);
            }

            @Override
            public void onFinish() {
            }
        });

    }

    public void setDisplayText(String text) {
        gesturesView.setDisplayText(text);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "Surface created");
        mHolder = holder;
        gesturesView.start();
        sensorListener.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("Huh?", "Surf changed?");
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        gesturesView.measure(measuredWidth, measuredHeight);
        gesturesView.layout(
                0, 0, gesturesView.getMeasuredWidth(), gesturesView.getMeasuredHeight());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void draw(View view) {
        Canvas canvas;
        try {
            canvas = mHolder.lockCanvas();
        } catch (Exception e) {
            return;
        }
        if (canvas != null) {
            view.draw(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
}
