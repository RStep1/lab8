package data;

public enum  CountMode {
    BY_DISTANCE_TRAVELLED("by distance travelled"),
    BY_ENGINE_POWER("by engine power");
    private final String displayName;
    CountMode(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}