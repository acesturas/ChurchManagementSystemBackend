package tim.dev.gfs.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tim.dev.gfs.dao.UserDao;
import tim.dev.gfs.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(
                    "User not found: " + username);
        }

        return new CustomUserDetails(user);
    }
}