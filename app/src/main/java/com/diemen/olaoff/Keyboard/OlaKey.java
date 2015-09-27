package com.diemen.olaoff.Keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.diemen.olaoff.R;
import com.diemen.olaoff.utilities.CabItem;
import com.diemen.olaoff.utilities.CabItemAdapter;
import com.diemen.olaoff.utilities.RecyclerOnItemClickListener;
import com.diemen.olaoff.utilities.SmsSender;

import java.util.ArrayList;

/**
 * Created by nravishankar on 9/27/2015.
 */
public class OlaKey extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener  {

    private KeyboardView kv;
    private Keyboard keyboard;
    LayoutInflater li;

    private boolean caps = false;

    private Keyboard qwertyKeyboard;
    private Keyboard symbolsKeyboard;
    private Keyboard symbolsShiftKeyboard;


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
            case -456:
                ChangeLayout();
                break;
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
        Toast.makeText(getApplicationContext(), "h", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void swipeRight() {
        ChangeLayout();
    }

    private Button mRideNowButton;
    private boolean isConnected;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CabItem> mCabList;
    private CabItemAdapter mAdapter;

    private static String selectedCab = "mini";
    private RecyclerOnItemClickListener.OnItemClickCallback mOnItemClickCallback = new RecyclerOnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, int position) {
            switch (view.getId()) {
                default:
                    mAdapter.disableRow(position);
                    Toast.makeText(getApplicationContext(), mCabList.get(position).getCabItem()+" selected", Toast.LENGTH_SHORT).show();
                    selectedCab = mCabList.get(position).getCabItem();
                    break;
            }
        }
    };
    private void ChangeLayout() {


        LinearLayout v = (LinearLayout) getLayoutInflater().inflate(R.layout.cab_keyboard_layout, null);
        mRideNowButton = (Button) v.findViewById(R.id.ride_now_btn);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCabList = CabItem.getCabList();

        mAdapter = new CabItemAdapter(mCabList, mOnItemClickCallback, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        v.setOnTouchListener(new View.OnTouchListener() {
            int downX, upX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    Log.i("event.getX()", " downX " + downX);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    upX = (int) event.getX();
                    Log.i("event.getX()", " upX " + downX);
                    if (upX - downX > 100) {
                        showKeyPad();
                    } else if (downX - upX > -100) {
                        showKeyPad();
                    }
                    return true;

                }
                return false;
            }
        });

        mRideNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCab.isEmpty() || selectedCab == null) {
                    Toast.makeText(getApplicationContext(), "Please Select a cab type!", Toast.LENGTH_SHORT).show();
                } else {
                    String message = "OLA," + "12.9501069" + "," + "77.6416856" + "," + "sdfas345" + "," + selectedCab + "," + "kundalahalli" + "," + "30";
                    SmsSender.sendSms("7022256703", message, new Location(""), "mini");
                }
            }
        });
        setInputView(v);
    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public void showKeyPad(){
        keyboard = qwertyKeyboard;
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        setInputView(kv);
    }
}
