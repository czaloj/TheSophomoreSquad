#define GROUP_SIZE 16
#define MAX_BLUR_DISTANCE 8

layout(rgba16f) uniform image2D unTexture;
layout(rgba16f) uniform image2D unTextureDest;
uniform int unBlurDistance;
uniform float unBlurWeights[MAX_BLUR_DISTANCE];

layout (local_size_x = GROUP_SIZE, local_size_y = GROUP_SIZE) in;
void main() {
    ivec2 uv = ivec2(gl_GlobalInvocationID.xy);

    vec4 accum = unBlurWeights[0] * imageLoad(unTexture, uv);
    for (int i = 1; i < unBlurDistance; i++) {
#ifdef VERTICAL
        accum += unBlurWeights[i] * (imageLoad(unTexture, uv + ivec2(0, i)) + imageLoad(unTexture, uv - ivec2(0, i)));
#else
        accum += unBlurWeights[i] * (imageLoad(unTexture, uv + ivec2(i, 0)) + imageLoad(unTexture, uv - ivec2(i, 0)));
#endif
    }
    imageStore(unTextureDest, uv, accum);
}
