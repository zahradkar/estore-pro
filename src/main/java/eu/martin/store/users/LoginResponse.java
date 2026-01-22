package eu.martin.store.users;

import eu.martin.store.common.Jwt;

record LoginResponse(
        Jwt accessToken,
        Jwt refreshToken
) {
}
