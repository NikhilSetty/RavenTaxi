package com.diemen.olaoff.utilities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diemen.olaoff.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vikoo on 27/09/15.
 */
public class CabItemAdapter extends RecyclerView.Adapter<CabItemAdapter.ViewHolder> {
    private List<CabItem> mDataset;
    private RecyclerOnItemClickListener.OnItemClickCallback mOnItemClickCallback;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvTime;
        public TextView tvCabType;
        public CircleImageView imgIcon;
        private View mView;


        public ViewHolder(View v) {
            super(v);
            mView = v;
            tvTime = (TextView) v.findViewById(R.id.time);
            tvCabType = (TextView) v.findViewById(R.id.cab_item);
            imgIcon = (CircleImageView) v.findViewById(R.id.cab_icon);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CabItemAdapter(List<CabItem> myDataset, RecyclerOnItemClickListener.OnItemClickCallback itemClickCallback) {
        mDataset = myDataset;
        mOnItemClickCallback = itemClickCallback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CabItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cab_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final CabItem item = mDataset.get(position);
        holder.mView.setOnClickListener(new RecyclerOnItemClickListener(position, mOnItemClickCallback));
        holder.tvTime.setText(item.getTime());
        holder.tvCabType.setText(item.getCabItem());
        holder.imgIcon.setImageResource(item.getIconRes());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
