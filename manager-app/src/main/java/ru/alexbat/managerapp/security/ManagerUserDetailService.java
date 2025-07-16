package ru.alexbat.managerapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexbat.managerapp.entity.Authority;
import ru.alexbat.managerapp.repository.ManagerUserRepository;

@Service
@RequiredArgsConstructor
public class ManagerUserDetailService implements UserDetailsService {

    private final ManagerUserRepository managerUserRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerUserRepository.findByUsername(username)
            .map(user -> User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities().stream()
                    .map(Authority::getAuthority)
                    .map(SimpleGrantedAuthority::new)
                    .toList()
                )
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
