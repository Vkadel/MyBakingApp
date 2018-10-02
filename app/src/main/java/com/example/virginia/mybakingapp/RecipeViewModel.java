package com.example.virginia.mybakingapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.AsyncTaskLoader;

import com.example.virginia.mybakingapp.Internet.ConvertToJSON;
import com.example.virginia.mybakingapp.Internet.GetString;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class RecipeViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Recipe>> myRecipiesLive;

    public RecipeViewModel() {
        super();
        if (myRecipiesLive==null){
            new myCalltoGetdata().execute();
        }

    }
    public LiveData<ArrayList<Recipe>> getRecipes (){
        if (myRecipiesLive==null){
            myRecipiesLive=new MutableLiveData<>();

        }
        return myRecipiesLive;
    }

    //Inner class to get the Recipes
    class myCalltoGetdata extends AsyncTask<Long, Long, ArrayList<Recipe>> {
        GetString getString;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected ArrayList<Recipe> doInBackground(Long... longs) {
            ArrayList<Recipe> myRecipesLil = new ArrayList<>();
            getString = new GetString();
            try {
                getString.run("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myRecipesLil;

        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            super.onPostExecute(recipes);
            String myResponse = getString.getStringBack();
            ConvertToJSON convertToJSON = new ConvertToJSON(myResponse);
            ArrayList<Recipe> myRecipiesTransition = convertToJSON.getRecipes();
            myRecipiesLive.postValue(myRecipiesTransition);
            Timber.d(myResponse);

        }

    }
}






