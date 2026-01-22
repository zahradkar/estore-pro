package eu.martin.store.auth;

record LoginResponse(
        Jwt accessToken,
        Jwt refreshToken
) {
}
