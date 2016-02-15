uniform sampler2D unTexture;

in vec2 fUV;
out vec4 pColor;

void main() {
    gl_FragColor = texture(unTexture, fUV);
}
