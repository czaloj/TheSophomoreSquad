package game.logic;

import blister.input.KeyboardEventDispatcher;
import blister.input.KeyboardKeyEventArgs;
import blister.input.MouseButton;
import ext.csharp.ACEventFunc;
import game.data.EntityInput;
import org.lwjgl.input.Keyboard;

/**
 * Handles translating user input into entity inputs
 */
public class PlayerInputController {
    public static class Input {
        public static final int TYPE_KEYBOARD = 0;
        public static final int TYPE_MOUSE = 1;
        public static final int TYPE_GAMEPAD = 2;

        public static Input createButton(int t, int st) {
            Input input = new Input();
            input.isAxis = false;
            input.type = t;
            input.subType = st;
            input.value = 0.0f;
            return input;
        }
        public static Input createKeyboardAxis(int kPositive, int kNegative) {
            Input input = new Input();
            input.isAxis = true;
            input.type = TYPE_KEYBOARD;
            input.subType = kPositive;
            input.value = 0.0f;
            input.subType2 = kNegative;
            input.value2 = 0.0f;
            return input;
        }
        public static Input createGamepadAxis(int stick) {
            Input input = new Input();
            input.isAxis = true;
            input.type = TYPE_GAMEPAD;
            input.subType = stick;
            input.value = 0.0f;
            return input;
        }

        public int type;
        public int subType;

        /**
         * A value used to symbolize an axis [-1, 1] or button press {0, 1} > 0.5
         */
        public float value;

        /**
         * Used for axis value calculations on a 2-button keyboard
         */
        public boolean isAxis;
        public int subType2;
        public float value2;

        private Input() {
            // Empty
        }

        public float getValue() {
            if (isAxis && type != TYPE_GAMEPAD) {
                return value - value2;
            }
            else {
                return value;
            }
        }
        public boolean getPressValue() {
            return value > 0.5f;
        }


        public void check(int t, int st, float v) {
            if (t != type) return;

            if (isAxis) {
                if (type == TYPE_GAMEPAD && st == subType) {
                    value = v;
                }
                else if (st == subType) {
                    value = v;
                }
                else if (st == subType2) {
                    value2 = v;
                }
            }
            else if (st == subType) {
                value = v;
            }
        }
    }

    public static class KeyMapping {
        public static final int MOVE = 0;
        public static final int JUMP = 1;
        public static final int MELEE = 2;
        public static final int SHOOT = 3;
        public static final int SPECIAL = 4;
        public static final int GRAPPLE = 5;

        public Input[] inputs = {
            Input.createKeyboardAxis(Keyboard.KEY_D, Keyboard.KEY_A),
            Input.createButton(Input.TYPE_KEYBOARD, Keyboard.KEY_D),
            Input.createButton(Input.TYPE_KEYBOARD, Keyboard.KEY_W),
            Input.createButton(Input.TYPE_MOUSE, MouseButton.Right),
            Input.createButton(Input.TYPE_MOUSE, MouseButton.Left),
            Input.createButton(Input.TYPE_KEYBOARD, Keyboard.KEY_SPACE),
            Input.createButton(Input.TYPE_KEYBOARD, Keyboard.KEY_Q)
        };
    }

    private EntityInput input;
    private final KeyMapping mapping = new KeyMapping();

    private ACEventFunc<KeyboardKeyEventArgs> onPress;
    private ACEventFunc<KeyboardKeyEventArgs> onRelease;

    public PlayerInputController() {
        onPress = (sender, args) -> {
            for (Input i : mapping.inputs) i.check(Input.TYPE_KEYBOARD, args.key, 1.0f);
        };
        onRelease = (sender, args) -> {
            for (Input i : mapping.inputs) i.check(Input.TYPE_KEYBOARD, args.key, 0.0f);
        };
    }

    public void init() {
        KeyboardEventDispatcher.OnKeyPressed.add(onPress);
        KeyboardEventDispatcher.OnKeyReleased.add(onRelease);
    }
    public void dispose() {
        KeyboardEventDispatcher.OnKeyPressed.remove(onPress);
        KeyboardEventDispatcher.OnKeyReleased.remove(onRelease);
    }

    public void reset(EntityInput i) {
        input = i;

        // TODO: Reset internal data structures later on
    }

    public void update(float dt) {
        input.moveDirection = mapping.inputs[KeyMapping.MOVE].getValue();
        input.jump = mapping.inputs[KeyMapping.JUMP].getPressValue();
        input.grapple = mapping.inputs[KeyMapping.GRAPPLE].getPressValue();
        input.melee = mapping.inputs[KeyMapping.MELEE].getPressValue();
        input.shoot = mapping.inputs[KeyMapping.SHOOT].getPressValue();
        input.special = mapping.inputs[KeyMapping.SPECIAL].getPressValue();
    }
}
