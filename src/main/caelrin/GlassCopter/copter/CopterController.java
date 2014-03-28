package caelrin.GlassCopter.copter;

import android.os.AsyncTask;
import android.util.Log;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.LEDAnimation;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by Caelrin on 3/15/14.
 */
public class CopterController {
    IARDrone drone = null;
    private String TAG = "Copter Controller";
    private static final long CONNECTION_TIMEOUT = 10000;

    public CopterController() {
        drone = new ARDrone();
    }

    public void start() {
        new StartCommand().execute();
    }

    public void land() {
        new LandCommand().execute();
    }

    private class LandCommand extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
//            try {
//            } catch (IOException e) {
//                Log.e(TAG, "Failed to land", e);
//            }
            return null;
        }
    }

    private class StartCommand  extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            drone.start();
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
            drone.getCommandManager().takeOff();
            drone.getCommandManager().waitFor(5000);
            drone.getCommandManager().landing();
            return null;
        }
    }
}