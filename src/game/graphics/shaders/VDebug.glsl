// The camera transformation matrix
uniform mat4 unVP;

in vec4 vPosition;
in vec4 vColor;

out vec4 fColor;

#if defined(USE_TEXTURE)
// Texture use requires passing UV coordinates
in vec2 vUV;
out vec2 fUV;
#endif

void main() {
#if defined(USE_TEXTURE)
    fUV = vUV;
#endif
    fColor = vColor;
    gl_Position = unVP * vPosition;
}
