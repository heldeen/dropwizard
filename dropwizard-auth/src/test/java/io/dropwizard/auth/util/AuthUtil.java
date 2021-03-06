package io.dropwizard.auth.util;

import com.google.common.base.Optional;
import io.dropwizard.auth.*;
import io.dropwizard.auth.basic.BasicCredentials;

import java.security.Principal;
import java.util.List;

public class AuthUtil {

    public static Authenticator<BasicCredentials, Principal> getBasicAuthenticator(final List<String> validUsers) {
        return new Authenticator<BasicCredentials, Principal>() {
            @Override
            public Optional<Principal> authenticate(BasicCredentials credentials) throws AuthenticationException {
                if (validUsers.contains(credentials.getUsername()) && "secret".equals(credentials.getPassword())) {
                    return Optional.<Principal>of(new PrincipalImpl(credentials.getUsername()));
                }
                if ("bad-guy".equals(credentials.getUsername())) {
                    throw new AuthenticationException("CRAP");
                }
                return Optional.absent();
            }
        };
    }

    public static Authenticator<String, Principal> getSingleUserOAuthAuthenticator(final String presented,
                                                                                   final String returned) {
        return new Authenticator<String, Principal>() {
            @Override
            public Optional<Principal> authenticate(String user) throws AuthenticationException {
                if (presented.equals(user)) {
                    return Optional.<Principal>of(new PrincipalImpl(returned));
                }
                if ("bad-guy".equals(user)) {
                    throw new AuthenticationException("CRAP");
                }
                return Optional.absent();
            }
        };
    }

    public static Authenticator<String, Principal> getMultiplyUsersOAuthAuthenticator(final List<String> validUsers) {
        return new Authenticator<String, Principal>() {
            @Override
            public Optional<Principal> authenticate(String credentials) throws AuthenticationException {
                if (validUsers.contains(credentials)) {
                    return Optional.<Principal>of(new PrincipalImpl(credentials));
                }
                if (credentials.equals("bad-guy")) {
                    throw new AuthenticationException("CRAP");
                }
                return Optional.absent();
            }
        };
    }

    public static Authorizer<Principal> getTestAuthorizer(final String validUser,
                                                          final String validRole) {
        return new Authorizer<Principal>() {
            @Override
            public boolean authorize(Principal principal, String role) {
                return principal != null
                        && validUser.equals(principal.getName())
                        && validRole.equals(role);
            }
        };
    }
}
