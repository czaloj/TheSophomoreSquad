#define GROUP_SIZE 16
#define MAX_BLUR_DISTANCE 8

layout(rgb16f) uniform image2D unTexture;
uniform int unBlurDistance;
uniform float unBlurWeights[MAX_BLUR_DISTANCE];

shared vec3 shAccumulationAmounts[GROUP_SIZE * GROUP_SIZE];

layout (local_size_x = GROUP_SIZE, local_size_y = GROUP_SIZE) in;
void main() {
    ivec2 uv = ivec2(gl_GlobalInvocationID.xy);

    // Vertical Blur
    vec3 accum = unBlurWeights[0] * imageLoad(unTexture, uv);
    for (int i = 1; i < unBlurDistance; i++) {
        accum += unBlurWeights[i] * (imageLoad(unTexture, uv + ivec2(0, i)) + imageLoad(unTexture, uv - ivec2(0, i)));
    }
    shAccumulationAmounts[gl_LocalInvocationIndex] = accum;
    barrier();

    // Horizontal Blur
    accum *= unBlurWeights[0];
    for (int i = 1; i < unBlurDistance; i++) {
        accum += unBlurWeights[i] * (imageLoad(unTexture, uv + ivec2(0, i)) + imageLoad(unTexture, uv - ivec2(0, i)));
    }
    shAccumulationAmounts[gl_LocalInvocationIndex] = accum;


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
