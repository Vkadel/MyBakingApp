package com.example.virginia.mybakingapp;

import java.util.ArrayList;

public class Recipe {
    String id;
    String name;
    String servings;
    ArrayList<RecipeStep> steps;
    ArrayList<RecipeIngredient> ingredients;
    String image;

    public Recipe(String recipeid,String recipeServings,String recipeName,
                  ArrayList<RecipeStep> recipeSteps,
                  ArrayList<RecipeIngredient> recipeIngredients,String imageurl) {
        id =recipeid;
        name =recipeName;
        servings =recipeServings;
        steps =recipeSteps;
        ingredients =recipeIngredients;
        image=imageurl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public void setSteps(ArrayList<RecipeStep> steps) {
        this.steps = steps;
    }

    public String getServings() {
        return servings;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
