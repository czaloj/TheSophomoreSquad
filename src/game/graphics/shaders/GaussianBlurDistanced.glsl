#define GROUP_SIZE 16
#define MAX_BLUR_SAMPLES 8

layout(rgb16f) uniform image2D unTexture;
uniform int unBlurDistance;
uniform int unBlurDistances[MAX_BLUR_SAMPLES];
uniform float unBlurWeights[MAX_BLUR_SAMPLES];

layout (local_size_x = GROUP_SIZE, local_size_y = GROUP_SIZE) in;
void main() {
    ivec2 uv = ivec2(gl_GlobalInvocationID.xy);

    vec3 accum = unBlurWeights[0] * imageLoad(unTexture, uv);
    for (int i = 1; i < unBlurDistance; i++) {
#ifdef VERTICAL
        accum += unBlurWeights[i] * (imageLoad(unTexture, uv + ivec2(0, unBlurDistances[i])) + imageLoad(unTexture, uv - ivec2(0, unBlurDistances[i])));
#else
        accum += unBlurWeights[i] * (imageLoad(unTexture, uv + ivec2(unBlurDistances[i], 0)) + imageLoad(unTexture, uv - ivec2(unBlurDistances[i], 0)));
#endif
    }
    imageStore(unTexture, uv, accum);
}
