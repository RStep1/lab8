package data;

public enum  CountMode {
    BY_FUEL_TYPE("by fuel type"),
    BY_ENGINE_POWER("by engine power");
    private final String name;
    CountMode(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}