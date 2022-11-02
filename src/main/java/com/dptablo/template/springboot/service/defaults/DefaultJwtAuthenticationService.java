package com.dptablo.template.springboot.service.defaults;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dptablo.template.springboot.exception.ApplicationException;
import com.dptablo.template.springboot.exception.AuthenticateFailedException;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.UserRepository;
import com.dptablo.template.springboot.security.DefaultUserDetails;
import com.dptablo.template.springboot.security.jwt.JwtTokenProcessor;
import com.dptablo.template.springboot.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultJwtAuthenticationService implements JwtAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProcessor jwtTokenProcessor;

    @Transactional
    @Override
    public String authenticate(String userId, String password) throws ApplicationException {
        var user = userRepository.findById(userId).orElseThrow(AuthenticateFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticateFailedException();
        }

        HashSet<GrantedAuthority> authoritySet = user.getUserRoleMappings().stream()
                .map(userRoleMapping -> new SimpleGrantedAuthority(userRoleMapping.getRole().getRole().toString()))
                .collect(Collectors.toCollection(HashSet::new));

        UserDetails userDetails = createUserDetails(user, authoritySet);
        return jwtTokenProcessor.generateToken(userDetails);
    }

    @Override
    public User signUp(String userId, String password) {
        User user = User.builder()
                .userId(userId)
                .password(password)
                .build();

        return userRepository.save(user);
    }

    @Override
    public boolean verifyToken(String token) throws JWTVerificationException {
        return jwtTokenProcessor.verifyToken(token).getToken()
                .equals(token);
    }

    @Transactional
    @Override
    public Optional<Authentication> getAuthentication(String token) {
        var decodedJWT = jwtTokenProcessor.verifyToken(token);

        var user = userRepository.findById(decodedJWT.getSubject()).orElseThrow(NullPointerException::new);
        Collection<GrantedAuthority> authorities =
                user.getUserRoleMappings().stream()
                        .map(userRoleMapping -> new SimpleGrantedAuthority(userRoleMapping.getRole().getRole().toString()))
                        .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("READ_USER_API"));

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUserId(),
                user.getPassword(),
                authorities
        );

        var userDetails = DefaultUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .enable(true)
                .authorities(authorities)
                .build();
        authenticationToken.setDetails(userDetails);
        return Optional.of(authenticationToken);
    }

    private UserDetails createUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        return DefaultUserDetails.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .enable(true)
                .authorities(authorities)
                .build();
    }
}
