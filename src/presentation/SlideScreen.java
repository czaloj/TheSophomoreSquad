package presentation;

import blister.GameScreen;
import blister.GameTime;
import blister.ScreenState;
import blister.input.KeyPressEventArgs;
import blister.input.KeyboardEventDispatcher;
import blister.input.KeyboardKeyEventArgs;
import ext.csharp.ACEventFunc;
import org.lwjgl.input.Keyboard;

/**
 * \brief
 */
public class SlideScreen extends GameScreen {
    private ACEventFunc<KeyboardKeyEventArgs> fKeyPress;

    @Override
    public int getNext() {
        return getIndex() + 1;
    }
    @Override
    protected void setNext(int next) {
        // Empty
    }

    @Override
    public int getPrevious() {
        return getIndex() - 1;
    }
    @Override
    protected void setPrevious(int previous) {
        // Empty
    }

    @Override
    public void build() {
        // Empty
    }
    @Override
    public void destroy(GameTime gameTime) {
        // Empty
    }

    @Override
    public void onEntry(GameTime gameTime) {
        fKeyPress = (sender, args) -> {
            switch (args.key) {
                case Keyboard.KEY_LEFT:
                    if (args.getControl()) setState(ScreenState.ChangePrevious);
                    break;
                case Keyboard.KEY_RIGHT:
                    if (args.getControl()) setState(ScreenState.ChangeNext);
                    break;
            }
        };
        KeyboardEventDispatcher.OnKeyPressed.add(fKeyPress);
    }
    @Override
    public void onExit(GameTime gameTime) {
        KeyboardEventDispatcher.OnKeyPressed.remove(fKeyPress);
    }

    @Override
    public void update(GameTime gameTime) {
        // Empty
    }
    @Override
    public void draw(GameTime gameTime) {
        // Empty
    }
}
