package data;

public enum  FilterMode {
    LESS_THEN_FUEL_TYPE("less then fuel type"),
    LESS_THEN_ENGINE_POWER("less then engine power");
    private final String name;
    FilterMode(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
