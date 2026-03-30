package hei.school.ingredient.controller;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.entity.StockValue;
import hei.school.ingredient.entity.UnitType;
import hei.school.ingredient.exception.BadRequestException;
import hei.school.ingredient.service.IngredientService;
import hei.school.ingredient.validator.ParamValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class IngredientController {
    private final ParamValidator paramValidator;
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService, ParamValidator paramValidator){
        this.ingredientService = ingredientService;
        this.paramValidator = paramValidator;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<?> getIngredients(
    ){ List<Ingredient> ingredients = ingredientService.getAllIngredients();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(ingredients);
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable int id){
        Ingredient ingredient;
        try {
            ingredient = ingredientService.getIngredientById(id);
        }
        catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(ingredient);
    }

    @GetMapping("/ingredients/{id}/stock")
    public ResponseEntity<?> getIngredientStock(
            @PathVariable int id,
            @RequestParam(required = false) String at,
            @RequestParam(required = false) String unit){
        StockValue stockValue;
        try {
            paramValidator.paramValidator(at, unit);
            Instant time = Instant.parse(at);
            UnitType unitType = UnitType.valueOf(unit);

            stockValue = ingredientService.getStockValueAt(id, time, unitType);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(stockValue);
        }
        catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
