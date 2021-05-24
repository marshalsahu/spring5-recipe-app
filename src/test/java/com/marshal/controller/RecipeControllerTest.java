package com.marshal.controller;

import com.marshal.commands.RecipeCommand;
import com.marshal.domain.Recipe;
import com.marshal.exceptions.NothingFoundException;
import com.marshal.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    public void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);


        when(recipeService.getRecipesById(anyLong())).thenReturn(recipe);

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostNewRecipeForm() throws Exception{
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
                .param("description","some string")
                .param("directions","some string")

        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/show/"));

    }

    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception{
        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(MockMvcRequestBuilders.post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description","some string")
                .param("id","")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"));

    }

    @Test
    public void getRecipeNotFound() throws Exception{
        when(recipeService.getRecipesById(anyLong())).thenThrow(NothingFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void getRecipeNumberFormat() throws Exception{
        when(recipeService.getRecipesById(anyLong())).thenThrow(NumberFormatException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/abc/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
}