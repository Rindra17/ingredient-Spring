package hei.school.ingredient.validator;

import hei.school.ingredient.entity.Ingredient;
import hei.school.ingredient.exception.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class IngredientValidator {
    public void ingredientValidator(Ingredient ingredient, int id) throws NotFoundException {
        if(ingredient == null){
            throw new NotFoundException("Ingredient id = " + id + " not found");
        }
    }
}
