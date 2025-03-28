package demoproject;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId; // Program ID for the shader program, memory address of the program object
    
    public Shader() { // Default unchanged code
        this(vertexShaderCode, fragmentShaderCode); // Ooo cool constructor chaining
    }

    public Shader(String vertexShaderCode, String fragmentShaderCode) { // Overloaded Constructor
        int vertexShader = createShader(vertexShaderCode, GL_VERTEX_SHADER); // Create the vertex shader
        int fragmentShader = createShader(fragmentShaderCode, GL_FRAGMENT_SHADER); // Create the fragment shader

        programId = glCreateProgram(); // GPU bull
        glAttachShader(programId, vertexShader); // Attach the vertex and fragment shader to the program
        glAttachShader(programId, fragmentShader);
        glLinkProgram(programId); // Link the program, this is where the GPU does its magic

        glDeleteShader(vertexShader); // Delete the vertex and fragment shader, no longer needed
        glDeleteShader(fragmentShader); 
    }

    public void setUniformMatrix4fv(String name, float[] matrix) {
        // Not very performant structure, we should create a uniform location cache to avoid calling glGetUniformLocation every frame
        // But for now, this is fine since we only have 3 uniforms and the shader is not very complex
        glUniformMatrix4fv(glGetUniformLocation(programId, name), false, matrix);
    }

    public void cleanup() {
        glDeleteProgram(programId); // Delete the program at the end of the application
    }

    public void use() {
        glUseProgram(programId); // Uploads the program to the GPU
    }

    private int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType); // Create the shader object
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId); // Compile the shader
        return shaderId; // Return the shader ID, this is the memory address of the shader object
    }

    // Even though the shader code doesn't need to be static, I put it here to keep it out of the app class
    // Also since its here its like a hardcoded fallback
    public static final String vertexShaderCode = """
        #version 330 core
        layout (location = 0) in vec3 aPos;

        uniform mat4 model;
        uniform mat4 view;
        uniform mat4 projection;

        void main() {
            gl_Position = projection * view * model * vec4(aPos, 1.0);
        }
        """;

    public static final String fragmentShaderCode = """
        #version 330 core
        out vec4 FragColor;

        void main() {
            FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
        }
        """;
}
