package com.marshal.controller;

import com.marshal.commands.RecipeCommand;
import com.marshal.exceptions.NothingFoundException;
import com.marshal.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class RecipeController {
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
        return "recipe/recipeform";
    }

    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand recipeCommand){
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleBadRequest(Exception exception){
        log.error("Handle Bad Request");
        log.error(exception.getMessage());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("400error");
        mv.addObject("exception", exception);
        return mv;
    }

}
