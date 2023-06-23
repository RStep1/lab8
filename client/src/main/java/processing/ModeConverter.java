package processing;

import java.util.function.Function;

import data.CountMode;
import data.FilterMode;
import mods.RemoveMode;

public class ModeConverter {
    public static final Function<String, RemoveMode> GET_REMOVE_MODE = (newType) -> {
        RemoveMode removeMode = null;
        for (RemoveMode mode : RemoveMode.values())
            if (mode.getName().equals(removeMode.getName()))
                removeMode = mode;
        return removeMode;
    };

    public static final Function<String, FilterMode> GET_FILTER_MODE = (newType) -> {
        FilterMode filterMode = null;
        for (FilterMode mode : FilterMode.values())
            if (mode.getName().equals(filterMode.getName()))
                filterMode = mode;
        return filterMode;
    };

    public static final Function<String, CountMode> GET_COUNT_MODE = (newType) -> {
        CountMode countMode = null;
        for (CountMode mode : CountMode.values())
            if (mode.getName().equals(countMode.getName()))
                countMode = mode;
        return countMode;
    };
}
