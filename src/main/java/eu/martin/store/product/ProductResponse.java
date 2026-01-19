package eu.martin.store.product;

record ProductResponse(
        Integer id,
        String name,
        String description,
        Float sellPrice,
        Float quantity,
        MeasureUnit measureUnit
) {
}
