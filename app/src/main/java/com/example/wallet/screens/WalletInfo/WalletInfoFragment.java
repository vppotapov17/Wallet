package com.example.wallet.screens.WalletInfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.adapters.LastChangesAdapter;
import com.example.wallet.models.Wallet;
import com.example.wallet.utils.DateUtils;
import com.google.android.material.appbar.AppBarLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WalletInfoFragment extends Fragment {

    private Wallet current_wallet;
    private List<Date> sortedDateList;
    private SimpleDateFormat dateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallet_info, container, false);
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);

        if (getArguments() != null) {
            current_wallet = (Wallet) getArguments().getSerializable("current_wallet");
            sortedDateList = DateUtils.sortDates(current_wallet.getValues());
        }
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    private boolean f1 = true, f2 = false;
    @Override
    public void onResume(){
        super.onResume();

        RecyclerView lastChangesRV = getView().findViewById(R.id.lastChangesRV);
        lastChangesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        lastChangesRV.setAdapter(new LastChangesAdapter(current_wallet));

        Double actualCourse = current_wallet.getValues().get(dateFormat.format(sortedDateList.get(0)));

        TextView title = getView().findViewById(R.id.title);

        ImageView wallet1_flag = getView().findViewById(R.id.wallet1_flag);
        EditText wallet1_value = getView().findViewById(R.id.wallet1_value);
        EditText wallet2_value = getView().findViewById(R.id.wallet2_value);


        title.setText(current_wallet.getName());
        wallet1_flag.setImageResource(current_wallet.getFlagIcon());

        Double val = Double.parseDouble(wallet1_value.getText().toString());
        val*= actualCourse;
        val = (double)Math.round(val * 10000) / 10000;
        wallet2_value.setText(val + "");

        wallet1_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                f1 = b;
                f2 = !b;
            }
        });

        wallet2_value.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                f1 = !b;
                f2 = b;
            }
        });

        wallet1_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (f1){
                    if (charSequence.toString().isEmpty()){

                        wallet2_value.setText("");
                    }
                    else {
                        Double val = Double.parseDouble(charSequence.toString());
                        val*= actualCourse;
                        val = (double)Math.round(val * 10000) / 10000;
                        wallet2_value.setText(val + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        wallet2_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (f2){

                    if (charSequence.toString().isEmpty()){
                        wallet1_value.setText("");
                    }
                    else {
                        Double val = Double.parseDouble(charSequence.toString());
                        val/= actualCourse;
                        val = (double)Math.round(val * 10000) / 10000;
                        wallet1_value.setText(val + "");
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
