package com.marshal.service;

import com.marshal.converters.RecipeCommandToRecipe;
import com.marshal.converters.RecipeToRecipeCommand;
import com.marshal.domain.Recipe;
import com.marshal.exceptions.NothingFoundException;
import com.marshal.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;
    RecipeCommandToRecipe recipeCommandToRecipe;
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipesById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);
        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipe1 = recipeService.getRecipesById(1L);
        assertNotNull(recipe1);
        verify(recipeRepository,times(1)).findById(anyLong());
        verify(recipeRepository,never()).findAll();

    }

    @Test
    public void getRecipes() {
        Recipe recipe = new Recipe();
        Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipeData);
        Set<Recipe> recipes = recipeService.getRecipes();

       assertEquals(recipes.size(),1);

       verify(recipeRepository, times(1)).findAll();
    }

    @Test(expected = NothingFoundException.class)
    public void getRecipeByIdTestNotFound(){
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipeReturned = recipeService.getRecipesById(1L);
        //should go boom
    }
}