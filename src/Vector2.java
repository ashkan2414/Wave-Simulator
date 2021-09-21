
public class Vector2 {
	public float x, y;

	public Vector2() {
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 vector) {
		x = vector.x;
		y = vector.y;
	}

	public static float GetDistance(Vector2 p1, Vector2 p2) {
		return (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
	}

	public static Vector2 Subtract(Vector2 start, Vector2 end) {
		Vector2 newVector = new Vector2();
		newVector.x = end.x - start.x;
		newVector.y = end.y - start.y;
		return newVector;
	}

	public static Vector2 Add(Vector2 v1, Vector2 v2) {
		Vector2 newVector = new Vector2();
		newVector.x = v1.x + v2.x;
		newVector.y = v1.y + v2.y;
		return newVector;
	}

	public void normalize() {
		float magnitude = getMagnitude();
		x /= magnitude;
		y /= magnitude;
	}

	public float getMagnitude() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
