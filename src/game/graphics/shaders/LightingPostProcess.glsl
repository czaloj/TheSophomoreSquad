#version 430

layout(rgba8) uniform image2D unTexture;

layout (local_size_x = 16, local_size_y = 16) in;
void main() {
    ivec2 uv = ivec2(gl_GlobalInvocationID.xy);
    imageStore(unTexture, uv, vec4(1.0, 0.0, 0.0, 0.0));
}
