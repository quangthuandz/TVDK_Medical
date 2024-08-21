package com.example.tvdkmedical.repositories.callbacks;

import java.util.List;

public interface Callback<T> {
    void onCallback(List<T> objects);
}