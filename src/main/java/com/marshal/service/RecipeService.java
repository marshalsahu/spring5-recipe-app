package com.marshal.service;


import com.marshal.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe getRecipesById(Long id);
}
