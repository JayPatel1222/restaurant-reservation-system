package nbcc.resto.provider;

import nbcc.auth.domain.LoginRequest;
import nbcc.auth.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public UserServiceAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials() != null ? authentication.getCredentials().toString() : "";

        var result = userService.isAuthorized(new LoginRequest(username, password));

        if (result.isError() || result.isEmpty() || !result.isSuccessful()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var userResponse = result.getValue();
        if (userResponse.isLocked()) {
            throw new LockedException("User account is locked");
        } else if (!userResponse.isEnabled()) {
            throw new DisabledException("User account is not enabled");
        }

        return new UsernamePasswordAuthenticationToken(userResponse, null, List.of());
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}