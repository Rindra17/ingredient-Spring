package hei.school.ingredient.entity;

public class CreateMovement {
    private UnitType unit;
    private Double value;
    private MouvementType type;

    public CreateMovement() {
    }

    public CreateMovement(UnitType unit, Double value, MouvementType type) {
        this.unit = unit;
        this.value = value;
        this.type = type;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public MouvementType getType() {
        return type;
    }

    public void setType(MouvementType type) {
        this.type = type;
    }
}
