#version 430

in vec2 fUV;

out vec4 pColor;

void main() {
    pColor = vec4(fUV, 0.0, 1.0);
}
