package com.example.wallet.screens.Wallets;

import android.app.Application;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WalletsViewModelFactory implements ViewModelProvider.Factory {
    SharedPreferences preferences;

    public WalletsViewModelFactory(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WalletsViewModel(preferences);
    }
}
