package eu.martin.store.checkout;


import eu.martin.store.orders.PaymentStatus;

record PaymentResult(Long orderId, PaymentStatus paymentStatus) {
}
