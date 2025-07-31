package com.ferralith;

import com.ferralith.engine.Window;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Window w = Window.getInstance();
        w.setSize(1920, 1080).setTitle("a").run();
    }
}