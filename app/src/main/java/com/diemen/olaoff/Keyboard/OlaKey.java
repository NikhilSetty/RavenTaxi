package com.diemen.olaoff.Keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.diemen.olaoff.R;

/**
 * Created by nravishankar on 9/27/2015.
 */
public class OlaKey extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener  {

    private KeyboardView kv;
    private Keyboard keyboard;
    LayoutInflater li;

    private boolean caps = false;

    Keyboard qwertyKeyboard;
    Keyboard symbolsKeyboard;
    Keyboard symbolsShiftKeyboard;

    @Override
    public View onCreateInputView() {
        qwertyKeyboard = new Keyboard(this, R.xml.qwerty);
        symbolsKeyboard = new Keyboard(this, R.xml.symbols);
        symbolsShiftKeyboard = new Keyboard(this, R.xml.symbols_shift);

        li = LayoutInflater.from(this);
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = qwertyKeyboard;
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                Keyboard currentKeyboard = kv.getKeyboard();
                if(currentKeyboard == symbolsKeyboard || currentKeyboard == symbolsShiftKeyboard){
                    HandleShift(true);
                }else {
                    caps = !caps;
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys();
                }
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                HandleShift(false);
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }

    }

    private void HandleShift(boolean shiftToSymbols) {
        Keyboard currentKeyboard = kv.getKeyboard();

        if(shiftToSymbols){
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            if(currentKeyboard == symbolsShiftKeyboard){
                keyboard = symbolsKeyboard;
            }else {
                keyboard = symbolsShiftKeyboard;
            }
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
            setInputView(kv);
        }
        else if(currentKeyboard == qwertyKeyboard) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = symbolsKeyboard;
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
            setInputView(kv);
        }else if(currentKeyboard == symbolsKeyboard){
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = qwertyKeyboard;
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
            setInputView(kv);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
