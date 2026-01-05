package de.gematik.mdrepo.utils.token;

import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/token")
public class TokenGenResource {

    @Inject OidcClient oidcClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken() {
        Tokens tokens = oidcClient.getTokens().await().indefinitely();

        return tokens.getAccessToken();
    }
}