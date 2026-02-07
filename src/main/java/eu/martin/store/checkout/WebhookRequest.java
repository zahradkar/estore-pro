package eu.martin.store.checkout;

import java.util.Map;

record WebhookRequest(Map<String, String> headers, String payload) {
}
