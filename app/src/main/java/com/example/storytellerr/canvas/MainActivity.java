package com.example.storytellerr.canvas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

        float Previousx,Previousy;

        public class CubeRenderer implements GLSurfaceView.Renderer {

            private Cube cube = new Cube();
            private float rotationVal = 0;
            float xRotation,yRotation;



            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
                // Depth buffer setup.
                gl.glClearDepthf(1.0f);
                // Enables depth testing.
                gl.glEnable(GL10.GL_DEPTH_TEST);
                // The type of depth testing to do.
                gl.glDepthFunc(GL10.GL_LEQUAL);
                // Really nice perspective calculations.
                gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                // Sets the current view port to the new size.
                gl.glViewport(0, 0, width, height);
                // Select the projection matrix
                gl.glMatrixMode(GL10.GL_PROJECTION);
                // Reset the projection matrix
                gl.glLoadIdentity();
                // Calculate the aspect ratio of the window
                GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
                // Select the modelview matrix
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                // Reset the modelview matrix
                gl.glLoadIdentity();
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
                gl.glLoadIdentity();
                gl.glTranslatef(0.0f, 0.0f, -10.0f);
                gl.glRotatef(rotationVal, xRotation, yRotation, 0.0f);
                //   Log.d("hello",Float.toString(rotationVal));
                cube.draw(gl);
                gl.glLoadIdentity();


            }
            public void changerotation(float rotData,float xData ,float yData)
            {
                this.rotationVal = rotationVal + rotData;
                this.xRotation=xData;
                this.yRotation=yData;
            }
        }

        class Cube {

            private FloatBuffer mVertexBuffer;
            private FloatBuffer mColorBuffer;
            private ByteBuffer  mIndexBuffer;

            private float vertices[] = {
                    -1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f,  1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, -1.0f,  1.0f,
                    1.0f, -1.0f,  1.0f,
                    1.0f,  1.0f,  1.0f,
                    -1.0f,  1.0f,  1.0f
            };
            private float colors[] = {
                    0.0f,  1.0f,  0.0f,  1.0f,
                    0.0f,  1.0f,  0.0f,  1.0f,
                    1.0f,  0.5f,  0.0f,  1.0f,
                    1.0f,  0.5f,  0.0f,  1.0f,
                    1.0f,  0.0f,  0.0f,  1.0f,
                    1.0f,  0.0f,  0.0f,  1.0f,
                    0.0f,  0.0f,  1.0f,  1.0f,
                    1.0f,  0.0f,  1.0f,  1.0f
            };

            private byte indices[] = {
                    0, 4, 5, 0, 5, 1,
                    1, 5, 6, 1, 6, 2,
                    2, 6, 7, 2, 7, 3,
                    3, 7, 4, 3, 4, 0,
                    4, 7, 6, 4, 6, 5,
                    3, 0, 1, 3, 1, 2
            };

            public Cube() {
                ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
                byteBuf.order(ByteOrder.nativeOrder());
                mVertexBuffer = byteBuf.asFloatBuffer();
                mVertexBuffer.put(vertices);
                mVertexBuffer.position(0);

                byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
                byteBuf.order(ByteOrder.nativeOrder());
                mColorBuffer = byteBuf.asFloatBuffer();
                mColorBuffer.put(colors);
                mColorBuffer.position(0);

                mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
                mIndexBuffer.put(indices);
                mIndexBuffer.position(0);
            }

            public void draw(GL10 gl) {
                gl.glFrontFace(GL10.GL_CW);

                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
                gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

                gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

                gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE,
                        mIndexBuffer);

                gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
                gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            }
        }



    class GLSurface extends GLSurfaceView {
        public GLSurface(Context context) {
            super(context);
              mRenderer = new CubeRenderer();
              setRenderer(mRenderer);
        }


        public boolean onTouchEvent(final MotionEvent event) {
            queueEvent(new Runnable(){
                public void run() {
                    float x = event.getX();
                    float y = event.getY();
                    Log.d("hello",Float.toString(y));

                    if (event.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        float dx = x - Previousx;
                        float dy = y - Previousy;

                        // reverse direction of rotation above the mid-line
                        if (dy > 0)
                        {
                            mRenderer.changerotation(5,1,0);
                        }

                        if(dx>0)
                        {
                            mRenderer.changerotation(5,0,1);
                        }
                        if(dx<0)
                        {
                            mRenderer.changerotation(5,0,-1);
                        }
                        if(dy<0)
                        {
                            mRenderer.changerotation(5,-1,0);
                        }

                    }

                    Previousx = x;
                    Previousy = y;
                }});
            return true;
        }

        CubeRenderer mRenderer;
    }

        @Override
        public void onCreate(Bundle state) {
            super.onCreate(state);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            GLSurfaceView view = new GLSurface(this);
            setContentView(view);

        }


}
