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
        drone = new ARDrone("192.168.1.1", null);
    }

    public void start() {
        new StartCommand().execute();
    }

    public void land() {
        new LandCommand().execute();
    }

    public void turnLeft() {
        drone.getCommandManager().spinLeft(5);
    }

    public void turnRight() {
        drone.getCommandManager().spinRight(5);
    }

    public void goForward(Integer speed) {
        drone.getCommandManager().forward(speed);
    }
    public void goBackward(Integer speed) {
        drone.getCommandManager().backward(speed);
    }
    public void goLeft(Integer speed) {
        drone.getCommandManager().goLeft(speed);
    }
    public void goRight(Integer speed) {
        drone.getCommandManager().goRight(speed);
    }

    public void hover() {
        drone.getCommandManager().hover();
    }

    private class LandCommand extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            drone.getCommandManager().landing();
            return null;
        }
    }

    private class StartCommand  extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            drone.start();
            drone.getCommandManager().setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 3);
            drone.getCommandManager().takeOff();
            drone.getCommandManager().waitFor(1000);
            drone.getCommandManager().flatTrim();
            drone.getCommandManager().hover();

            return null;
        }
    }
}
