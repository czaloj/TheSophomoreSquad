#if defined(USE_TEXTURE)
uniform sampler2D unTexture;
in vec2 fUV;
#endif

in vec4 fColor;

out vec4 pColor;

void main() {
    pColor = fColor;
#if defined(USE_TEXTURE)
    pColor *= texture(unTexture, fUV);
#endif
}
