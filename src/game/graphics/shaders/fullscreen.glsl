#version 430

out vec2 fUV;

// Merci to Cort Stratton for the quick trick
void main() {
    fUV = vec2((gl_VertexID << 1) & 2, gl_VertexID & 2);
    gl_Position = vec4(fUV * vec2(2, -2) + vec2(-1, 1), 0, 1);
    fUV.y = 1.0 - fUV.y;
}
