import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Application {

    private long window;

    public static void main(String[] args){
        new Application().run();
    }

    public void run(){
        init();
        loop();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }


    private void init(){

        //This like prints c errors on the console
        GLFWErrorCallback.createPrint(System.err);

        //Initializing GLFW
        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE); //the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE); //the window will be resizable

        //create window
        window = glfwCreateWindow(800,500,"Hello World",NULL,NULL);
        if(window==NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        //Setting up a key callback. This callback closes the window on holding esc key
        glfwSetKeyCallback(window,(window,key,scancode,action,mods) ->{
           if(key==GLFW_KEY_ESCAPE && action==GLFW_REPEAT){
               glfwSetWindowShouldClose(window,true);
           }
        });

        //Get a thread stack and push a new frame
        try(MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(2);

            //Get the window size passed to above pointers
            glfwGetWindowSize(window,pWidth,pHeight);

            //Get the resolution of your device
            GLFWVidMode  glfwVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            //Centers the window
            glfwSetWindowPos(window,
                    (glfwVidMode.width()-pWidth.get(0))/2,
                    (glfwVidMode.height()-pHeight.get(0))/2
            );
        }//The stack frame is popped automatically

        //Ties the openGL window and context to currently running thread.
        glfwMakeContextCurrent(window);

        //Enable v-sync
        glfwSwapInterval(1);

        //make the window visible
        glfwShowWindow(window);
    }
    private void loop(){
        GL.createCapabilities();
        GL11.glClearColor(0.0f,1.0f,1.0f,0.0f);

        while(!glfwWindowShouldClose(window)){
            GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //clear the framebuffer

            glfwSwapBuffers(window); //swap the color buffers

            //Poll the window events like key callback
            glfwPollEvents();
        }
    }

}
