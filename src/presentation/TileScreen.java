package presentation;

import blister.GameTime;
import blister.input.KeyboardKeyEventArgs;
import ext.csharp.ACEventFunc;
import org.lwjgl.input.Keyboard;

/**
 *
 */
public class TileScreen extends SlideScreen {
    private ACEventFunc<KeyboardKeyEventArgs> fKeyPress = (sender, args) -> {
        switch (args.key) {
            case Keyboard.KEY_1:
            case Keyboard.KEY_2:
            case Keyboard.KEY_3:
            case Keyboard.KEY_4:
                tileViewingType = args.key - Keyboard.KEY_1;
                break;

        }
    };

    /**
     * 0 = Simple tiles with no borders
     * 1 = Simple tiles with a single border type
     * 2 = Complex full connected texture system (Thornmite Overlay)
     * 3 = Random connected texture from Andreas/Georg
     */
    private int tileViewingType = 0;


    @Override
    public void update(GameTime gameTime) {
        super.update(gameTime);


    }

    @Override
    public void draw(GameTime gameTime) {
        super.draw(gameTime);
    }
}
