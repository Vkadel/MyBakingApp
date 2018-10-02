package com.example.virginia.mybakingapp.Internet;

import com.example.virginia.mybakingapp.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ConvertToJSON {
    String mInputString;
    ArrayList<Recipe> Recipes;
    public ConvertToJSON(String inputString){
        mInputString=inputString;
        ProcessArray();
    }
    public void ProcessArray(){
        Gson gson=new Gson();
        Type RecipeListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
        Recipes = gson.fromJson(mInputString,RecipeListType);
    }

    public ArrayList<Recipe> getRecipes() {
        return Recipes;
    }
}
