package presentation;

import blister.GameScreen;
import blister.GameTime;

/**
 * \brief
 */
public class SlideScreen extends GameScreen {
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
        // Empty
    }
    @Override
    public void onExit(GameTime gameTime) {
        // Empty
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
