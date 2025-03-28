package demoproject;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height; // Will be set by the constructor
    private final String title;

    private long window; // Window handle, funny port from C API
    
    public static final float FOV = (float)Math.toRadians(70); // Set values for the projection matrix
    public static final float Z_NEAR = 0.01f; // Don't need to be static, but hardcoded (final)
    public static final float Z_FAR = 1000f;

    public Window() { // Default constructor, sets the title and size of the window to default values
        this.title = "Default Title";
        this.width = 1600;
        this.height = 900;
    }

    public Window(int width, int height, String title) { // Overloaded constructor
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public long handle() { // For usage of keyboard and mouse input in the camera class
        return window;
    }

    public float getAspect() { // For the projection matrix
        return (float)width / height;
    }

    public boolean shouldClose() { // For the main loop
        return glfwWindowShouldClose(window);
    }

    public void cleanup() {
        glfwFreeCallbacks(window); // Free the callbacks set in the init function for resizing and input
        glfwDestroyWindow(window);
        glfwTerminate(); // Terminate GLFW, removes all the resources allocated by GLFW
    }

    public void update() {
        glfwSwapBuffers(window); // Double buffering for smoothness
        glfwPollEvents(); // Poll for window events (keyboard and mouse input)
    }
    
    private boolean wireframe = false; // Wireframe mode, toggled with the tab key
    public void init() {
        if (!glfwInit()) // Initialize GLFW, returns false if it fails
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE); // OpenGL works in a globals context
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Set window to be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // Set the OpenGL version to 3.3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // Set the OpenGL profile to core, no compatibility mode

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Lambda functions for callbacks
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) // Hardcoded keys for ease of creation
                glfwSetWindowShouldClose(window, true); // Close the window when escape is pressed
    
            if (key == GLFW_KEY_TAB && action == GLFW_PRESS) { // Toggle wireframe mode when tab is pressed
                wireframe = !wireframe;
            }
            if (wireframe) {
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE); // Set the polygon mode to wireframe
            }
            else {
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); // Else, set it to fill
            }
        });

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> { // Resize the window when the framebuffer size changes
            this.width = width;
            this.height = height;
            glViewport(0, 0, width, height); // Set the viewport to the new size
        });

        glfwMakeContextCurrent(window); // Set the current context, single-threaded
        glfwSwapInterval(1); // Vsync
        glfwShowWindow(window); // Show the window after everything is set up

        createCapabilities(); // Load OpenGL functions

        glEnable(GL_DEPTH_TEST); // Depth testing for btf rendering
        glEnable(GL_STENCIL_TEST); // Unused at the moment
    }
}