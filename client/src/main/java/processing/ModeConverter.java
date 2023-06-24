package processing;

import java.util.function.Function;

import data.CountMode;
import data.FilterMode;
import mods.RemoveMode;

public class ModeConverter {
    public static final Function<String, RemoveMode> GET_REMOVE_MODE = (newRemoveMode) -> {
        RemoveMode removeMode = null;
        for (RemoveMode mode : RemoveMode.values())
            if (mode.getName().equals(newRemoveMode))
                removeMode = mode;
        return removeMode;
    };

    public static final Function<String, FilterMode> GET_FILTER_MODE = (newFilterMode) -> {
        FilterMode filterMode = null;
        for (FilterMode mode : FilterMode.values())
            if (mode.getName().equals(newFilterMode))
                filterMode = mode;
        return filterMode;
    };

    public static final Function<String, CountMode> GET_COUNT_MODE = (newCountMode) -> {
        CountMode countMode = null;
        for (CountMode mode : CountMode.values())
            if (mode.getDisplayName().equals(newCountMode))
                countMode = mode;
        return countMode;
    };
}
