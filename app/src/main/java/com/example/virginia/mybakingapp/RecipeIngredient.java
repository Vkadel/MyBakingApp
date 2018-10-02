package com.example.virginia.mybakingapp;

class RecipeIngredient {
    int mIngredientId;
    double quantity;
    String measure;
    String ingredient;
    public RecipeIngredient(int ingredientId, double ingredientQuantity,
                            String ingredientMeasure, String ingredientName){
        mIngredientId=ingredientId;
        quantity =ingredientQuantity;
        measure =ingredientMeasure;
        ingredient =ingredientName;
    }

    public void setmIngredientId(int mIngredientId) {
        this.mIngredientId = mIngredientId;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getmIngredientId() {
        return mIngredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
