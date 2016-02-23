package game.graphics;

import egl.GLTexture;

/**
 * \brief
 */
public class RenderData {
    /**
     * Mesh information for the environment
     */
    public MeshGeometry meshEnvironment;

    /**
     * Texture contains damage information for the environment
     */
    public GLTexture textureDamage;
    /**
     * FBO used to draw onto damage texture
     */
    public int fboDamage;
    /**
     * Stencil mask for the damage
     */
    public int stencilDamage;
}
