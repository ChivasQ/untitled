package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Tags;
import com.ferralith.engine.Window;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.scenes.editor.PropertiesWindow;
import com.ferralith.engine.utils.GenObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gizmo extends Component {
    private final Vector4f xAxisColor = new Vector4f(1,0,0,1);
    private final Vector4f xAxisColorHover = new Vector4f();
    private final Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private final Vector4f yAxisColorHover = new Vector4f();
    private final Vector4f xyAxisColor = new Vector4f(0, 0, 1, 1);
    private final Vector4f xyAxisColorHover = new Vector4f();

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private GameObject xyAxisObject;
    private SpriteRenderer xAxisSpriteRenderer;
    private SpriteRenderer yAxisSpriteRenderer;
    private SpriteRenderer xyAxisSpriteRenderer;
    protected GameObject activeGameObject = null;
    private PropertiesWindow propertiesWindow;

    private Vector2f xAxisOffset = new Vector2f(60, -8);
    private Vector2f yAxisOffset = new Vector2f(8, 60);
    private Vector2f xyAxisOffset = new Vector2f(30, 14);

    private final int gizmoArrowWidth = 16;
    private final int gizmoArrowHeight = 48;
    private final int gizmoSquareWidth = 16;
    private final int gizmoSquareHeight = 32;
    protected boolean xAxisActive;
    protected boolean yAxisActive;
    protected boolean xyAxisActive;


    public Gizmo(Sprite arrowSprite, Sprite squareSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = GenObject.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisObject.addTag(Tags.nonPickable);
        this.xAxisObject.addTag(Tags.doNotSerialize);
        this.yAxisObject = GenObject.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject.addTag(Tags.nonPickable);
        this.yAxisObject.addTag(Tags.doNotSerialize);
        this.xyAxisObject = GenObject.generateSpriteObject(squareSprite, 16, 32);
        this.xyAxisObject.addTag(Tags.nonPickable);
        this.xyAxisObject.addTag(Tags.doNotSerialize);

        this.xAxisSpriteRenderer = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSpriteRenderer = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.xyAxisSpriteRenderer = this.xyAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        Window.getScene().addGameObject(this.xAxisObject);
        Window.getScene().addGameObject(this.yAxisObject);
        Window.getScene().addGameObject(this.xyAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
    }

    @Override
    public void update(float dt) {
        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();
        boolean xyAxisHot = checkXYHoverState();
        //System.out.println(xAxisHot + " " + yAxisHot + " "+ xyAxisHot);

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
            xyAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = false;
            yAxisActive = true;
            xyAxisActive = false;
        } else if ((xyAxisHot || xyAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = false;
            yAxisActive = false;
            xyAxisActive = true;
        } else {
            xAxisActive = false;
            yAxisActive = false;
            xyAxisActive = false;
        }

        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xyAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
            this.xyAxisObject.transform.position.add(this.xyAxisOffset);
//            this.xAxisObject.transform.scale.set(Window.getScene().getCamera().getZoom() * 40, Window.getScene().getCamera().getZoom() * 120);
//            this.yAxisObject.transform.scale.set(Window.getScene().getCamera().getZoom() * 40, Window.getScene().getCamera().getZoom() * 120);
        }


    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (    mousePos.x <= yAxisObject.transform.position.x &&
                mousePos.x >= yAxisObject.transform.position.x - gizmoArrowWidth &&
                mousePos.y <= yAxisObject.transform.position.y &&
                mousePos.y >= yAxisObject.transform.position.y - gizmoArrowHeight) {
            yAxisSpriteRenderer.setColor(yAxisColorHover);
            return true;
        }

        yAxisSpriteRenderer.setColor(yAxisColor);
        return false;
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (    mousePos.x <= xAxisObject.transform.position.x &&
                mousePos.x >= xAxisObject.transform.position.x - gizmoArrowHeight &&
                mousePos.y >= xAxisObject.transform.position.y &&
                mousePos.y <= xAxisObject.transform.position.y + gizmoArrowWidth) {
            xAxisSpriteRenderer.setColor(xAxisColorHover);
            System.out.println("hi");
            return true;
        }

        xAxisSpriteRenderer.setColor(xAxisColor);
        return false;
    }

    private boolean checkXYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x >= xyAxisObject.transform.position.x &&
            mousePos.x <= xyAxisObject.transform.position.x + gizmoSquareWidth &&
            mousePos.y >= xyAxisObject.transform.position.y + gizmoSquareWidth &&
            mousePos.y <= xyAxisObject.transform.position.y + gizmoSquareHeight) {
            xyAxisSpriteRenderer.setColor(xyAxisColorHover);
            return true;
        }

        xyAxisSpriteRenderer.setColor(xyAxisColor);
        return false;
    }

    private void setActive() {
        this.xAxisSpriteRenderer.setColor(xAxisColor);
        this.yAxisSpriteRenderer.setColor(yAxisColor);
        this.xyAxisSpriteRenderer.setColor(xyAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSpriteRenderer.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSpriteRenderer.setColor(new Vector4f(0, 0, 0, 0));
        this.xyAxisSpriteRenderer.setColor(new Vector4f(0, 0, 0, 0));
        xAxisActive = false;
        yAxisActive = false;
        xyAxisActive = false;
    }
}
