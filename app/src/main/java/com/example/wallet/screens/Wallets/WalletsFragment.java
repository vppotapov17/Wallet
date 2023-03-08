package com.example.wallet.screens.Wallets;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.adapters.WalletListAdapter;
import com.google.android.material.button.MaterialButton;

public class WalletsFragment extends Fragment {

    WalletsViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_wallets, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(this, new WalletsViewModelFactory(null)).get(WalletsViewModel.class);
    }

    @Override
    public void onResume(){
        super.onResume();

        LinearLayout errorLayout = getView().findViewById(R.id.errorLayout);
        MaterialButton retryButton = getView().findViewById(R.id.retryButton);
        ProgressBar progressBar = getView().findViewById(R.id.progressBar);

        new Handler().postDelayed(()->{
            vm.getWalletListLiveData().observe(this, walletList -> {
                progressBar.setVisibility(View.INVISIBLE);
                if (walletList != null){
                    if (walletList.size() != 0){
                        RecyclerView walletListRV = getView().findViewById(R.id.walletListRv);

                        walletListRV.setLayoutManager(new LinearLayoutManager(getContext()));
                        walletListRV.setAdapter(new WalletListAdapter(walletList, getActivity().getSupportFragmentManager()));
                        errorLayout.setVisibility(View.INVISIBLE);
                    }
                    else errorLayout.setVisibility(View.VISIBLE);
                }
                else errorLayout.setVisibility(View.VISIBLE);

            });

        }, 400);

        retryButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.INVISIBLE);
            vm.getData();
        });
    }

}
