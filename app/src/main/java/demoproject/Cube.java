package demoproject;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Cube {
    private final Vector3f position; // Position of the cube in world space, randomly set in the app class
    private final boolean shouldRotate; // Whether the cube should rotate or not, randomly set in the app class

    public Cube() { // Default Constructor
        this.position = new Vector3f(0.0f, 0.0f, 0.0f); // If nothing is passed in, the cube is at the origin and does not rotate
        this.shouldRotate = false;
    }

    public Cube(Vector3f position, boolean shouldRotate) { // Overloaded Constructor
        this.position = position; // Passed in parameters
        this.shouldRotate = shouldRotate;
    }

    public Matrix4f getModelMatrix(float time, int index) {
        // 4x4 identity matrix then translated to the position of the cube, rotated (if shouldRotate is true) around the y-axis
        return new Matrix4f().identity().translate(position) // Matrices are done using the builder pattern
            .rotate((float) Math.toRadians(20.0f * (index + 1) * (shouldRotate ? 1 : 0)) * time, new Vector3f(0.5f, 1.0f, 0.0f).normalize());
    }

    // Vertices and indices for the cube are shared among all instances, no data duplication
    // Also, these cubes are not in the right winding order, but the cubes are currently hardcoded so its a lot off effort to fix
    // I'll just render both sides of the faces
    public static final float[] vertices = {
        -0.5f,0.5f,-0.5f,
        -0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,0.5f,-0.5f,

        -0.5f,0.5f,0.5f,
        -0.5f,-0.5f,0.5f,
        0.5f,-0.5f,0.5f,
        0.5f,0.5f,0.5f,

        0.5f,0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,0.5f,
        0.5f,0.5f,0.5f,

        -0.5f,0.5f,-0.5f,
        -0.5f,-0.5f,-0.5f,
        -0.5f,-0.5f,0.5f,
        -0.5f,0.5f,0.5f,

        -0.5f,0.5f,0.5f,
        -0.5f,0.5f,-0.5f,
        0.5f,0.5f,-0.5f,
        0.5f,0.5f,0.5f,

        -0.5f,-0.5f,0.5f,
        -0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,-0.5f,
        0.5f,-0.5f,0.5f
    };
    public static final int[] indices = {
        0,1,3,
        3,1,2,
        4,5,7,
        7,5,6,
        8,9,11,
        11,9,10,
        12,13,15,
        15,13,14,
        16,17,19,
        19,17,18,
        20,21,23,
        23,21,22
    };
}
