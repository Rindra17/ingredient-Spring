package hei.school.ingredient.controller;

import hei.school.ingredient.entity.Dish;
import hei.school.ingredient.exception.NotFoundException;
import hei.school.ingredient.repository.DishRepository;
import hei.school.ingredient.service.DishService;
import hei.school.ingredient.validator.RequestBodyValidator;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DishController {
    private final DishRepository dishRepository;
    private final DishService dishService;
    private final RequestBodyValidator requestBodyValidator;

    public DishController(DishRepository dishRepository, DishService dishService, RequestBodyValidator requestBodyValidator) {
        this.dishRepository = dishRepository;
        this.dishService = dishService;
        this.requestBodyValidator = requestBodyValidator;
    }

    @GetMapping("/dishes")
    public ResponseEntity<?> findAll() {
        List<Dish> dishes = dishRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(dishes);
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(
            @PathVariable Integer id,
            @RequestBody List<Integer> ingId) {
       try {
           requestBodyValidator.requestBodyValidator(ingId);
           Dish dish = dishService.updateIngredient(id, ingId);

           return ResponseEntity
                   .status(HttpStatus.OK)
                   .header("Content-Type", "application/json")
                   .body(dish);
       }
       catch (BadRequestException e) {
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .header("Content-Type", "text/plain")
                   .body(e.getMessage());
       }
       catch (NotFoundException e) {
           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .header("Content-Type", "text/plain")
                   .body(e.getMessage());
       }
       catch (RuntimeException e) {
           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .header("Content-Type", "text/plain")
                   .body(e.getMessage());
       }
    }
}
