package com.diemen.olaoff;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class BookingActivityFragment extends Fragment {

    private RelativeLayout mPrimeRelativeLayout;
    private RelativeLayout mSedanRelativeLayout;
    private RelativeLayout mMiniRelativeLayout;
    private Button mRideNowButton;
    private Button mSelectTimeButton;

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

        mSelectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
