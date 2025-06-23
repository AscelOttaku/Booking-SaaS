//package kg.attractor.bookingsaas.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findUserByLogin(username)
//                .orElseThrow(() -> new UsernameNotFoundException("user not found by email " + username));
//
//        return new MyUserDetails(
//                user.getLogin(),
//                user.getAuthority().getName(),
//                user.getPassword()
//        );
//    }
//}
