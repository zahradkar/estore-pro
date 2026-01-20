package eu.martin.store.product;

record ProductBuyResponse(
        int id,
        String name,
        float newQuantity,
        float lastBuyPrice,
        String lastSupplier
) {
}
