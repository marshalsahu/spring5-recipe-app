package com.marshal.controller;

import com.marshal.commands.RecipeCommand;
import com.marshal.service.ImageService;
import com.marshal.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    RecipeService recipeService;
    @Mock
    ImageService imageService;
    ImageController imageController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(recipeService,imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                    .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    void getFormGet() throws Exception{
        //given
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        when(recipeService.getRecipesCommandById(anyLong())).thenReturn(recipeCommand);
        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService,times(1)).getRecipesCommandById(anyLong());
    }

    @Test
    public void handleImagePostTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile","testing.txt",
                "text/plain","Spring Framework Guru".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","/recipe/1/show"));

    }
    @Test
    public void renderImageFromDBTest() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        String s = "Spring Framework";
        Byte[] bytes = new Byte[s.getBytes(StandardCharsets.UTF_8).length];
        int i=0;
        for(byte b: s.getBytes()){
            bytes[i++] =b;
        }
        recipeCommand.setImage(bytes);
        when(recipeService.getRecipesCommandById(anyLong())).thenReturn(recipeCommand);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] byteResponse = response.getContentAsByteArray();
        assertEquals(s.getBytes().length,byteResponse.length);
    }

    public void testGetImageNumberFormatException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/recipe/asdf/recipeimage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

}
