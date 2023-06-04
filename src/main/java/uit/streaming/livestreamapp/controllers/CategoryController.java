package uit.streaming.livestreamapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uit.streaming.livestreamapp.entity.Category;
import uit.streaming.livestreamapp.payload.request.CreateCategoryRequest;
import uit.streaming.livestreamapp.payload.response.MessageResponse;
import uit.streaming.livestreamapp.repository.CategoryRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/all")
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addNewCategory(@RequestBody CreateCategoryRequest cateRequest) {
        Category category = new Category(cateRequest.getName(), cateRequest.getDescription());
        categoryRepository.save(category);

        return ResponseEntity.ok(new MessageResponse("Successfully added new category."));
    }

}
