package com.dnanh01.backend.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dnanh01.backend.config.JwtProvider;
import com.dnanh01.backend.exception.UserException;
import com.dnanh01.backend.model.Cart;
// import com.dnanh01.backend.model.Cart;
import com.dnanh01.backend.model.User;
import com.dnanh01.backend.repository.UserRepository;
import com.dnanh01.backend.request.ChangePasswordRequest;
import com.dnanh01.backend.request.EmailRequest;
import com.dnanh01.backend.request.LoginRequest;
import com.dnanh01.backend.request.ResetPasswordRequest;
import com.dnanh01.backend.response.AuthResponse;
import com.dnanh01.backend.service.CartService;
import com.dnanh01.backend.service.CustomerUserServiceImplementation;
import com.dnanh01.backend.service.EmailService;

import ch.qos.logback.core.util.Duration;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomerUserServiceImplementation customerUserService;
    private CartService cartService;
    private EmailService emailService;
    public AuthController(
            UserRepository userRepository,
            CustomerUserServiceImplementation customerUserService,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            CartService cartService,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.customerUserService = customerUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.cartService = cartService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

        String firstNameString = user.getFirstName();
        String lastNameString = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();
        String mobile = user.getMobile();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist != null) {
            throw new UserException("Email is already used with another account");
        }

        User createUser = new User();
        createUser.setFirstName(firstNameString);
        createUser.setLastName(lastNameString);
        createUser.setEmail(email);
        createUser.setPassword(passwordEncoder.encode(password));
        createUser.setMobile(mobile);
        createUser.setRole(role);
        createUser.setCreateAt(LocalDateTime.now());

        User saveUser = userRepository.save(createUser);
        Cart cart = cartService.createCart(saveUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                saveUser.getEmail(),
                saveUser.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setRole(saveUser.getRole());
        authResponse.setMessage("Signup Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    
   /* @PutMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest) throws MessagingException {
        String password = findPasswordByUsername(emailRequest.getEmail());
        emailService.sendOtpEmail(emailRequest.getEmail(),password);
        return "Gửi thành công";
    } */
    
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest)
            throws UserException {

        String userName = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        User foundUser = userRepository.findByEmail(userName);

        if (foundUser == null) {
            throw new UserException("User does not exist.");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setRole(foundUser.getRole());
        authResponse.setMessage("Sign in Success!");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }
    
    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customerUserService.loadUserByUsername(userName);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username...");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
    public String findPasswordByUsername(String userName) {
        // Lấy thông tin chi tiết của người dùng từ dịch vụ customerUserService dựa trên tên người dùng.
        UserDetails userDetails = customerUserService.loadUserByUsername(userName);

        // Nếu không tìm thấy thông tin người dùng (userDetails là null), trả về null hoặc một giá trị nào đó tùy theo yêu cầu.
        if (userDetails == null) {
            return null;
        }

        // Trả về mật khẩu của người dùng.
        return passwordEncoder.encode(userDetails.getPassword());
    }
    


    @PostMapping("/requestResetPassword")
    public ResponseEntity<String> requestResetPassword(@RequestBody EmailRequest emailRequest) throws UserException, MessagingException {
        User user = userRepository.findByEmail(emailRequest.getEmail());
        if (user == null) {
            throw new UserException("User with this email does not exist.");
        }

        // Tạo token đặt lại mật khẩu
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenCreationDate(LocalDateTime.now());
        userRepository.save(user);
        emailService.sendResetPasswordEmail(user.getEmail(), token);

        return new ResponseEntity<>("Password reset email sent.", HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws UserException {
        User user = userRepository.findByResetPasswordToken(resetPasswordRequest.getToken());
        if (user == null || isTokenExpired(user)) {
            throw new UserException("Invalid or expired password reset token.");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);

        return new ResponseEntity<>("Password has been reset.", HttpStatus.OK);
    }

    private boolean isTokenExpired(User user) {
        LocalDateTime tokenCreationDate = user.getTokenCreationDate();
        return tokenCreationDate.plusHours(24).isBefore(LocalDateTime.now());
    }
    
    
}
