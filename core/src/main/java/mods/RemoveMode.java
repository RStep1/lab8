package mods;

/**
 *  Used to distinguish between two commands to remove an element.
 */
public enum RemoveMode {
    GREATER_THEN_DISTANCE_TRAVELLED("greater then distance travelled", ">"),
    LOWER_THEN_DISTANCE_TRAVELLED("lower then distance travelled", "<");

    private final String name;
    private final String symbol;
    RemoveMode(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * @return Returns the mathematical symbol by which the elements are compared.
     */
    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
