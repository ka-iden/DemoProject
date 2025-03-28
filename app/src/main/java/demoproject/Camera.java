package demoproject;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

public class Camera { // No constructor required
    private final float radius = 10.0f; // Distance from the origin
    private float azimuth = 0.0f; // Horizontal angle (in radians)
    private float elevation = (float) Math.toRadians(30.0f); // Vertical angle (in radians)

    private final float sensitivity = 0.005f; // Mouse sensitivity
    private final float moveSpeed = 0.1f;
    private final Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);

    private float lastMouseX = 0.0f; // For calculating mouse movement
    private float lastMouseY = 0.0f;
    private boolean firstMouse = true; // To prevent the camera from jumping on the first mouse movement

    public boolean createCube = false; // Create and delete cube flags
    public boolean deleteCube = false;
    private boolean wasCreateCubePressed = false; // To stop key rollover
    private boolean wasDeleteCubePressed = false;

    public void handleMouseInput(long windowHandle) {
        double[] mouseX = new double[1]; // Glfw function expects double arrays because in c it's meant to be a pointer to a double
        double[] mouseY = new double[1];
        glfwGetCursorPos(windowHandle, mouseX, mouseY);

        // Only move the camera if the mouse is pressed and the cursor is inside the window
        boolean isDragging = glfwGetMouseButton(windowHandle, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
        processMouseInput((float) mouseX[0], (float) mouseY[0], isDragging);
    }

    public void handleKeyboardInput(long windowHandle) {
        processKeyboardInput(windowHandle); // lol
    }

    public void processMouseInput(float mouseX, float mouseY, boolean isDragging) {
        if (firstMouse) { // Initialize last mouse position to the current mouse position
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            firstMouse = false; // Set to false so it doesn't run again
        }

        float deltaX = mouseX - lastMouseX; // Mouse movement since last frame
        float deltaY = lastMouseY - mouseY; // Reversed since y-coordinates go from bottom to top in opengl
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        if (isDragging) { // Only update the camera if the mouse is being dragged
            azimuth -= deltaX * sensitivity;
            elevation -= deltaY * sensitivity;

            // Clamp elevation to prevent flipping & gimbal lock
            elevation = (float)Math.clamp(elevation, Math.toRadians(-89.0f), Math.toRadians(89.0f));
        }
    }

    public void processKeyboardInput(long windowHandle) {
        Vector3f forwardVec = new Vector3f( // Bullshitery that I pulled from an old project at https://github.com/fl2mex/OpenGL-Test
            -(float) Math.sin(azimuth),
            0.0f,
            -(float) Math.cos(azimuth)).normalize(); // Calculate forward vector based on azimuth angle

        Vector3f rightVec = new Vector3f(
            (float) Math.sin(azimuth + Math.PI / 2),
            0.0f,
            (float) Math.cos(azimuth + Math.PI / 2)).normalize(); // Calculate right vector based on azimuth angle

        if (glfwGetKey(windowHandle, GLFW_KEY_W) == GLFW_PRESS) { // Forward movement, W key
            position.add(forwardVec.mul(moveSpeed, new Vector3f()));
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_S) == GLFW_PRESS) { // Backward movement, S key
            position.sub(forwardVec.mul(moveSpeed, new Vector3f()));
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_A) == GLFW_PRESS) { // Left movement, A key
            position.sub(rightVec.mul(moveSpeed, new Vector3f()));
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_D) == GLFW_PRESS) { // Right movement, D key
            position.add(rightVec.mul(moveSpeed, new Vector3f()));
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_Q) == GLFW_PRESS) { // Up movement, Q key
            position.y += moveSpeed;
        }
        if (glfwGetKey(windowHandle, GLFW_KEY_E) == GLFW_PRESS) { // Down movement, E key
            position.y -= moveSpeed;
        }
        boolean isCreateCubePressed = glfwGetKey(windowHandle, GLFW_KEY_EQUAL) == GLFW_PRESS; // Create cube flag
        if (isCreateCubePressed && !wasCreateCubePressed) { // If the plus key is pressed and was not pressed before
            createCube = true;
        }
        wasCreateCubePressed = isCreateCubePressed; // Reset the create cube flag
        boolean isDeleteCubePressed = glfwGetKey(windowHandle, GLFW_KEY_MINUS) == GLFW_PRESS; // Delete cube flag
        if (isDeleteCubePressed && !wasDeleteCubePressed) { // If the minus key is pressed and was not pressed before
            deleteCube = true;
        }
        wasDeleteCubePressed = isDeleteCubePressed; // Reset the delete cube flag
    }

    public Matrix4f getViewMatrix() {
        // Convert spherical coordinates to Cartesian coordinates, more stuff pulled from my old project at https://github.com/fl2mex/OpenGL-Test
        float x = (float) (radius * Math.cos(elevation) * Math.sin(azimuth)) + position.x;
        float y = (float) (radius * Math.sin(elevation)) + position.y;
        float z = (float) (radius * Math.cos(elevation) * Math.cos(azimuth)) + position.z;
        // Look at the central position of spherical camera from the origin
        return new Matrix4f().lookAt(new Vector3f(x, y, z), new Vector3f(position.x, position.y, position.z), new Vector3f(0.0f, 1.0f, 0.0f));
    }
}
