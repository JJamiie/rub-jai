package com.rashata.jamie.spend.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rashata.jamie.spend.R;


/**
 * Created by jjamierashata on 1/8/2017 AD.
 */

public class PasscodeAdapter extends RecyclerView.Adapter<PasscodeAdapter.PasscodeViewHolder> {
    private static final String TAG = "PasscodeAdapter";
    private String passcode = "";
    private final int MAX_CLICKED = 4;
    private ActivityListener activityListener;

    public PasscodeAdapter(ActivityListener activityListener) {
        this.activityListener = activityListener;
    }

    public interface ActivityListener {
        void onFillPasscodeFinish(String passcode);
    }

    @Override
    public PasscodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passcode_blue, parent, false);
        PasscodeViewHolder pvh = new PasscodeViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PasscodeViewHolder holder, int position) {
        if (position < passcode.length()) {
            holder.v_passcode.setVisibility(View.VISIBLE);
        } else {
            holder.v_passcode.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return MAX_CLICKED;
    }

    public class PasscodeViewHolder extends RecyclerView.ViewHolder {
        View v_passcode;

        public PasscodeViewHolder(View itemView) {
            super(itemView);
            v_passcode = itemView.findViewById(R.id.v_passcode);
        }
    }

    public void clicked(int number) {
        passcode = passcode + number;
        Log.d(TAG,"passcode:" + passcode);
        notifyDataSetChanged();
        if (passcode.length() > 3) {
            activityListener.onFillPasscodeFinish(passcode);
        }
    }

    public void delete() {
        if (passcode.isEmpty()) return;
        passcode = passcode.substring(0, passcode.length() - 1);
        notifyDataSetChanged();
    }

    public void clear(){
        passcode =  "";
        notifyDataSetChanged();
    }
}
