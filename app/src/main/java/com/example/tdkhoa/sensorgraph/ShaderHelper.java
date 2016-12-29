package com.example.tdkhoa.sensorgraph;


import android.opengl.GLES20;
import android.util.Log;

public class ShaderHelper {
        private static final String TAG = "ShaderHelper";

        /**
         * Loads and compiles a vertex shader, returning the OpenGL object ID.
         */
        public static int compileVertexShader(String shaderCode) {
            return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
        }

        /**
         * Loads and compiles a fragment shader, returning the OpenGL object ID.
         */
        public static int compileFragmentShader(String shaderCode) {
            return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
        }

        /**
         * Compiles a shader, returning the OpenGL object ID.
         */
        private static int compileShader(int type, String shaderCode) {
            // Create a new shader object.
            final int shaderObjectId = GLES20.glCreateShader(type);

            if (shaderObjectId == 0) {
                return 0;
            }

            // Pass in the shader source.
            GLES20.glShaderSource(shaderObjectId, shaderCode);

            // Compile the shader.
            GLES20.glCompileShader(shaderObjectId);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS,
                    compileStatus, 0);

            // Verify the compile status.
            if (compileStatus[0] == 0) {
                // If it failed, delete the shader object.
                GLES20.glDeleteShader(shaderObjectId);

                return 0;
            }

            // Return the shader object ID.
            return shaderObjectId;
        }

        /**
         * Links a vertex shader and a fragment shader together into an OpenGL
         * program. Returns the OpenGL program object ID, or 0 if linking failed.
         */
        public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

            // Create a new program object.
            final int programObjectId = GLES20.glCreateProgram();

            if (programObjectId == 0) {
                return 0;
            }

            // Attach the vertex shader to the program.
            GLES20.glAttachShader(programObjectId, vertexShaderId);

            // Attach the fragment shader to the program.
            GLES20.glAttachShader(programObjectId, fragmentShaderId);

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programObjectId);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS,
                    linkStatus, 0);


            // Verify the link status.
            if (linkStatus[0] == 0) {
                // If it failed, delete the program object.
                GLES20.glDeleteProgram(programObjectId);


                return 0;
            }

            // Return the program object ID.
            return programObjectId;
        }

        /**
         * Validates an OpenGL program. Should only be called when developing the
         * application.
         */
        public static boolean validateProgram(int programObjectId) {
            GLES20.glValidateProgram(programObjectId);
            final int[] validateStatus = new int[1];
            GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS,
                    validateStatus, 0);
            Log.v(TAG, "Results of validating program: " + validateStatus[0]
                    + "\nLog:" + GLES20.glGetProgramInfoLog(programObjectId));

            return validateStatus[0] != 0;
        }

        /**
         * Helper function that compiles the shaders, links and validates the
         * program, returning the program ID.
         */
        public static int buildProgram(String vertexShaderSource,
                                       String fragmentShaderSource) {
            int program;

            // Compile the shaders.
            int vertexShader = compileVertexShader(vertexShaderSource);
            int fragmentShader = compileFragmentShader(fragmentShaderSource);

            // Link them into a shader program.
            program = linkProgram(vertexShader, fragmentShader);


            return program;
        }
    }