package com.marshal.repositories;

import com.marshal.domain.Category;
import org.springframework.data.repository.CrudRepository;


public interface CategoryRepository extends CrudRepository<Category,Long> {
}
