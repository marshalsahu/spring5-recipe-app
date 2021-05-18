package com.marshal.controller;

import com.marshal.commands.IngredientCommand;
import com.marshal.commands.RecipeCommand;
import com.marshal.commands.UnitOfMeasureCommand;
import com.marshal.service.IngredientService;
import com.marshal.service.RecipeService;
import com.marshal.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{id}/ingredients")
    public String listIngredients(@PathVariable String id, Model model){
        log.debug("Getting ingredient list for recipe id"+ id);

        //use command object to avoid lazy load errors in Thymeleaf
        model.addAttribute("recipe",recipeService.getRecipesCommandById(Long.parseLong(id)));
        return "recipe/ingredient/list";
    }

    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredients(@PathVariable String recipeId,@PathVariable String ingredientId
                                                ,Model model ){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(recipeId),Long.parseLong(ingredientId)));
        return "recipe/ingredient/show";
    }

    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable String recipeId, Model model){
        RecipeCommand recipeCommand = recipeService.getRecipesCommandById(Long.valueOf(recipeId));
        //todo raise exception if null
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(Long.valueOf(recipeId));
        model.addAttribute("ingredient",ingredientCommand);
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("uomList",unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredients(@PathVariable String recipeId,@PathVariable String ingredientId
            ,Model model ){
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(Long.parseLong(recipeId),Long.parseLong(ingredientId)));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        System.out.println(unitOfMeasureService.listAllUoms());
        return "recipe/ingredient/ingredientform";
    }
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId){
        ingredientService.deleteIngredientCommandById(Long.valueOf(recipeId),Long.valueOf(ingredientId));
        return "redirect:/recipe/"+recipeId+"/ingredients";
    }

    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand ingredientCommand){
        System.out.println("ingredient commadn uom details: id: "+ingredientCommand.getUnitOfMeasure().getId()+" description: "+ingredientCommand.getUnitOfMeasure().getDescription());
        IngredientCommand savedCommand=ingredientService.saveIngredientCommand(ingredientCommand);
        log.debug("saved recipe id: "+savedCommand.getRecipeId());
        log.debug("saved ingredient id: "+savedCommand.getId());
        return "redirect:/recipe/"+savedCommand.getRecipeId()+"/ingredient/"+savedCommand.getId()+"/show";
    }
}
