#version 430

uniform sampler2D unTexture;

in vec2 fUV;
out vec4 pColor;

void main() {
    gl_FragColor = vec4(fUV, 0, 1); // texture(unTexture, fUV) * 0.0001;
}
