package com.ferralith.engine.components;

import com.ferralith.engine.Component;

public class SpriteRenderer extends Component {

    @Override
    public void start() {
        System.out.println("started");
    }

    @Override
    public void update(float dt) {
        System.out.println("I am updating");
    }
}
