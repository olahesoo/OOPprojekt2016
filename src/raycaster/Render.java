package raycaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Render {
	static Vector3[] directions;
	static Vector3 cameraPos = new Vector3(0, 0, 0);
	static int[] buffer;
	static int width;
	static int height;
	
	public static void initRender(double xfov, int width, int height) {
		Render.width = width;
		Render.height = height;
		buffer = new int[width * height];
		
		xfov = xfov / 180 * Math.PI;
		double yfov = 2 * Math.atan(Math.tan(xfov / 2) * height / width);
		directions = new Vector3[width * height]; //Pre-computed ray directions.
		//What if you turn around though?
		for (int y = 0; y < height; y++) {
			double yAngle = y * yfov / height - yfov / 2;
			for (int x = 0; x < width; x++) {
				double xAngle = x * xfov / width - xfov / 2;
				int index = y * width + x;
				directions[index] = new Vector3(Math.tan(xAngle), Math.tan(yAngle), 1);
				directions[index].normalize();
			}
		}
	}
	
	public static int[] render(ArrayList<Shape> shapes) {

		IntStream.range(0, buffer.length)
				.parallel()
				.forEach(index -> {
					Ray ray = new Ray(cameraPos, directions[index]);

					double distance = CollisionCheck.noCollision;
					double closestDistance = Double.MAX_VALUE;
					Color closestColor = new Color(0, 0, 0);

					for (Shape shape : shapes) {

						if (shape instanceof FlatShape) {
							FlatShape fShape = (FlatShape) shape;

							Triangle[] triangles = fShape.getTriangles();
							if (triangles != null) {
								for (Triangle triangle : triangles) {
									distance = CollisionCheck.rayTriangle(ray, triangle);
									if (distance < closestDistance && distance != CollisionCheck.noCollision) {
										closestDistance = distance;
										closestColor = shape.color;
									}
								}
							}

							Parallelogram[] quads = fShape.getQuads();
							if (quads != null) {
								for (Parallelogram parallelogram : quads) {
									distance = CollisionCheck.rayParallelogram(ray, parallelogram);
									if (distance < closestDistance && distance != CollisionCheck.noCollision) {
										closestDistance = distance;
										closestColor = shape.color;
									}
								}
							}
						} else if (shape instanceof Sphere) {
							distance = CollisionCheck.raySphere(ray, (Sphere) shape);
							if (distance < closestDistance && distance != CollisionCheck.noCollision) {
								closestDistance = distance;
								closestColor = shape.color;
							}
						}
					}

					int shade = closestColor.shade(closestDistance);
					buffer[index] = shade;
				});

		return buffer;
	}

}
