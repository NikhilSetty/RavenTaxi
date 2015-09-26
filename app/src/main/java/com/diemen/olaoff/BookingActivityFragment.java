package com.diemen.olaoff;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.internal.widget.ThemeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookingActivityFragment extends Fragment {

    private RelativeLayout mPrimeRelativeLayout;
    private RelativeLayout mSedanRelativeLayout;
    private RelativeLayout mMiniRelativeLayout;
    private Button mRideNowButton;
    private Button mSelectTimeButton;
    private Integer mMinutes;

    public BookingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_booking, container, false);
        mPrimeRelativeLayout = (RelativeLayout) view.findViewById(R.id.prime_rr);
        mSedanRelativeLayout = (RelativeLayout) view.findViewById(R.id.sedan_rr);
        mMiniRelativeLayout = (RelativeLayout) view.findViewById(R.id.mini_rr);
        mRideNowButton = (Button) view.findViewById(R.id.ride_now_btn);
        mSelectTimeButton = (Button) view.findViewById(R.id.select_time_btn);
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        mSelectTimeButton.setText(now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE));
        mSelectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minutes) {
                                mMinutes = minutes;
                                mSelectTimeButton.setText(hour+":"+minutes);
                            }
                        }
                        , now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE), true);
                timePickerDialog.show(getFragmentManager(),"timepicker");
            }
        });

        mPrimeRelativeLayout.setOnClickListener(onCategoryClick);
        mSedanRelativeLayout.setOnClickListener(onCategoryClick);
        mMiniRelativeLayout.setOnClickListener(onCategoryClick);

        return view;
    }

    View.OnClickListener onCategoryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onCategorySelect((RelativeLayout)v);
        }
    };

    void onCategorySelect(RelativeLayout selectedRR) {

        int childCount = selectedRR.getChildCount();
        ImageView imageView = (ImageView)selectedRR.getChildAt(0);
        TextView textView = (TextView)selectedRR.getChildAt(1);
        Bitmap bmSelected = null;
        Bitmap bmUnSelected = null;
        Integer whichCategory = Integer.parseInt((String) selectedRR.getTag());
        switch (whichCategory) {
            case 1: {
                //mini
                bmSelected = BitmapFactory.decodeResource(getResources(),R.drawable.mini_selected);

                ImageView iV = (ImageView)mPrimeRelativeLayout.getChildAt(0);
                mPrimeRelativeLayout.setBackgroundColor(getResources().getColor(R.color.ola_green));
                bmUnSelected = BitmapFactory.decodeResource(getResources(),R.drawable.prime_unselected);
                iV.setImageBitmap(bmUnSelected);
                TextView tV = (TextView)mPrimeRelativeLayout.getChildAt(1);
                tV.setTextColor(getResources().getColor(R.color.white));

                iV = (ImageView)mSedanRelativeLayout.getChildAt(0);
                mSedanRelativeLayout.setBackgroundColor(getResources().getColor(R.color.ola_green));
                bmUnSelected = BitmapFactory.decodeResource(getResources(),R.drawable.sedan_unselected);
                iV.setImageBitmap(bmUnSelected);
                tV = (TextView)mSedanRelativeLayout.getChildAt(1);
                tV.setTextColor(getResources().getColor(R.color.white));
                break;
            }
            case 2: {
                //sedan
                bmSelected = BitmapFactory.decodeResource(getResources(),R.drawable.sedan_selected);

                ImageView iV = (ImageView)mPrimeRelativeLayout.getChildAt(0);
                mPrimeRelativeLayout.setBackgroundColor(getResources().getColor(R.color.ola_green));
                bmUnSelected = BitmapFactory.decodeResource(getResources(),R.drawable.prime_unselected);
                iV.setImageBitmap(bmUnSelected);
                TextView tV = (TextView)mPrimeRelativeLayout.getChildAt(1);
                tV.setTextColor(getResources().getColor(R.color.white));

                iV = (ImageView)mMiniRelativeLayout.getChildAt(0);
                mMiniRelativeLayout.setBackgroundColor(getResources().getColor(R.color.ola_green));
                bmUnSelected = BitmapFactory.decodeResource(getResources(),R.drawable.mini_unselected);
                iV.setImageBitmap(bmUnSelected);
                tV = (TextView)mMiniRelativeLayout.getChildAt(1);
                tV.setTextColor(getResources().getColor(R.color.white));

                break;
            }
            case 3: {
                //prime
                bmSelected = BitmapFactory.decodeResource(getResources(),R.drawable.prime_selected);

                ImageView iV = (ImageView)mSedanRelativeLayout.getChildAt(0);
                mSedanRelativeLayout.setBackgroundColor(getResources().getColor(R.color.ola_green));
                bmUnSelected = BitmapFactory.decodeResource(getResources(),R.drawable.sedan_unselected);
                iV.setImageBitmap(bmUnSelected);
                TextView tV = (TextView)mSedanRelativeLayout.getChildAt(1);
                tV.setTextColor(getResources().getColor(R.color.white));

                iV = (ImageView)mMiniRelativeLayout.getChildAt(0);
                mMiniRelativeLayout.setBackgroundColor(getResources().getColor(R.color.ola_green));
                bmUnSelected = BitmapFactory.decodeResource(getResources(),R.drawable.mini_unselected);
                iV.setImageBitmap(bmUnSelected);
                tV = (TextView)mMiniRelativeLayout.getChildAt(1);
                tV.setTextColor(getResources().getColor(R.color.white));
                break;
            }
        }

        imageView.setImageBitmap(bmSelected);
        textView.setTextColor(getResources().getColor(R.color.ola_green));
        selectedRR.setBackgroundColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
