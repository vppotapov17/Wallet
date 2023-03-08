package com.example.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.models.Wallet;
import com.example.wallet.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LastChangesAdapter extends RecyclerView.Adapter {

    private Wallet current_wallet;
    private List<Date> sortedDateList;
    private SimpleDateFormat simpleDateFormat;

    public LastChangesAdapter(Wallet current_wallet){
        this.current_wallet = current_wallet;
        this.sortedDateList = DateUtils.sortDates(current_wallet.getValues());
        this.simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.last_change_item, parent, false)) {};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView date = holder.itemView.findViewById(R.id.lastChangeDate);
        TextView value = holder.itemView.findViewById(R.id.lastChangeValue);
        TextView difference = holder.itemView.findViewById(R.id.lastChangeDifference);

        Double current_value = current_wallet.getValues().get(simpleDateFormat.format(sortedDateList.get(position)));


        if (position == getItemCount() - 1){
            difference.setVisibility(View.INVISIBLE);
        }
        else {
            Double past_value = current_wallet.getValues().get(simpleDateFormat.format(sortedDateList.get(position + 1)));
            Double diff = current_value - past_value;
            diff = (double)Math.round(diff * 1000) / 1000;
            if (diff > 0){
                difference.setTextColor(holder.itemView.getResources().getColor(R.color.positiveGreen));
                difference.setText("+" + diff);
            }
            else if (diff < 0){
                difference.setTextColor(holder.itemView.getResources().getColor(R.color.negativeRed));
                difference.setText(diff + "");
            }
        }

        date.setText(DateUtils.getDateString(sortedDateList.get(position)));

        value.setText(current_value + " ₽");
    }

    @Override
    public int getItemCount() {
        return current_wallet.getValues().size();
    }
}
