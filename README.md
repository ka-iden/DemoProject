# DemoProject

## Overview
My demo project is a rough Java-based 3D OpenGL application that demonstrates a dynamic rendering system. It features a moving camera and a programmable list of cubes that can be dynamically created, positioned, and optionally rotated. The project showcases the use of modern Java features, OpenGL, and basic object-oriented programming principles.

## How to Run
1. Ensure you have Java 17 or higher installed.
2. Clone the repository or download the project files.
3. Navigate to the project directory and build the project using Gradle:
  - Windows:
    ```sh
    ./gradlew.bat build
    ./gradlew.bat shadowJar
    ```
   - Mac/Linux:
     ```sh
     ./gradlew build
     ./gradlew shadowJar
     ```
4. Navigate to `./app/build/libs` ([here](./app/build/libs/)) and open a terminal and run `java -jar app-all.jar`

Prebuilt JARs are available in the on my github [here](https://github.com/ka-iden/DemoProject/releases/latest). You can run it the same way as step 4, but without having to navigate to the folder listed above.

## Controls
- WASD movement, alongside Q and E to move up and down.
- The amount of cubes can be changed with the **+** (plus, its the same key as the equals sign) and **-** (minus) keys to create or delete the latest cube.

## Features
- **3D Rendering**: Uses OpenGL to render a 3D scene with cubes.
- **Dynamic Cube Management**: Add or remove cubes dynamically during runtime using + and - keys.
- **Camera System**: Move the camera using keyboard and mouse inputs (WASD to move, QE to go up and down).
- **Wireframe Mode**: Toggle between wireframe and solid rendering modes (Tab key).

## Technologies Used
- **Java**: Core programming language.
  - **LWJGL**: Lightweight Java Game Library for OpenGL bindings.
  - **JOML**: Java OpenGL Math Library for matrix and vector operations.
- **Gradle**: Build tool for dependency management and project compilation.
  - JohnRengleman's **Gradle Shadow Plugin**: For creating a shadow JAR with all dependencies included.

## App Structure
### **Main Application (App.java)**
- **Variables**:
  - `window`, `camera`, `shader`: Instances of custom classes.
  - `vao`, `vbo`, `ebo`: OpenGL arrays to be passed onto the renderer.
  - `cubeList`: Variablly allocated list of cubes in the scene.
- **Functions**:
  - `run`: Initializes the application and starts the main loop.
  - `bindArrays`: Sets up OpenGL buffers for rendering.
  - `loop`: Main rendering loop that updates the scene and handles input.
  - `main`: Entry point of the application, handles user input for the window title.
### **Camera Class**
- **Variables**:
  - `radius`: Distance from the origin.
  - `azimuth` and `elevation`: Angles for spherical coordinates.
  - `position`: Current position of the camera.
  - `sensitivity` and `moveSpeed`: Control mouse and keyboard input responsiveness.
  - `createCube` and `deleteCube`: Flags for adding or removing cubes.
- **Functions**:
  - `handleMouseInput`: Processes mouse input for camera rotation.
  - `handleKeyboardInput`: Processes keyboard input for movement and cube management.
  - `getViewMatrix`: Generates the view matrix for rendering the scene.
### **Cube Class**
- **Variables**:
  - `position`: Position of the cube in the 3D space.
  - `shouldRotate`: Determines if the cube rotates.
  - `vertices` and `indices`: Shared static arrays for cube geometry.
- **Functions**:
  - `getModelMatrix`: Generates the model matrix for the cube, applying transformations like translation and rotation.
### **Shader Class**
- **Variables**:
  - `programId`: OpenGL program ID for the shader.
  - `vertexShaderCode` and `fragmentShaderCode`: Hardcoded shader source code.
- **Functions**:
  - `use`: Activates the shader program.
  - `setUniformMatrix4fv`: Sets uniform matrix variables in the shader.
  - `cleanup`: Deletes the shader program.
  - `createShader`: Compiles and links shader code.
### **Window Class**
- **Variables**:
  - `width`, `height`, `title`: Window dimensions and title.
  - `window`: GLFW window handle.
  - `wireframe`: Toggles wireframe rendering mode.
- **Functions**:
  - `init`: Initializes the GLFW window and OpenGL context.
  - `update`: Swaps buffers and polls events.
  - `cleanup`: Frees resources and destroys the window.
  - `getAspect`: Returns the aspect ratio for the projection matrix.