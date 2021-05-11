package com.marshal.service;

import com.marshal.domain.Recipe;
import com.marshal.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
        Set<Recipe> recipes = new HashSet<>();

        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    public Recipe getRecipesById(Long id) {
        Optional<Recipe> recipeOptional= recipeRepository.findById(id);
        if(!recipeOptional.isPresent())
            throw new RuntimeException("Recipe Not Found");

        return recipeOptional.get();
    }
}
