package com.marshal.controller;

import com.marshal.commands.RecipeCommand;
import com.marshal.exceptions.NothingFoundException;
import com.marshal.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class RecipeController {

    private static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.getRecipesById(Long.parseLong(id)));
        return "recipe/show";
    }

    @RequestMapping("/recipe/new")
    public String showForm(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    @RequestMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.getRecipesById(Long.parseLong(id)));
        return RECIPE_RECIPEFORM_URL;
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors()
                    .forEach(objectError -> {
                        log.debug(objectError.toString());
                    });
            return RECIPE_RECIPEFORM_URL;
        }
        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/"+savedRecipe.getId()+"/show/";
    }
    @RequestMapping("/recipe/{id}/delete")
    public String deleteById(@PathVariable Long id){
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NothingFoundException.class)
    public ModelAndView handleNotFound(Exception exception){
        log.error("Handle Not Found");
        log.error(exception.getMessage());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("404error");
        mv.addObject("exception", exception);
        return mv;
    }



}
