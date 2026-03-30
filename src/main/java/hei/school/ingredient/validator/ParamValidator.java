package hei.school.ingredient.validator;

import hei.school.ingredient.exeptions.IngredientExeption;
import org.springframework.stereotype.Component;

@Component
public class ParamValidator {
    public void paramValidator (String at, String unit) throws IngredientExeption {
        StringBuilder msg = new StringBuilder();
        int i = 0;
        if (at == null) {
            msg.append("Query parameter 'at' is required\n");
            i += 1;
        }
        if (unit == null) {
            msg.append("Query parameter 'unit' is required\n");
            i += 1;
        }
        if (!msg.isEmpty()) {
            if (i == 2) {
            throw new IngredientExeption("Either mandatory query parameter `at` or `unit` is not provided.");
        }
            else {
                throw new IngredientExeption(msg.toString());
            }
        }
    }
}
