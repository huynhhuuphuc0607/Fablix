package com.example.huynh.fablix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.ViewHolder> {

    private Context mContext;
    private int resID;
    private ArrayList<Star> mStarList;
    private List<LinearLayout> linearLayouts;
    private int[] starDrawables;
    private int lastPosition = -1;
    public StarAdapter(Context c, int resID, ArrayList<Star> starList) {
        mContext = c;
        this.resID = resID;
        mStarList = starList;
        linearLayouts = new ArrayList<>();
        starDrawables = new int[]{R.drawable.vindiesel,R.drawable.jlaw,R.drawable.willsmith, R.drawable.dwayne_johnson};
    }

    @Override
    public int getItemCount() {
        return mStarList.size();
    }

    public LinearLayout getLinearLayout(int position) {
        return linearLayouts.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(resID, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Star star = mStarList.get(i);
        LinearLayout starLinearLayout = viewHolder.starLinearLayout;
        CircleImageView starAvatarCircleImageView = viewHolder.starAvatarCircleImageView;
        TextView starNameTextView = viewHolder.starNameTextView;

        starAvatarCircleImageView.setImageResource(starDrawables[i % 4]);
        star.setResId(starDrawables[i%4]);
       // starAvatarCircleImageView.setImageResource(star.getResId());
        starNameTextView.setText(star.getName());

        starLinearLayout.setTag(R.id.id1, star);
        starLinearLayout.setTag(R.id.id2, i + "|" + starAvatarCircleImageView.getId() + "|" + starNameTextView.getId());
        linearLayouts.add(starLinearLayout);
        starLinearLayout.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_recyclerview));
        lastPosition = i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView starAvatarCircleImageView;
        public TextView starNameTextView;
        public LinearLayout starLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            starLinearLayout = itemView.findViewById(R.id.starLinearLayout);
            starAvatarCircleImageView = itemView.findViewById(R.id.starAvatarCircleImageView);
            starNameTextView = itemView.findViewById(R.id.starNameTextView);
        }
    }
}
