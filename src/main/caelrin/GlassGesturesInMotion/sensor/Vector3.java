package caelrin.GlassGesturesInMotion.sensor;

import java.text.DecimalFormat;

/**
 * Created by Caelrin on 3/15/14.
 */
public class Vector3 {
    public Float x, y, z;

    public Vector3(){
        x = y = z = 0f;
    }

    public Vector3(Float x,Float y,Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(x) + " " + df.format(y) + " " + df.format(z);
    }
}
