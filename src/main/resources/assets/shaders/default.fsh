#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
flat in int fTexId;

uniform sampler2D uTextures[3];

out vec4 color;

float normalize1(float x, float minVal, float maxVal) {
    return clamp((x - minVal) / (maxVal - minVal), 0.0, 1.0);
}

void main() {
    //color = vec4(normalize1(fTexId, 0, 8), 0.0, 0, 1.0);
    color = texture(uTextures[fTexId], fTexCoords);
}