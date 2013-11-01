/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew;

/**
 * 
 * @author michael
 */
public class Util {
	public static float DoFrictionX(float xv, float yv, float FRICTION) {
		float angle = (float) Math.atan(yv / xv);
		if (xv == 0) {
			if (yv == 0) {
				angle = 0;
			}
			angle = (float) Math.PI / 2;
		}

		// apply friction
		float fx = Math.abs(FRICTION * (float) Math.cos(angle));
		if (xv > 0) {
			xv -= fx;
			if (xv < 0) {
				xv = 0;
			}
		} else if (xv < 0) {
			xv += fx;
			if (xv > 0) {
				xv = 0;
			}
		} else {
			xv = 0;
		}

		return xv;
	}

	public static float DoFrictionY(float xv, float yv, float FRICTION) {
		float angle = (float) Math.atan(yv / xv);
		if (xv == 0) {
			if (yv == 0) {
				angle = 0;
			}
			angle = (float) Math.PI / 2;
		}

		// apply friction
		float fy = Math.abs(FRICTION * (float) Math.sin(angle));

		if (yv > 0) {
			yv -= fy;
			if (yv < 0) {
				yv = 0;
			}
		} else if (yv < 0) {
			yv += fy;
			if (yv > 0) {
				yv = 0;
			}
		} else {
			yv = 0;
		}
		return yv;
	}

}
