package com.nikitha.android.sunshineapp.layers;

import android.content.Context;
import android.os.Bundle;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

// COMPLETED (1) Make this class extend ViewModel ViewModelProvider.NewInstanceFactory
public class TaskViewModelDetailsFactory extends ViewModelProvider.NewInstanceFactory {
         Bundle input;
        Context context;
        public TaskViewModelDetailsFactory(Context context) {
           this.context=context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TaskViewModelDetails(context);
        }
}

