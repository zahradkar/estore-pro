package eu.martin.store.products;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
enum MeasureUnit {
    KILOGRAM("KG"),
    METRE("M"),
    LITER("L"),
    UNIT("KS"),
//    SECOND("SEK"),
    PACKAGE("BAL");

    private final String code;
}
