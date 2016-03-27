package presentation;

import blister.input.*;
import egl.math.Matrix4;
import egl.math.Vector2;
import ext.csharp.ACEventFunc;

/**
 * \brief
 */
public class CameraController {
    private float w;
    private float h;

    private boolean mouseButtonHeld = false;
    private final Vector2 mouseDelta = new Vector2();
    private float zoomDelta = 0.0f;

    public CameraController(int screenWidth, int screenHeight) {
        w = screenWidth;
        h = screenHeight;
    }

    private ACEventFunc<MouseWheelEventArgs> fMouseWheel = (sender, args) -> {
        zoomDelta += args.ScrollChange / 120;
    };
    private ACEventFunc<MouseMoveEventArgs> fMouseMotion = (sender, args) -> {
        if (mouseButtonHeld) mouseDelta.add((args.dx / w) * 2, (args.dy / h) * 2);
    };
    private ACEventFunc<MouseButtonEventArgs> fMousePress = (sender, args) -> {
        if (args.button == MouseButton.Left) mouseButtonHeld = true;
    };
    private ACEventFunc<MouseButtonEventArgs> fMouseRelease = (sender, args) -> {
        if (args.button == MouseButton.Left) mouseButtonHeld = false;
    };

    public void start() {
        MouseEventDispatcher.OnMouseScroll.add(fMouseWheel);
        MouseEventDispatcher.OnMouseMotion.add(fMouseMotion);
        MouseEventDispatcher.OnMousePress.add(fMousePress);
        MouseEventDispatcher.OnMouseRelease.add(fMouseRelease);
    }
    public void stop() {
        MouseEventDispatcher.OnMouseScroll.remove(fMouseWheel);
        MouseEventDispatcher.OnMouseMotion.remove(fMouseMotion);
        MouseEventDispatcher.OnMousePress.remove(fMousePress);
        MouseEventDispatcher.OnMouseRelease.remove(fMouseRelease);
    }

    public void update(Matrix4 mCamera) {
        if (zoomDelta != 0) {
            mCamera.mulAfter(Matrix4.createScale((float)Math.pow(1.1, zoomDelta)));
            zoomDelta = 0;
        }
        mCamera.mulAfter(Matrix4.createTranslation(mouseDelta.x, mouseDelta.y, 0));
        mouseDelta.setZero();
    }
}
