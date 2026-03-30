package hei.school.ingredient.controller;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngredientController {
    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService){
        this.ingredientService = ingredientService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<?> getIngredients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        List<Ingredient> ingredients = ingredientService.getAllIngredients(page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(ingredients);
    }
}
