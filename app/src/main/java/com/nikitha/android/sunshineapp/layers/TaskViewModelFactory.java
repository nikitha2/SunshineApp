package com.nikitha.android.sunshineapp.layers;

import android.content.Context;
import android.os.Bundle;
import com.nikitha.android.sunshineapp.database.AppDatabase;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// COMPLETED (1) Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class TaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
         Bundle input;
        Context context;
        public TaskViewModelFactory(Context context,Bundle input) {
           this.input=input;
           this.context=context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TaskViewModel(input,context);
        }
}

