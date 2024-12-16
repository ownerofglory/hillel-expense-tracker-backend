package ua.ithillel.expensetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExpenseTagColor {
    CRIMSON("Crimson", "#DC143C"),
    TEAL("Teal", "#008080"),
    GOLDENROD("Goldenrod", "#DAA520"),
    SLATE_BLUE("SlateBlue", "#6A5ACD"),
    SEA_GREEN("Sea Green", "#2E8B57"),
    CORAL("Coral", "#FF7F50"),
    DARK_ORCHID("Dark Orchid", "#9932CC"),
    SANDY_BROWN("Sandy Brown", "#F4A460");

    private final String name;
    private final String hexCode;
}
