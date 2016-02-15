#version 430

layout(rgba8) uniform image2D unTexture;

layout (local_size_x = 16, local_size_y = 16) in;
void main() {
    ivec2 uv = ivec2(gl_GlobalInvocationID.xy);

    // Load data
    vec4 data = imageLoad(unTexture, uv);
    vec4 nTop = imageLoad(unTexture, uv + ivec2(0, 1));
    vec4 nBottom = imageLoad(unTexture, uv + ivec2(0, -1));
    vec4 nLeft = imageLoad(unTexture, uv + ivec2(-1, 0));
    vec4 nRight = imageLoad(unTexture, uv + ivec2(1, 0));

    // Conway's Game
    float neighbors = nTop.r + nBottom.r + nLeft.r + nRight.r;
    if (data.r > 0.5) {
        // Live cell
        if (neighbors < 1.5 || neighbors > 3.5) {
            data.r = 0.0;
        }
    } else if (neighbors > 2.5 && neighbors < 3.5){
        // Dead cell coming live
        data.r = 1.0;
    }

    imageStore(unTexture, uv, data);
}
