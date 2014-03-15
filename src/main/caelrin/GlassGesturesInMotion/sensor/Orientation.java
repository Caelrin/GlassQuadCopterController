package caelrin.GlassGesturesInMotion.sensor;

/**
 * Created by Caelrin on 3/15/14.
 */
public class Orientation {
    public Vector3 pitch;
    public Vector3 yaw;
    public Vector3 roll;

    public Orientation(){
        pitch = new Vector3();
        yaw = new Vector3();
        roll = new Vector3();
    }

    public Orientation(Vector3 pitch, Vector3 yaw, Vector3 roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public String toString() {
        return  "Pitch " + pitch.toString() + "\n" +
                "Yaw " + yaw.toString() + "\n" +
                "Roll " + roll.toString() + "\n";
    }
}
