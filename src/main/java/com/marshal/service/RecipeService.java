package com.marshal.service;


import com.marshal.commands.RecipeCommand;
import com.marshal.domain.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
    Recipe getRecipesById(Long id);
    RecipeCommand getRecipesCommandById(Long id);
    RecipeCommand saveRecipeCommand(RecipeCommand command);
    void deleteById(Long id);
}
