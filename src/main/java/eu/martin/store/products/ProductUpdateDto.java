package eu.martin.store.products;

record ProductUpdateDto(
        String name,
        String description,
        Float sellPrice,
        Float quantity,
        MeasureUnit measureUnit) {
}
