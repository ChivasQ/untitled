package com.ferralith.engine.scenes.components;

import com.ferralith.engine.Component;
import com.ferralith.engine.GameObject;
import com.ferralith.engine.Window;
import com.ferralith.engine.components.Sprite;
import com.ferralith.engine.components.SpriteRenderer;
import com.ferralith.engine.inputs.MouseListener;
import com.ferralith.engine.scenes.editor.PropertiesWindow;
import com.ferralith.engine.utils.GenObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Gizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(1,0,0,1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisColorHover = new Vector4f();

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSpriteRenderer;
    private SpriteRenderer yAxisSpriteRenderer;
    private GameObject activeGameObject = null;
    private PropertiesWindow propertiesWindow;

    private Vector2f xAxisOffset = new Vector2f(60, -8);
    private Vector2f yAxisOffset = new Vector2f(8, 60);

    private int gizmoWidth = 16;
    private int gizmoHeight = 48;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = GenObject.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = GenObject.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSpriteRenderer = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSpriteRenderer = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        Window.getScene().addGameObject(this.xAxisObject);
        Window.getScene().addGameObject(this.yAxisObject);
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();
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



        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
//            this.xAxisObject.transform.scale.set(Window.getScene().getCamera().getZoom() * 40, Window.getScene().getCamera().getZoom() * 120);
//            this.yAxisObject.transform.scale.set(Window.getScene().getCamera().getZoom() * 40, Window.getScene().getCamera().getZoom() * 120);
        }


    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (    mousePos.x <= yAxisObject.transform.position.x &&
                mousePos.x >= yAxisObject.transform.position.x - gizmoWidth &&
                mousePos.y <= yAxisObject.transform.position.y &&
                mousePos.y >= yAxisObject.transform.position.y - gizmoHeight) {
            yAxisSpriteRenderer.setColor(yAxisColorHover);
            System.out.println("ha1a");
            return true;
        }

        yAxisSpriteRenderer.setColor(yAxisColor);
        return false;
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (    mousePos.x <= xAxisObject.transform.position.x &&
                mousePos.x >= xAxisObject.transform.position.x - gizmoHeight &&
                mousePos.y >= xAxisObject.transform.position.y &&
                mousePos.y <= xAxisObject.transform.position.y + gizmoWidth) {
            xAxisSpriteRenderer.setColor(xAxisColorHover);
            System.out.println("haa");
            return true;
        }

        xAxisSpriteRenderer.setColor(xAxisColor);
        return false;
    }

    private void setActive() {
        this.xAxisSpriteRenderer.setColor(xAxisColor);
        this.yAxisSpriteRenderer.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSpriteRenderer.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSpriteRenderer.setColor(new Vector4f(0, 0, 0, 0));
    }


}
