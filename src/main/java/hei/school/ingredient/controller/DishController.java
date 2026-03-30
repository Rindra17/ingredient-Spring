package hei.school.ingredient.controller;

import hei.school.ingredient.entity.Dish;
import hei.school.ingredient.repository.DishRepository;
import hei.school.ingredient.repository.IngredientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DishController {
    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping("/dishes")
    public ResponseEntity<?> findAll() {
        List<Dish> dishes = dishRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(dishes);
    }
}
