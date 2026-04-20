package nbcc.resto.service;

import nbcc.auth.domain.UserResponse;
import nbcc.common.service.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements LoginService {

    public UserLoginServiceImpl() {
    }

    @Override
    public boolean isLoggedIn() {
       return getUserResponse() != null;
    }

    @Override
    public boolean isLoggedOut() {
        return !isLoggedIn();
    }

    @Override
    public String getCurrentUsername() {
        var user = getUserResponse();
        return user != null ? user.getUsername() : null;
    }

    private UserResponse getUserResponse() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof UserResponse userResponse) {
            return userResponse;
        } else {
            return null;
        }
    }
}
