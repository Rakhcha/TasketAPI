package ru.rakhcheev.tasket.api.tasketapi.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rakhcheev.tasket.api.tasketapi.entity.UserEntity;
import ru.rakhcheev.tasket.api.tasketapi.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByLogin(login);
        if (user == null) throw new UsernameNotFoundException("Пользователь не найден");
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>(List.of(new SimpleGrantedAuthority(user.getAuthority().name())));
        return new User(user.getLogin(), user.getPassword(), authorities);
    }
}
