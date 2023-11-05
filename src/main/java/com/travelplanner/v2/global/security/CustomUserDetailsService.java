package com.travelplanner.v2.global.security;

import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) {

        Optional<User> user = userRepository.findByEmailAndProvider(email, "local");

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. 유저 이메일:  " + email);
        }

        return new CustomUserDetails(user.get());
    }
}