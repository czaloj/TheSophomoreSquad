package presentation.lightning;

import egl.*;
import egl.math.*;
import org.jbox2d.pooling.arrays.FloatArray;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.awt.image.PackedColorModel;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class LightningRenderer {
    private static final int ALPHA_THRESHOLD = 180;

    private LightningEffects fx = new LightningEffects();
    private Color cFade = new Color();

    private GLRenderTarget rtCurrent;
    private GLRenderTarget rtPrevious;
    private final Matrix4 mIdentity = new Matrix4();
    private final Matrix4 mImage = new Matrix4();

    private final Random rng = new Random();

    private GLTexture boltTexture;
    private final Vector4 boltCapLeft = new Vector4();
    private final Vector4 boltMiddle = new Vector4();
    private final Vector4 boltCapRight = new Vector4();

    private final Vector2 boltStart = new Vector2();
    private final Vector2 boltEnd = new Vector2();

    private final Color boltColor = new Color();
    private final Colorf boltColorF = new Colorf();
    private final Vector3 boltColorV = new Vector3();

    private ArrayList<Vector2> positions;
    private ArrayList<Vector3> colors;
    private SpriteBatch batch;

    public LightningRenderer() {
        cFade.set(new Colorf(fx.fadePerFrame, fx.fadePerFrame, fx.fadePerFrame));
    }

    public void init(BufferedImage image, GLTexture boltTexture) {
        batch = new SpriteBatch(true);
        try {
            rtCurrent = new GLRenderTarget(true);
            rtCurrent.setImage2D(image, false);
            rtCurrent.buildRenderTarget();

            rtPrevious = new GLRenderTarget(true);
            rtPrevious.setImage2D(image, false);
            rtPrevious.buildRenderTarget();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Matrix4.createOrthographic2D(0, image.getWidth(), 0, image.getHeight(), mImage);

        this.boltTexture = boltTexture;
        float pCap = (float)boltTexture.getHeight() / (float)boltTexture.getWidth();
        boltCapLeft.set(0.0f, 0.0f, pCap, 1.0f);
        boltMiddle.set(pCap, 0.0f, 1.0f - 2 * pCap, 1.0f);
        boltCapRight.set(1.0f - pCap, 0.0f, pCap, 1.0f);

        positions = new ArrayList<>();
        colors = new ArrayList<>();

        int h = image.getHeight();
        int w = image.getWidth();
        Color c = new Color();
        for(int y = h - 1;y >= 0;y--) {
            for(int x = 0; x < w; x++) {
                c.setIntARGB(image.getRGB(x, y));
                if (c.a() > ALPHA_THRESHOLD) {
                    positions.add(new Vector2(x, (h - 1) - y));
                    colors.add(new Vector3(
                        c.r() / 255.0f,
                        c.g() / 255.0f,
                        c.b() / 255.0f
                    ));
                }
            }
        }
    }
    public void dispose() {
        rtCurrent.dispose();
        rtCurrent = null;
        rtPrevious.dispose();
        rtPrevious = null;
        boltTexture = null;
        batch.dispose();
        batch = null;
    }

    public void setFX(LightningEffects o) {
        fx.boltMin = o.boltMin;
        fx.boltMax = o.boltMax;
        fx.boltJag = o.boltJag;
        fx.boltWidth = o.boltWidth;
        fx.minNeighborDistance = o.minNeighborDistance;
        fx.maxNeighborDistance = o.maxNeighborDistance;
        fx.fadePerFrame = o.fadePerFrame;
        fx.maxBoltsPerFrame = o.maxBoltsPerFrame;
        fx.neighborChecks = o.neighborChecks;
        cFade.set(new Colorf(fx.fadePerFrame, fx.fadePerFrame, fx.fadePerFrame));
    }


    public void updateLightning(float dt) {
        // Swap render targets
        GLRenderTarget tmpRT = rtCurrent;
        rtCurrent = rtPrevious;
        rtPrevious = tmpRT;
        rtCurrent.useTarget();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Draw faded previous frame
        batch.begin();
        batch.draw(rtPrevious, new Vector2(-1, -1), new Vector2(2, 2), cFade, 0.0f);
        batch.end(SpriteSortMode.None);
        batch.renderBatch(mIdentity, mIdentity, BlendState.ADDITIVE, SamplerState.LINEAR_CLAMP, DepthState.NONE, RasterizerState.CULL_NONE);

        batch.begin();
        int boltCount = rng.nextInt(fx.maxBoltsPerFrame);
        Vector3 cStart = null, cEnd = null;
        while (boltCount-- > 0) {
            // Get a random bolt position
            int ri = rng.nextInt(positions.size());
            boltStart.set(positions.get(ri));
            cStart = colors.get(ri);

            // Try to find the closest ending point
            float d2 = fx.maxNeighborDistance * fx.maxNeighborDistance;
            Vector2 end = null;
            for (int i = 0; i < fx.neighborChecks; ++i) {
                ri = rng.nextInt(positions.size());
                Vector2 tmp = positions.get(ri);
                float nd2 = tmp.distSq(boltStart);
                if (nd2 <= d2 && nd2 > (fx.minNeighborDistance * fx.minNeighborDistance)) {
                    cEnd = colors.get(ri);
                    end = tmp;
                    d2 = nd2;
                }
            }

            // Make sure we have a bolt termination point
            if (end == null) continue;
            boltEnd.set(end);

            // Create lightning pieces
            createBolt(boltStart, cStart, boltEnd, cEnd);
        }
        batch.end(SpriteSortMode.None);

        // Render the lightning pieces to the render target
        batch.renderBatch(mIdentity, mImage, BlendState.ADDITIVE, SamplerState.LINEAR_CLAMP, DepthState.NONE, RasterizerState.CULL_NONE);

        GLRenderTarget.unuseTarget();
    }

    public GLTexture getLightningTexture() {
        return rtCurrent;
    }


    private void createBolt(Vector2 s, Vector3 sc, Vector2 e, Vector3 ec) {
        // Find Displacement
        Vector2 d = new Vector2(e).sub(s);
        float dist = d.len();
        float initialDist = dist;
        d.div(dist);
        Vector2 n = new Vector2(-d.y, d.x);

        // Get Range Of Lightning Displacements
        float range = fx.boltMax - fx.boltMin;

        // Start point
        Vector2 pl = new Vector2(s);
        Vector2 nl = new Vector2(s);

        // Add Jags
        while(dist > fx.boltMax) {
            float disp = rng.nextFloat() * range + fx.boltMin;
            s.addMultiple(disp, d);
            boltColorV.set(ec).lerp(sc, dist / initialDist);
            boltColorF.set(boltColorV);
            boltColor.set(boltColorF);
            dist -= disp;
            nl.set(s).addMultiple((rng.nextFloat() * 2 - 1) * fx.boltJag, n);
            drawBolt(pl, nl, boltColor);
            pl.set(nl);
        }

        // End point
        boltColorF.set(ec);
        boltColor.set(boltColorF);
        drawBolt(nl, e, boltColor);
    }
    private void drawBolt(Vector2 p0, Vector2 p1, Color c) {
        float dist = p0.dist(p1);
        float angle = (float)Math.atan2((p1.y - p0.y) / dist, (p1.x - p0.x) / dist);
        batch.draw(
            boltTexture, boltCapLeft, new Vector2(1, 1),
            p0, new Vector2(1, 0.5f), new Vector2(fx.boltWidth * 0.5f, fx.boltWidth), angle,
            c, 0.0f);
        batch.draw(
            boltTexture, boltMiddle, new Vector2(1, 1),
            p0, new Vector2(0, 0.5f), new Vector2(dist, fx.boltWidth), angle,
            c, 0.0f);
        batch.draw(
            boltTexture, boltCapRight, new Vector2(1, 1),
            p1, new Vector2(0, 0.5f), new Vector2(fx.boltWidth * 0.5f, fx.boltWidth), angle,
            c, 0.0f);
    }
}
