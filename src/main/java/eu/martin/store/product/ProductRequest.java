package eu.martin.store.product;

record ProductRequest(
        String name,
        String description,
        Float sellPrice,
        Float quantity,
        MeasureUnit measureUnit) {
}
