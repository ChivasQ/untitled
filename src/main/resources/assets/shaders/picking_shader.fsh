#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
flat in int fTexId;
in float fEntityId;

uniform sampler2D uTextures[8];

out vec3 color;

void main() {

    vec4 texColor = vec4(1,1,1,1);

    if (fTexId > 0) {
        int id = int(fTexId);
        texColor = fColor * texture(uTextures[id], fTexCoords);
        //color = vec4(fTexCoords, 0, 1);
    }

    if (texColor.a < 0.5) {
        discard;
    }
    color = vec3(fEntityId, fEntityId, fEntityId);
}