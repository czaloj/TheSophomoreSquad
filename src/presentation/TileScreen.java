package presentation;

import blister.GameTime;
import blister.input.*;
import egl.*;
import egl.math.*;
import egl.math.Color;
import ext.csharp.ACEventFunc;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTAbgr;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.INTELMapTexture;
import presentation.tiles.ConnectedTextureBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Function;

/**
 *
 */
public class TileScreen extends SlideScreen {
    public static final int TILES_X = 80;
    public static final int TILES_Y = 50;

    private final int[] tiles = new int[TILES_X * TILES_Y];
    private final int[] tileTextureIndices = new int[TILES_X * TILES_Y];

    private SpriteBatch batch;
    private boolean shouldRebuild;

    private GLTexture texSimple;
    private GLTexture texSingleBorder;
    private GLTexture texConnected;
    private GLTexture texLargeRepeat;
    private GLTexture texOverlay;

    private int tileViewingType = 0;

    private final Matrix4 mCamera = new Matrix4();
    private final Matrix4 mIdentity = new Matrix4();
    private CameraController cameraController;

    private ACEventFunc<KeyboardKeyEventArgs> fKeyPress = (sender, args) -> {
        switch (args.key) {
            case Keyboard.KEY_O:
                try {
                    Display.setFullscreen(false);
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    int result = fileChooser.showOpenDialog(new JFrame());
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                        String selectedFileOverlay = selectedFile.substring(0, selectedFile.length() - 4) + "_overlay.png";
                        if (tileViewingType != 3 || new File(selectedFileOverlay).exists()) {
                            switch (tileViewingType) {
                                case 0:
                                    texSimple.setImage2D(selectedFile, true);
                                    break;
                                case 1:
                                    texSingleBorder.setImage2D(selectedFile, true);
                                    break;
                                case 2:
                                    texConnected.setImage2D(selectedFile, true);
                                    break;
                                case 3:
                                    texLargeRepeat.setImage2D(selectedFile, true);
                                    texOverlay.setImage2D(selectedFileOverlay, true);
                                    break;
                            }
                        }
                    }
                    Display.setFullscreen(true);
                }
                catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.exit(-1);
                }
                shouldRebuild = true;
                break;
            case Keyboard.KEY_1:
            case Keyboard.KEY_2:
            case Keyboard.KEY_3:
            case Keyboard.KEY_4:
                tileViewingType = args.key - Keyboard.KEY_1;
                shouldRebuild = true;
                break;
            case Keyboard.KEY_R:
                for (int i = 0; i < (TILES_X * TILES_Y); i++) tiles[i] = (Math.random() < 0.3) ? 0 : 1;
                shouldRebuild = true;
                break;
            case Keyboard.KEY_F:
                int i = 0;
                for (int y = 0; y < TILES_Y; y++) {
                    for (int x = 0; x < TILES_X; x++) {
                        tiles[i++] = (y <= 2 || y >= TILES_Y - 3 || x <= 2 || x >= TILES_X - 3) ? 1 : 0;
                    }
                }
                shouldRebuild = true;
                break;
        }
    };
    private boolean addTile;
    private boolean delTile;
    private ACEventFunc<MouseButtonEventArgs> fMousePress = (sender, args) -> {
        if (args.button == MouseButton.Left) delTile = true;
        if (args.button == MouseButton.Right) addTile = true;
    };
    private ACEventFunc<MouseButtonEventArgs> fMouseRelease = (sender, args) -> {
        if (args.button == MouseButton.Left) delTile = false;
        if (args.button == MouseButton.Right) addTile = false;
    };

    /**
     * 0 = Simple tiles with no borders
     * 1 = Simple tiles with a single border type
     * 2 = Complex full connected texture system (Thornmite Overlay)
     * 3 = Random connected texture from Andreas/Georg
     */

    @Override
    public void onEntry(GameTime gameTime) {
        super.onEntry(gameTime);

        // Random tiles and default camera
        for (int i = 0; i < (TILES_X * TILES_Y); i++) tiles[i] = (Math.random() < 0.3) ? 0 : 1;
        shouldRebuild = true;
        Matrix4.createOrthographic2D(0, TILES_Y * ((float)game.getWidth() / (float)game.getHeight()), 0, TILES_Y, mCamera);

        batch = new SpriteBatch(true);
        try {
            texSimple = new GLTexture(GL.TextureTarget.Texture2D, true);
            texSimple.setImage2D("data/tiles/Simple.png", true);
            texSingleBorder = new GLTexture(GL.TextureTarget.Texture2D, true);
            texSingleBorder.setImage2D("data/tiles/Metal.png", true);
            texConnected = new GLTexture(GL.TextureTarget.Texture2D, true);
            texConnected.setImage2D("data/tiles/Building/plating_thornmite_overlay.png", true);
            texLargeRepeat = new GLTexture(GL.TextureTarget.Texture2D, true);
            texLargeRepeat.setImage2D("data/tiles/Metals/steel_rusty.png", true);
            texOverlay = new GLTexture(GL.TextureTarget.Texture2D, true);
            texOverlay.setImage2D("data/tiles/Metals/steel_rusty_overlay.png", true);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0);

        KeyboardEventDispatcher.OnKeyPressed.add(fKeyPress);
        MouseEventDispatcher.OnMousePress.add(fMousePress);
        MouseEventDispatcher.OnMouseRelease.add(fMouseRelease);
        addTile = false;
        delTile = false;
        cameraController = new CameraController(game.getWidth(), game.getHeight());
        cameraController.start();
    }

    @Override
    public void onExit(GameTime gameTime) {
        super.onExit(gameTime);

        KeyboardEventDispatcher.OnKeyPressed.remove(fKeyPress);
        MouseEventDispatcher.OnMousePress.remove(fMousePress);
        MouseEventDispatcher.OnMouseRelease.remove(fMouseRelease);

        batch.dispose();
        batch = null;
        texSimple.dispose();
        texSimple = null;
        texSingleBorder.dispose();
        texSingleBorder = null;
        texConnected.dispose();
        texConnected = null;
        texLargeRepeat.dispose();
        texLargeRepeat = null;
        texOverlay.dispose();
        texOverlay = null;

        cameraController.stop();
        cameraController = null;
    }

    @Override
    public void update(GameTime gameTime) {
        super.update(gameTime);


        if ((addTile || delTile) && (addTile != delTile)) {
            Vector3 pos = new Vector3(2 * Mouse.getX() / (float)game.getWidth() - 1, 2 * Mouse.getY() / (float)game.getHeight() - 1, 0);
            Matrix4 mCamInv = new Matrix4(mCamera);
            mCamInv.invert();
            mCamInv.mulPos(pos);
            if (pos.x >= 0 && pos.x < TILES_X && pos.y >= 0 && pos.y < TILES_Y) {
                int toggleIndex = (int)pos.y * TILES_X + (int)pos.x;
                int tileToSet = addTile ? 1 : 0;
                if (tiles[toggleIndex] != tileToSet) {
                    tiles[toggleIndex] = tileToSet;
                    shouldRebuild = true;
                }
            }
        }

        if (shouldRebuild) {
            shouldRebuild = false;
            rebuildTiles();
        }

        cameraController.update(mCamera);
    }

    @Override
    public void draw(GameTime gameTime) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        batch.renderBatch(mIdentity, mCamera, BlendState.ADDITIVE, SamplerState.POINT_WRAP, DepthState.NONE, RasterizerState.CULL_NONE);
        super.draw(gameTime);
    }

    private void rebuildTiles() {
        ConnectedTextureBuilder.buildTextureIndices(tiles, tileTextureIndices, TILES_X, TILES_Y);

        batch.begin();
        Vector4 uvr = new Vector4(0, 0, 1, 1);
        Vector2 position = new Vector2();
        Vector2 size = new Vector2(1, 1);
        int i = 0;
        for (int y = 0; y < TILES_Y; y++) {
            for (int x = 0; x < TILES_X; x++) {
                if (tiles[i] == 0) {
                    i++;
                    continue;
                }

                position.set(x, y);
                switch (tileViewingType) {
                    case 0:
                        batch.draw(texSimple, position, size, Color.White, 0.0f);
                        break;
                    case 1:
                    case 2:
                        uvr.x = (tileTextureIndices[i] % 12) / 12.0f;
                        uvr.y = (3 - (tileTextureIndices[i] / 12)) / 4.0f;
                        uvr.z = 1.0f / 12.0f;
                        uvr.w = 0.25f;
                        batch.draw(tileViewingType == 1 ? texSingleBorder : texConnected, uvr, position, size, Color.White, 0.0f);
                        break;
                    case 3:
                        uvr.x = (x % 16) / 16.0f;
                        uvr.y = (y % 16) / 16.0f;
                        uvr.z = 1.0f / 16.0f;
                        uvr.w = 1.0f / 16.0f;
                        batch.draw(texLargeRepeat, uvr, position, size, Color.White, 0.0f);
                        uvr.x = (tileTextureIndices[i] % 12) / 12.0f;
                        uvr.y = (3 - (tileTextureIndices[i] / 12)) / 4.0f;
                        uvr.z = 1.0f / 12.0f;
                        uvr.w = 0.25f;
                        batch.draw(texOverlay, uvr, position, size, Color.White, 0.0f);
                        break;
                }
                i++;
            }
        }
        batch.end(SpriteSortMode.BackToFront);
    }
}
