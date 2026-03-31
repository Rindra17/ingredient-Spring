package hei.school.ingredient.controller;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.entity.StockMovement;
import hei.school.ingredient.entity.StockValue;
import hei.school.ingredient.entity.UnitType;
import hei.school.ingredient.exception.BadRequestException;
import hei.school.ingredient.exception.NotFoundException;
import hei.school.ingredient.service.IngredientService;
import hei.school.ingredient.validator.IngredientValidator;
import hei.school.ingredient.validator.ParamValidator;
import hei.school.ingredient.validator.StockMovementValidator;
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
    private final StockMovementValidator stockMovementValidator;
    private final IngredientValidator ingredientValidator;

    public IngredientController(
            IngredientService ingredientService,
            ParamValidator paramValidator,
            StockMovementValidator stockMovementValidator,
            IngredientValidator ingredientValidator){
        this.ingredientService = ingredientService;
        this.paramValidator = paramValidator;
        this.stockMovementValidator = stockMovementValidator;
        this.ingredientValidator = ingredientValidator;
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
        catch (NotFoundException e) {
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

    @GetMapping("/ingredients/{id}/stockMovements")
    public ResponseEntity<?> getIngredientStockMovements(
            @PathVariable int id,
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            Ingredient ingredient = ingredientService.getIngredientById(id);
            ingredientValidator.ingredientValidator(ingredient, id);
            stockMovementValidator.stockMovementParamValidator(from, to);
            Instant timeFrom = Instant.parse(from);
            Instant timeTo = Instant.parse(to);
            List<StockMovement> stockMovements = ingredientService.getStockMovement(id, timeFrom, timeTo);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(stockMovements);
        }
        catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
        catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }
}
