package com.example.wallet.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.models.Wallet;
import com.example.wallet.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;


public class WalletListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    GregorianCalendar lastDateCalendar;
    private List<Wallet> walletList;
    private List<Date> sortedDates;

    public WalletListAdapter(List<Wallet> walletList){
        this.walletList = walletList;
        this.lastDateCalendar = new GregorianCalendar();

        this.sortedDates = DateUtils.sortDates(walletList.get(0).getValues());
        this.lastDateCalendar.setTime(sortedDates.get(0));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.last_update_text, parent, false)) {};
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item, parent, false)) {};
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0){

            TextView lastUpdateText = holder.itemView.findViewById(R.id.lastUpdateText);


            String months[] = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
            String currentMonth = months[lastDateCalendar.get(Calendar.MONTH)];
            String text = "По данным ЦБ РФ на ";
            text += lastDateCalendar.get(Calendar.DAY_OF_MONTH) + " " + currentMonth + " " + lastDateCalendar.get(Calendar.YEAR) + " г.";

            lastUpdateText.setText(text);
        }
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            ImageView flag = holder.itemView.findViewById(R.id.walletFlag);
            TextView name = holder.itemView.findViewById(R.id.walletName);
            TextView value = holder.itemView.findViewById(R.id.walletValue);
            TextView difference = holder.itemView.findViewById(R.id.difference);

            Wallet wallet = walletList.get(position - 1);

            double todayValue = wallet.getValues().get(dateFormat.format(sortedDates.get(0)));
            double yesterdayValue = wallet.getValues().get(dateFormat.format(sortedDates.get(1)));

            double differenceValue = todayValue - yesterdayValue;
            differenceValue = (double) Math.round(differenceValue * 1000) / 1000;

            if (differenceValue > 0) {
                difference.setTextColor(holder.itemView.getResources().getColor(R.color.positiveGreen));
                difference.setText("+" + differenceValue);
            }
            else if (differenceValue < 0){
                difference.setTextColor(holder.itemView.getResources().getColor(R.color.negativeRed));
                difference.setText(differenceValue + "");
            }
            else difference.setVisibility(View.GONE);


            flag.setImageResource(wallet.getFlagIcon());
            name.setText(wallet.getName());
            value.setText(wallet.getValues().get(dateFormat.format(lastDateCalendar.getTime())) + " ₽");
        }
    }


    @Override
    public int getItemCount() {
        return walletList.size() + 1;
    }
}
