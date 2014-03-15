package caelrin.GlassGesturesInMotion.sensor;

import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import caelrin.GlassGesturesInMotion.sensor.OrientationListener;
import caelrin.GlassGesturesInMotion.sensor.SensorListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSensorEvent;
import org.robolectric.shadows.ShadowSensorManager;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by Caelrin on 3/13/14.
 */
@Config(shadows = {ShadowSensorEvent.class})
@RunWith(RobolectricTestRunner.class)
public class SensorListenerTest {
    @InjectMocks
    private SensorListener underTest;

    @Mock
    private SensorManager sensorManager;
    @Mock
    private OrientationListener listener;
    @Mock
    private SensorEvent event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startShouldConnectToSensorManager() {

        underTest.start();

        verify(sensorManager).registerListener(any(SensorEventListener.class), any(Sensor.class), anyInt());
    }

    @Test
    public void startShouldNotRunIfItHasAlreadyRun() {
        underTest.start();
        underTest.start();

        verify(sensorManager, times(1)).registerListener(any(SensorEventListener.class), any(Sensor.class), anyInt());
    }


    //TODO: Figure out sensor testing
    @Test
    public void orientationChangedShouldMakeCallToListener() {
        underTest.start();
        underTest.addListener(listener);
        float[] rotationVector = new float[]{1.0f, 1.0f, 1.0f};
//        SensorManager sManager = Robolectric.newInstanceOf(SensorManager.class);
//        ShadowSensorManager shadowManager = Robolectric.shadowOf(sManager);
//        SensorEvent sEvent = shadowManager.createSensorEvent();

//        underTest.mSensorListener.onSensorChanged(event);
//
//        ArgumentCaptor<Orientation> ac = ArgumentCaptor.forClass(Orientation.class);
//        verify(listener).orientationChanged(ac.capture());
//        assertThat(ac.getValue().pitch, is(new Vector3(1f, 1f, 1f)));
    }




}
