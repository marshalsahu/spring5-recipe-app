package com.marshal.service;

import com.marshal.commands.RecipeCommand;
import com.marshal.converters.RecipeCommandToRecipe;
import com.marshal.converters.RecipeToRecipeCommand;
import com.marshal.domain.Recipe;
import com.marshal.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
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
        if(!recipeOptional.isPresent()){
            throw new com.marshal.exceptions.NothingFoundException("Recipe Not Found! for id: "+id.toString());
        }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand getRecipesCommandById(Long id) {
        return recipeToRecipeCommand.convert(getRecipesById(id));
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("saved Recipe Id : "+savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
