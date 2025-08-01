#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in float aTexId;
layout (location = 4) in float aEctityId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out int  fTexId;
out float fEntityId;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = int(aTexId);
    fEntityId = aEctityId;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}