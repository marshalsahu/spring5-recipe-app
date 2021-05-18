package com.marshal.service;

import com.marshal.domain.Recipe;
import com.marshal.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService{

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Transactional
    @Override
    public void saveImageFile(Long id, MultipartFile multipartFile) {
        try{
            Recipe recipe = recipeRepository.findById(id).get();
            Byte[] byteObject = new Byte[multipartFile.getBytes().length];
            int i=0;
            for(byte b:multipartFile.getBytes()){
                byteObject[i++]=b;
            }
            recipe.setImage(byteObject);
            recipeRepository.save(recipe);
        } catch (IOException e) {
            //todo handle better
            log.debug("Error occurred",e);
            e.printStackTrace();
        }
    }
}
