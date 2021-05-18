package com.marshal.controller;

import com.marshal.commands.RecipeCommand;
import com.marshal.service.ImageService;
import com.marshal.service.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String showImageForm(@PathVariable String recipeId, Model model){
        model.addAttribute("recipe", recipeService.getRecipesCommandById(Long.valueOf(recipeId)));
        return "recipe/imageuploadform";
    }
    @PostMapping("/recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable String recipeId, @RequestParam("imageFile")MultipartFile multipartFile){
        imageService.saveImageFile(Long.valueOf(recipeId), multipartFile);
        return "redirect:/recipe/"+recipeId+"/show";
    }

    @GetMapping("/recipe/{id}/recipeimage")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.getRecipesCommandById(Long.valueOf(id));

        if(recipeCommand.getImage()!=null) {
            byte[] bytes = new byte[recipeCommand.getImage().length];
            int i = 0;
            for (Byte b : recipeCommand.getImage()) {
                bytes[i++] = b; //auto-unboxing
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(bytes);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}
