package raycaster;

public class Triangle implements Side{
	Vector3 a, b, c;
	Vector3[] vertices = {a, b, c};
	Vector3 ab, ac;
	Plane plane;
	double xx, xy, yy, d;
	
	Triangle(Vector3 a, Vector3 b, Vector3 c) {
		this.a = a;
		this.b = b;
		this.c = c;
		Update();
	}
	
	void Update() {
		ab = Vector3.sub(b, a);
		ac = Vector3.sub(c, a);
		plane = new Plane(a, ab, ac);
		xx = Vector3.dot(ab, ab);
		xy = Vector3.dot(ab, ac);
		yy = Vector3.dot(ac, ac);
		d = 1 / (yy * xx - xy * xy);
	}
	
	@Override
	public String toString() {
		return a + ", " + b + ", " + c;
	}
	
}