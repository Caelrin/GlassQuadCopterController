package caelrin.GlassCopter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import caelrin.GlassCopter.sensor.SensorListener;

/**
 * Created by Caelrin on 1/25/14.
 */
public class GestureHolder implements SurfaceHolder.Callback {

    private static final String TAG = "Gesture Holder";
    private final StartupView gesturesView;
    private SurfaceHolder mHolder;

    public GestureHolder(Context context, SensorListener sensorListener) {
        gesturesView = new StartupView(context);
        gesturesView.setListener(new StartupView.GesturesListener() {

            @Override
            public void onTick(long millisUntilFinish) {
                draw(gesturesView);
            }

            @Override
            public void onFinish() {
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        gesturesView.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
