package com.ferralith.engine.utils;

import org.joml.Vector2f;

import static java.lang.Math.cos;

public class Mth {
    private static final double DEG_TO_RAD = Math.PI/180;
    private static final double RAD_TO_DEG = 180/Math.PI;


    public static Vector2f rotate(Vector2f point, Vector2f pivot, float angleInDegrees) {
        float angleInRagians = (float) (angleInDegrees * DEG_TO_RAD);

        Vector2f p = point.sub(pivot);

        float s = (float) Math.sin(angleInRagians);
        float c = (float) cos(angleInRagians);

        float xnew = p.x * c - p.y * s;
        float ynew = p.x * s + p.y * c;

        point.x = xnew + pivot.x;
        point.y = ynew + pivot.y;

        return point;
    }
}
