package demoproject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL30.*;

public class App {
    private Window window; // Custom classes
    private Camera camera;
    private Shader shader;

    private int vao; // OpenGL objects
    private int vbo; // They're actually references stored as ints
    private int ebo; // Remember: under the hood they ARE arrays

    private final Random random = new Random(); // Random

    private final ArrayList<Cube> cubeList = new ArrayList<>(); // ArrayLists

    private void bindArrays() {
        vao = glGenVertexArrays(); // Set up those arrays
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        glBindVertexArray(vao); // Bind the array, OpenGL uses a global state machine

        glBindBuffer(GL_ARRAY_BUFFER, vbo); // Bind the vertices array to the vertex buffer object
        glBufferData(GL_ARRAY_BUFFER, Cube.vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo); // Bind the indices array buffer to the element buffer object
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Cube.indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0); // Set the vertex attribute pointer, this is the location in the shader
        // 0 is the location in the shader, 3 is the number of components (x,y,z), GL_FLOAT is the type, false is normalized, 0 is stride, 0 is offset
        glEnableVertexAttribArray(0);
    }

    private void loop() {
		// Perspective projection matrix, view matrix is computed in the window class
        Matrix4f proj = new Matrix4f().perspective((float) Math.toRadians(45.0f), window.getAspect(), Window.Z_NEAR, Window.Z_FAR);

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f); // Background color
        while (!window.shouldClose()) { // Custom function to check if the window is closed
            camera.handleMouseInput(window.handle()); // Custom mouse and keyboard input functions
            camera.handleKeyboardInput(window.handle());

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear the screen and depth buffer at the start of each frame

            shader.use(); // Use the shader program, this is a custom function in the shader class

            // Lazy way to pass data between classes, although technically this is "good" because
            if (camera.createCube) { // it can miss a frame and still create a cube next frame
                cubeList.add(new Cube(new Vector3f(10 * random.nextFloat(-0.5f, 0.5f), 10 * random.nextFloat(-0.5f, 0.5f), 10 * random.nextFloat(-0.5f, 0.5f)),
                    random.nextBoolean()));
                
                camera.createCube = false; // Reset the create cube flag
            }
            if (camera.deleteCube) {
                if (!cubeList.isEmpty()) { // Make sure we're not accessing an illegal index
                    cubeList.remove(cubeList.size() - 1); // Delete the latest cube
                }
                camera.deleteCube = false; // Reset the delete cube flag
            }

            glBindVertexArray(vao); // Bind the vertex array object, it's in here to allow dynamic cube creation
            
            shader.setUniformMatrix4fv("view", camera.getViewMatrix().get(new float[16])); // Send the view matrix to the shader
            shader.setUniformMatrix4fv("projection", proj.get(new float[16])); // Send the projection matrix to the shader

            for (int i = 0; i < cubeList.size(); i++) {
                Matrix4f model = cubeList.get(i).getModelMatrix((float) glfwGetTime(), i); // Get the model matrix for each cube
                shader.setUniformMatrix4fv("model", model.get(new float[16])); // Send the model matrix to the shader
                
                // Draw the cube, 36 is the number of indices, GL_UNSIGNED_INT is the type, 0 is the offset
                glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
            }

            glBindVertexArray(0); // Unbind the vertex array object

            // Custom function to update the window, swap buffers (double buffering for smoothness) and poll events (keyboard and mouse input)
            window.update();
        }
        glDeleteVertexArrays(vao); // Delete the vertex array object and buffer object at the end of the program
        glDeleteBuffers(vbo);
    }

    public void run(int width, int height, String title) {
		// Constructors
        window = new Window(width, height, title); // Overloaded constructor
		camera = new Camera(); // Default constructor
        window.init(); // Class function

        cubeList.add(new Cube()); // Add a cube at the origin

        int i = 0; // Vikrant asked for a while loop
        while (i < 9) { // Add 9 more cubes at random positions
            cubeList.add(new Cube(new Vector3f(10 * random.nextFloat(-0.5f, 0.5f), 10 * random.nextFloat(-0.5f, 0.5f), 10 * random.nextFloat(-0.5f, 0.5f)),
                true));
            i++;
        }
        //if (int i = 0; i < 9; i++) { // Add 9 more cubes at random positions
        //    cubeList.add(new Cube(new Vector3f(10 * random.nextFloat(-0.5f, 0.5f), 10 * random.nextFloat(-0.5f, 0.5f), 10 * random.nextFloat(-0.5f, 0.5f)),
        //        true));
        //}

        bindArrays(); // Bind the vertex array object and buffer object

		shader = new Shader(); // Default constructor, this is the shader class

        loop(); // Main loop, this is where the rendering happens, will run until the window is closed

        shader.cleanup(); // No destructors in java :( Cons of a garbage collector
        window.cleanup();
    }

    public static void main(String[] args) { // Entry point of the program, the main function
		// User Input
		System.out.print("Enter a title for the window: ");
		String title = "I set this up with a constuctor!"; // Default title
		Scanner scanner;
		try { // Try catch blocks
			scanner = new Scanner(System.in); // The scanner actually gives a warning for not putting this in a try catch block
			title = scanner.nextLine(); // Get custom title from the user
			scanner.close(); // Close the scanner, to avoid memory leaks
		}
		catch (Exception e) { // Catch any exceptions that occur
			System.out.println("Invalid input, using default title.");
		}

		new App().run(1600, 900, title); // Constructor
    }
}