package demo_ver.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import demo_ver.demo.mail.MailService;
import demo_ver.demo.model.ManageUser;
import demo_ver.demo.repository.ManageUserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ManageUserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ManageUserRepository manageUserRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ManageRoleService manageRoleService;

    private static RestTemplate restTemplate = new RestTemplate();
    
    public ManageUserService(PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.passwordEncoder = passwordEncoder;
        ManageUserService.restTemplate = new RestTemplate();
        this.manageUserRepository = null;
        ManageRoleService manageRoleService = new ManageRoleService(null);
    }

    @Autowired
    public ManageUserService(PasswordEncoder passwordEncoder, ManageUserRepository manageUserRepository,
            MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.manageUserRepository = manageUserRepository;
        this.mailService = mailService;
    }

    // Get all users in the system
    public List<ManageUser> getAllUsers() {
        return (List<ManageUser>) manageUserRepository.findAll();
    }

    // Add a new user to the system
    public void addUser(ManageUser newUser, int roleID) {
        if (isUserUnique(newUser)) {
            String plainTextPassword = newUser.getPassword(); // Get the plain text password before encoding
            newUser.setRoleID(roleID);
            newUser.setPassword(passwordEncoder.encode(plainTextPassword));
            manageUserRepository.save(newUser);

            // send email notification to new user with plain text password
            sendNewUserNotification(newUser, plainTextPassword);
        } else {
            // Handle duplicate user logic if needed
        }
    }

    // send email to new user
    private void sendNewUserNotification(ManageUser newUser, String plainTextPassword) {
        String subject = "Welcome to the System";
        String message = "Dear " + newUser.getUsername() + ",\n\n"
                + "Welcome to our system! Your account has been successfully created.\n"
                + "Username: " + newUser.getUsername() + "\n"
                + "Password: " + plainTextPassword + "\n"
                + "Please log in and change your password.\n\n"
                + "Best regards,\nThe System Team";

        mailService.sendAssignedMail(newUser.getEmail(), subject, message);
    }

    // Check if a user with the same username or email already exists
    private boolean isUserUnique(ManageUser newUser) {
        return !manageUserRepository.existsByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());

    }

    // Delete a user by userID
    public void deleteUser(int userID) {
        manageUserRepository.deleteById(userID);
    }

    // Check if a username exists in the system
    public boolean isUsernameExists(String username) {
        return manageUserRepository.existsByUsername(username);
    }

    // Check if an email exists in the system
    public boolean isEmailExists(String email) {
        return manageUserRepository.existsByEmail(email);
    }

    // Get a user by userID
    public ManageUser getUserById(int userID) {
        return manageUserRepository.findById(userID).orElse(null);
    }

    // Check if a username exists in the system excluding the current user
    public boolean isUsernameExistsExcludingCurrentUser(String username, int userID) {
        return manageUserRepository.existsByUsernameAndUserIDNot(username, userID);
    }

    // Check if an email exists in the system excluding the current user
    public boolean isEmailExistsExcludingCurrentUser(String email, int userID) {
        return manageUserRepository.existsByEmailAndUserIDNot(email, userID);
    }

    // Update user details (including role)
    public void updateUser(ManageUser updatedUser, int roleID) {
        manageUserRepository.findById(updatedUser.getUserID()).ifPresent(user -> {
            user.setEmail(updatedUser.getEmail());
            user.setUsername(updatedUser.getUsername());
            user.setRoleID(roleID);
            manageUserRepository.save(user);
        });
    }

    // Retrieve user details for authentication
    public ManageUser getUserByUsername(String username) {
        return manageUserRepository.findByUsername(username).orElse(null);
    }

    // Load user details for authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ManageUser manageUser = getUserByUsername(username);

        if (manageUser == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        String roleName = manageRoleService.getRoleNameByIdString(manageUser.getRoleID());
        if (roleName == null) {
            throw new UsernameNotFoundException("Role not found for user with username: " + username);
        }

        List<GrantedAuthority> authorities = getAuthorities(roleName);

        // Log user details and authorities
        // System.out.println("User Details: ");
        // System.out.println("Username: " + manageUser.getUsername());
        // System.out.println("Password: " + manageUser.getPassword());
        // System.out.println("Authorities: " + authorities);

        // Update the encoded password from ManageUser
        return new User(
                manageUser.getUsername(),
                manageUser.getPassword(), // Use the updated encoded password
                authorities);
    }

    // Get authorities for a user role
    private List<GrantedAuthority> getAuthorities(String role) {
        return new ArrayList<>(Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

    // Check if a raw password matches an encoded password
    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // Update user password
    public void updateUserPassword(ManageUser user) {
        manageUserRepository.findById(user.getUserID()).ifPresent(existingUser -> {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            manageUserRepository.save(existingUser);
        });
    }

    // Find a user by reset token
    public ManageUser findUserByResetToken(String resetToken) {
        return manageUserRepository.findByResetToken(resetToken).orElse(null);
    }

    // Update reset token for a user
    public void updateResetToken(ManageUser user, String resetToken) {
        user.setResetToken(resetToken);
        manageUserRepository.save(user);
    }

    // Check if a reset token is valid (dummy implementation)
    public boolean isValidToken(String token) {
        return true;
    }

    // Generate a reset token (dummy implementation)
    public String generateResetToken(String email) {
        return UUID.randomUUID().toString();
    }

    // Check if a password is valid
    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    // Update user password with a new password
    public void updateUserPassword(ManageUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        manageUserRepository.save(user);
    }

    // Get a user by email
    public ManageUser getUserByEmail(String email) {
        return manageUserRepository.findByEmail(email).orElse(null);
    }
}