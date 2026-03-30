package hei.school.ingredient.service;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients(int page, int size) {
        return ingredientRepository.findAll(page, size);
    }
}
