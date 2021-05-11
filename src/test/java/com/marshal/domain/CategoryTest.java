package com.marshal.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {

    private Category category;

    @Before
    public void setUp(){
        category = new Category();
    }

    @Test
    public void getId() {
        Long cid=4L;
        category.setId(cid);
        assertEquals(cid,category.getId());
    }

    @Test
    public void getDescription() {
    }
}