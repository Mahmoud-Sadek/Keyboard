package com.example.mahmoud.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

/**
 * Created by Mahmoud on 7/13/2016.
 */
public class Mykeyboard extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private boolean caps = false;
    private int mKeyboardState = R.integer.keyboard_normal;
    Keyboard numberKeyBoard;
    Keyboard normalKeyBoard;
    Keyboard arabicKeyBoard;
    Keyboard secondNumberKeyBoard;

    final static int ARABIC_KEYBOARD = 1111;
    final static int SECOND_NUMBER_KEYBOARD = 2222;
    final static int NUMBER_KEYBOARD = 3333;


    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty2);

        kv.setOnKeyboardActionListener(this);

        normalKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_normal);
        numberKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_numbers);
        arabicKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_arabic);
        secondNumberKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_numbers_second);

        kv.setKeyboard(normalKeyBoard);
        return kv;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
    }

    private void playClick(int keyCode) {
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }


    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        if( primaryCode== Keyboard.KEYCODE_MODE_CHANGE) {
            if(kv != null) {
                if(mKeyboardState == R.integer.keyboard_normal){
                    //change to symbol keyboard
                    if(arabicKeyBoard== null){
                        arabicKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_arabic);
                    }

                    kv.setKeyboard(arabicKeyBoard);
                    mKeyboardState = R.integer.keyboard_arabic;
                } else {
                    if(normalKeyBoard== null){
                        normalKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_normal);
                    }

                    kv.setKeyboard(normalKeyBoard);
                    mKeyboardState = R.integer.keyboard_normal;
                }
                //no shifting
                kv.setShifted(false);
            }
        }

        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_MODE_CHANGE:
                //change
                break;
            case ARABIC_KEYBOARD:
                if(kv != null) {
                    if(mKeyboardState == R.integer.keyboard_normal || mKeyboardState == R.integer.keyboard_numbers){
                        //change to symbol keyboard
                        if(arabicKeyBoard== null){
                            arabicKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_arabic);
                        }

                        kv.setKeyboard(arabicKeyBoard);
                        mKeyboardState = R.integer.keyboard_arabic;
                    } else {
                        if(normalKeyBoard== null){
                            normalKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_normal);
                        }

                        kv.setKeyboard(normalKeyBoard);
                        mKeyboardState = R.integer.keyboard_normal;
                    }
                    //no shifting
                    kv.setShifted(false);
                }
                break;
            case NUMBER_KEYBOARD:
                if(kv != null) {
                    if(mKeyboardState == R.integer.keyboard_normal || mKeyboardState == R.integer.keyboard_arabic){
                        //change to symbol keyboard
                        if(numberKeyBoard== null){
                            numberKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_numbers);
                        }

                        kv.setKeyboard(numberKeyBoard);
                        mKeyboardState = R.integer.keyboard_numbers;
                    } else {
                        if(normalKeyBoard== null){
                            normalKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_normal);
                        }

                        kv.setKeyboard(normalKeyBoard);
                        mKeyboardState = R.integer.keyboard_normal;
                    }
                    //no shifting
                    kv.setShifted(false);
                }
                break;
            case SECOND_NUMBER_KEYBOARD:
                if(kv != null) {
                    if(mKeyboardState == R.integer.keyboard_numbers){
                        //change to symbol keyboard
                        if(secondNumberKeyBoard== null){
                            secondNumberKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_numbers_second);
                        }

                        kv.setKeyboard(secondNumberKeyBoard);
                        mKeyboardState = R.integer.keyboard_numbers_second;
                    } else {
                        if(numberKeyBoard== null){
                            numberKeyBoard = new Keyboard(getBaseContext(), R.xml.qwerty2, R.integer.keyboard_numbers);
                        }

                        kv.setKeyboard(numberKeyBoard);
                        mKeyboardState = R.integer.keyboard_numbers;
                    }
                    //no shifting
                    kv.setShifted(false);
                }
                break;
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code), 1);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeDown() {
        Toast.makeText(Mykeyboard.this, "swipe", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swipeLeft() {
        Toast.makeText(Mykeyboard.this, "swipe", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swipeRight() {
        Toast.makeText(Mykeyboard.this, "swipe", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swipeUp() {
        Toast.makeText(Mykeyboard.this, "swipe", Toast.LENGTH_SHORT).show();
    }
}