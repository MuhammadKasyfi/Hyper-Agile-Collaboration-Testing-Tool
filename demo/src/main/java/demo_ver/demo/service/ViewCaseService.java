package demo_ver.demo.service;

import java.net.http.HttpClient;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo_ver.demo.mail.MailService;
import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestCase;
import demo_ver.demo.utils.RandomNumber;

@Service
public class ViewCaseService {
    private static final Logger logger = LoggerFactory.getLogger(ViewCaseService.class);

    // use this list to store test cases locally, switch to mysql or any database
    private static List<TestCase> testList = new ArrayList<TestCase>() {
        {
        //     //// PTX ///// test for jenkins8
        //     add(new TestCase("", (long) 1, "PTX-1", "Successful Register Profile", 
        //         "Summary\n " +
        //         "To validate the successful register profile\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n Expected result: The correct sign-up form displayed.\n Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n Expected result: The full name is inserted.\n Execution: Manual\n " +
        //         "Step actions: 3. The client enters their email.\n An email with the correct format is inserted.\n Execution: Manual\n " +
        //         "Step actions: 4. The client enter their phone number.\n Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Step actions: 5. The client enters their password.\n Expected result: Password with more than 6 characters is inserted.\n Execution: Manual\n " + 
        //         "Step actions: 6. The client re-confirmed their password.\n Expected result: The same string of passwords is inserted.\n Execution: Manual\n " +
        //         "Step actions: 7. The client ticks the box to agree with the terms and conditions.\n Expected result: The right icon inside the box is displayed.\n Execution: Manual\n " +
        //         "Step actions: 8. The client clicks the sign-up button at the buttom of the form.\n  Expected result: The email verification page is displayed.\n Execution: Manual\n" +
        //         "Step actions: 9. The system will send a code with 6 digits via client's email.\n Expected result: The client received the 6-digit code via email.\n Execution: Automated\n " +
        //         "Step actions: 10. The client enters the code.\n The homepage of the system is displayed.\n ",
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007, 2015)));
        //     add(new TestCase("", (long) 2, "PTX-2", "Request New Verification Code", 
        //         "Summary\n " +
        //         "To validate the successful register profile.\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters their phone number.\n" +
        //         "Expected result: Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 5. The client enters their password.\n" +
        //         "Expected result: Password with more than 6 characters is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 6. The client re-confirmed their password. \n" +
        //         "Expected result: The same string of passwords is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 7. The client ticks the box to agree with the terms and conditions. \n" +
        //         "Expected result: The right icon inside the box is displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 8. The client clicks the sign-up button at the button of the screen. \n" +
        //         "Expected result: The email verification page is displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 9. The system will send a code with 6 digits via the client’s email. \n" +
        //         "Expected result: The client received the 6-digit code via email.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 10. The client clicks a button to resend a new 6-digit email verification \n" +
        //         "Expected result: The client received the new 6-digit code via email.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 11. The client enters the code.\n" +
        //         "Expected result: The homepage of the system is displayed.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 12. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n ",
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 3, "PTX-3", "Invalid Email (Register)", 
        //         "Summary\n " +
        //         "Verify error message is displayed when the client enters the wrong email format, the email is already used for signup or the email does not exist. \n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters an invalid email.\n" +
        //         "Expected result: An email with the wrong format or already used for signup or an email that does not exist is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters their phone number.\n" +
        //         "Expected result: Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 5. The client enters their password.\n" +
        //         "Expected result: Password with more than 6 characters is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 6. The client re-confirmed their password. \n" +
        //         "Expected result: The same string of passwords is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 7. The client ticks the box to agree with the terms and conditions. \n" +
        //         "Expected result: The right icon inside the box is displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 8. The client clicks the sign-up button at the button of the screen. \n" +
        //         "Expected result: The error message of “Invalid Email” is displayed.\n " +
        //         "Execution: Manual\n ",              
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 4, "PTX-4", "Invalid Phone Number", 
        //         "Summary\n " +
        //         "Verify error message is displayed when the client enters the phone number of incorrect length.\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters a phone number with less than 11 numbers.\n" +
        //         "Expected result: The error message of “Invalid Phone Number” is displayed.\n " +
        //         "Execution: Manual\n ",                
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 5, "PTX-5", "Invalid Password (Register)", 
        //         "Summary\n " +
        //         "Verify error message is displayed when the client enters the password with incorrect length.\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters their phone number.\n" +
        //         "Expected result: Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. The client enters a password with less than 6 characters.\n" +
        //         "Expected result: The error message of “Password must be more than 6 characters” is displayed.\n " +
        //         "Execution: Manual\n ",              
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 6, "PTX-6", "Invalid Reconfirm Password", 
        //         "Summary\n " +
        //         "Verify error message is displayed when the client enters the confirmation password that is different from the previous password inserted.\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters their phone number.\n" +
        //         "Expected result: Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. The client enters their password.\n" +
        //         "Expected result: Password with more than 6 characters are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. The client re-confirmed the password with a different one from previous one. \n" +
        //         "Expected result: The error message “The password does not match” is displayed.\n " +
        //         "Execution: Manual\n ",      
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 7, "PTX-7", "Not Agreed with Terms and Conditions", 
        //         "Summary\n " +
        //         "Verify error message is displayed when the client does not tick the terms and agreement of sign-up progress.\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters their phone number.\n" +
        //         "Expected result: Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. The client enters their password.\n" +
        //         "Expected result: Password with more than 6 characters are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. The client re-confirmed their password.\n" +
        //         "Expected result: The same string of passwords is inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 7. The client does not tick the box to agree with the terms and conditions.\n" +
        //         "Expected result: The error message of “Terms and conditions must be agreed upon” is displayed.\n " +
        //         "Execution: Manual\n ",      
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 10, "PTX-10", "Invalid Verification Code", 
        //         "Summary\n " +
        //         "Verify error message is displayed when the client enters an invalid verification code.\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client clicks the sign-up button.\n" +
        //         "Expected result: The correct sign-up form displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their full name.\n" +
        //         "Expected result: The full name is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 3. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 4. The client enters their phone number.\n" +
        //         "Expected result: Phone numbers with more than 10 numbers are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. The client enters their password.\n" +
        //         "Expected result: Password with more than 6 characters are inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. The client re-confirmed their password.\n" +
        //         "Expected result: The same string of passwords is inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 7. The client ticks the box to agree with the terms and conditions.\n" +
        //         "Expected result: The right icon inside the box is displayed.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 8. The client clicks the sign-up button at the button of the form.\n" +
        //         "Expected result: The email verification page is displayed.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 9. The system will send a code with 6 digits via the client’s email.\n" +
        //         "Expected result: The client received the 6-digit code via email.\n" +
        //         "Execution: Manual\n " +
        //         "Step actions: 10. The client enters the invalid code.\n" +
        //         "Expected result: The error message of “Invalid code” is displayed.\n " +
        //         "Execution: Manual\n ",     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2006, 2008, 2007)));
        //     add(new TestCase("", (long) 11, "PTX-11", "Successful Login", 
        //         "Summary\n " +
        //         "Verify that the homepage is displayed when inserting the correct login credentials\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client enters their email.\n" +
        //         "Expected result: An email with the correct format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their password.\n" +
        //         "Expected result: The correct password is inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. The client clicks the login.\n" +
        //         "Expected result: The homepage of the system is displayed.\n " +
        //         "Execution: Manual\n ",               
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2013, 2008, 2007)));
        //     add(new TestCase("", (long) 12, "PTX-12", "Invalid Email (Login)", 
        //         "Summary\n " +
        //         "Verify error message is displayed when entering an invalid email\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client enters their email.\n" +
        //         "Expected result: An email with the wrong format is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their password.\n" +
        //         "Expected result: The correct password is inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. The client clicks the login.\n" +
        //         "Expected result: The error message is displayed.\n " +
        //         "Execution: Manual\n ",               
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2013, 2008, 2007)));
        //     add(new TestCase("", (long) 13, "PTX-13", "Invalid Password (Login)", 
        //         "Summary\n " +
        //         "Verify error message is displayed when entering the wrong password\n " +
        //         "Preconditions\n " + 
        //         "The client has access to the system.\n " + 
        //         "Step actions: 1. The client enters their email.\n" +
        //         "Expected result: The correct email is inserted.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. The client enters their password.\n" +
        //         "Expected result: The wrong password is inserted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. The client clicks the login.\n" +
        //         "Expected result: The error message is displayed.\n " +
        //         "Execution: Manual\n ",               
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2013, 2008, 2007)));

        //     //// Plantfeed /////
        //     add(new TestCase("", (long) 14, "PFWA-TC-1", "Register New User", 
        //         "Summary\n " +
        //         "Ensure the registration interface is user-friendly and secure, and that all necessary user data is captured and stored correctly.\n " +
        //         "Preconditions\n " + 
        //         "The registration page is accessible, and the database server is online to receive and store user data.\n " + 
        //         "Step actions: 1. Navigate to the registration page of the application.\n" +
        //         "Expected result: The registration page loads successfully and displays all required input fields (e.g., username, password, email, etc.).\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Enter valid user details in all input fields and submit the form.\n" +
        //         "Expected result: The system validates the input and confirms that all fields are filled correctly. No error messages are shown.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Check for a confirmation message or email after submission.\n" +
        //         "Expected result: A confirmation message appears on the screen or a confirmation email is sent to the registered email address, indicating successful registration.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Attempt to log in with the newly registered credentials.\n" +
        //         "Expected result: The login is successful, and the user is directed to the user dashboard or homepage.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Verify in the database that the user details are stored correctly.\n" +
        //         "Expected result: The user's details are correctly entered into the database with appropriate encryption for sensitive data like passwords.\n " +
        //         "Execution: Manual\n ",                  
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 15, "PFWA-TC-2", "User Login and Logout", 
        //         "Summary\n " +
        //         "Confirm that the login process handles credentials correctly, sessions are managed securely, and logout functionality cleans up session data properly.\n " +
        //         "Preconditions\n " + 
        //         "The user is already registered, and the login interface is operational. The system's session management mechanism is initialized and functioning.\n " + 
        //         "Step actions: 1. Navigate to the login page and enter valid user credentials.\n" +
        //         "Expected result: The user is authenticated, and the session starts, redirecting the user to the homepage or dashboard.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Check session cookies or tokens for security.\n" +
        //         "Expected result: Session cookies or tokens are securely created and stored in the user's system.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Navigate through various secured pages requiring authentication.\n" +
        //         "Expected result: User remains logged in, and access to pages is granted without re-entering credentials.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Log out from the application using the logout option.\n" +
        //         "Expected result: The session is terminated, the user is redirected to the login page, and session cookies or tokens are cleared from the user's system.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Attempt to navigate back using the back button after logging out.\n" +
        //         "Expected result: User should not be able to access authenticated pages and should be redirected to the login page.\n " +
        //         "Execution: Manual\n ",                  
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 16, "PFWA-TC-3", "Update User Profile", 
        //         "Summary\n " +
        //         "Verify that users can update their profile details accurately and changes are reflected immediately in the database.\n " +
        //         "Preconditions\n " + 
        //         "Verify that users can update their profile details accurately and changes are reflected immediately in the database.\n " + 
        //         "Step actions: 1. Log in with valid credentials.\n" +
        //         "Expected result: User is authenticated and directed to their dashboard or profile page.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Navigate to the profile update section.\n" +
        //         "Expected result: The profile update page loads, displaying current user information in editable fields.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Change various details (e.g., phone number, address) and submit the updates.\n" +
        //         "Expected result: Input validation occurs without errors, and changes are submitted successfully.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Check for a confirmation message indicating successful profile updates.\n" +
        //         "Expected result: A confirmation message is displayed, confirming the successful update of profile details.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Refresh the page or log out and log back in to check the persistence of the updated details.\n" +
        //         "Expected result: Updated details are retained and displayed correctly.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. Verify in the database that the changes are reflected accurately.\n" +
        //         "Expected result: The database shows the new details matching those entered by the user during the profile update process.\n " +
        //         "Execution: Manual\n ",                      
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 17, "PFWA-TC-4", "Password Recovery", 
        //         "Summary\n " +
        //         "Test the robustness of the password recovery process, ensuring it is secure and user-friendly.\n " +
        //         "Preconditions\n " + 
        //         "The user has a registered email accessible for receiving recovery instructions. The password recovery system is operational.\n " + 
        //         "Step actions: 1. Navigate to the login page.\n" +
        //         "Expected result: The login page should be displayed.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Click on the \"Forgot Password\" link.\n" +
        //         "Expected result: The password recovery form should be displayed.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Enter the registered email address in the \"Email\" field.\n" +
        //         "Expected result: The email field should accept input.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Click on the \"Submit\" button.\n" +
        //         "Expected result: A confirmation message should indicate that an email has been sent.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Check the email inbox for the password recovery email.\n" +
        //         "Expected result: The password recovery email should be received.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. Click on the link provided in the email.\n" +
        //         "Expected result: The password reset page should open.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 7. Enter a new password in the \"New Password\" field.\n" +
        //         "Expected result: The new password field should accept input.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 8. Re-enter the new password in the \"Confirm Password\" field.\n" +
        //         "Expected result: The confirmation password field should accept input.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 9. Click on the \"Reset Password\" button.\n" +
        //         "Expected result: A confirmation message should indicate that the password has been reset.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 10. Log in with the new password to verify the change.\n" +
        //         "Expected result: The user should successfully log in with the new password.\n " +
        //         "Execution: Manual\n ",                           
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 18, "PFWA-TC-5", "Create and Manage Groups", 
        //         "Summary\n " +
        //         "Validate the creation of new groups, group settings adjustments, and member management functionalities.\n " +
        //         "Preconditions\n " + 
        //         "The user must be logged in with permissions to create and manage groups. The group management interface is available.\n " + 
        //         "Step actions: 1. Log in to the PlantFeed application using valid credentials.\n" +
        //         "Expected result: The user should be logged in successfully and redirected to the dashboard.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Navigate to the \"Groups\" section from the main menu.\n" +
        //         "Expected result: The \"Groups\" section should be displayed with an option to create a new group.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Click on the \"Create New Group\" button to start creating a new group.\n" +
        //         "Expected result: The \"Create New Group\" form should appear, allowing the user to enter group details.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Fill in the \"Group Name\" and \"Description\" fields with appropriate text.\n" +
        //         "Expected result: The \"Group Name\" and \"Description\" fields should accept text input, and the information should be displayed correctly.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Select the desired privacy setting (e.g., Public, Private) from the available options.\n" +
        //         "Expected result: The privacy setting should be selectable, and the selected option should be highlighted.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. Click on the \"Create Group\" button to finalize the creation.\n" +
        //         "Expected result: A confirmation message should indicate that the group has been successfully created, and the group should appear in the user's group list.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 7. Navigate to the newly created group's page and click on the \"Manage Group\" button to access management options.\n" +
        //         "Expected result: The group management page should display options to add/remove members and change group settings.\n " +
        //         "Execution: Manual\n ",                         
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 19, "PFWA-TC-6", "Post and Manage Content in Groups", 
        //         "Summary\n " +
        //         "Ensure that posting, editing, and deleting content within groups work as expected and user permissions are respected.\n " +
        //         "Preconditions\n " + 
        //         "The user is a member of at least one group with permissions to post and manage content. The content posting interface is functional.\n " + 
        //         "Step actions: 1. Log in to the PlantFeed application.\n" +
        //         "Expected result: The user should be logged in successfully.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Navigate to the \"Groups\" section and select a group.\n" +
        //         "Expected result: The group page should be displayed.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Click on the \"Create Post\" button.\n" +
        //         "Expected result: The post creation form should be displayed.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Enter the post title in the \"Title\" field.\n" +
        //         "Expected result: The title field should accept input.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Enter the content in the \"Content\" field.\n" +
        //         "Expected result: The content field should accept input.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 6. Attach an image or file using the \"Attach File\" button.\n" +
        //         "Expected result: The file should be successfully attached.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 7. Click on the \"Post\" button.\n" +
        //         "Expected result: The post should be created and displayed in the group.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 8. Click on the \"Edit\" button next to the created post.\n" +
        //         "Expected result: The post should be editable.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 9. Modify the title of the post.\n" +
        //         "Expected result: The title should be updated with the new input.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 10. Click on the \"Update\" button to save changes.\n" +
        //         "Expected result: The post should be updated and changes should be visible.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 11. Click on the \"Delete\" button next to the post to delete it.\n" +
        //         "Expected result: The post should be deleted and no longer visible.\n " +
        //         "Execution: Manual\n ",                     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 20, "PFWA-TC-7", "List and Search Products", 
        //         "Summary\n " +
        //         "Verify the accuracy and efficiency of search functions and the correctness of product listings.\n " +
        //         "Preconditions\n " + 
        //         "The product listing page is operational, and there are products already listed in the database to be searched and displayed.\n " + 
        //         "Step actions: 1. User logs into the PlantFeed web application\n" +
        //         "Expected result: User successfully logs in and gains access to their account.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. User navigates to the \"Products\" section.\n" +
        //         "Expected result: User finds and clicks on the \"Products\" section, accessing the product management interface.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. User selects the option to list a new product.\n" +
        //         "Expected result: User initiates the process of listing a new product by clicking the appropriate button.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. User fills in details such as product name, description, price, category, and uploads images.\n" +
        //         "Expected result: User completes the form with accurate details and uploads relevant images for the product.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. User submits the product listing.\n" +
        //         "Expected result: The newly listed product is now visible to other users, and the user receives a confirmation message indicating successful submission.\n " +
        //         "Execution: Manual\n ",                     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 21, "PFWA-TC-8", "Buy and Sell Products", 
        //         "Summary\n " +
        //         "Ensure the purchase and sales process is seamless, including cart management and checkout functionalities.\n " +
        //         "Preconditions\n " + 
        //         "The user is logged in, and the marketplace interface for buying and selling is operational. There are products available for purchase, and the user has a valid payment method configured\n " + 
        //         "Step actions: 1. Buyer navigates to the product listing they are interested in.\n" +
        //         "Expected result: Buyer successfully locates the desired product from the listings.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Buyer reviews product details, images, price, and seller information.\n" +
        //         "Expected result: Buyer inspects product information and seller details for transparency and trust.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Buyer adds the product to their cart.\n" +
        //         "Expected result: Buyer adds the selected product to their shopping cart with the intention to purchase.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. User fills in details such as product name, description, price, category, and uploads images.\n" +
        //         "Expected result: User completes the form with accurate details and uploads relevant images for the product.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Buyer proceeds to checkout.\n" +
        //         "Expected result: Buyer goes through the checkout process smoothly, confirming the purchase.\n " +
        //         "Execution: Manual\n ",                     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));
        //     add(new TestCase("", (long) 22, "PFWA-TC-9", "Product Reviews and Ratings", 
        //         "Summary\n " +
        //         "Test the capability for users to review and rate products, and for these ratings to influence product visibility.\n " +
        //         "Preconditions\n " + 
        //         "The user has purchased a product and is logged in to submit a review. The review system is active and ready to record and display user inputs.\n " + 
        //         "Step actions: 1. After purchasing a product, the buyer navigates to the purchased product's page.\n" +
        //         "Expected result: Buyer finds the purchased product's page easily within their account.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Buyer selects the option to leave a review.\n" +
        //         "Expected result: Buyer clicks on the option to leave a review, indicating their willingness to provide feedback.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Buyer rates the product based on their experience.\n" +
        //         "Expected result: Buyer selects a rating, expressing their satisfaction level with the product.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Buyer writes a detailed review describing their thoughts, experience, and satisfaction level.\n" +
        //         "Expected result: Buyer writes a descriptive review, sharing insights and opinions about their experience with the product.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Buyer submits the review and rating for the purchased product.\n" +
        //         "Expected result: The review and rating are successfully submitted and displayed on the product page, contributing to the overall feedback system of the platform.\n " +
        //         "Execution: Manual\n ",                     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2016, 2017, 2019)));

        //     //// APPS /////
        //     add(new TestCase("", (long) 23, "APPS-TC-14", "Valid Login", 
        //         "Summary\n " +
        //         "Verifies that users can log into the system using valid credentials.\n " +
        //         "Preconditions\n " + 
        //         "User has valid login credentials.\n " + 
        //         "Step actions: 1. Navigate to the login page by entering the URL in the browser's address bar or by clicking on the login link from the homepage.\n" +
        //         "Expected result: The login page should be displayed with the username field, password field, and login button clearly visible, ensuring users can identify where to input their credentials.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Check the page for necessary elements (username field, password field, and login button) to ensure they are present and enabled for user input.\n" +
        //         "Expected result: All necessary elements, including the username field, password field, and login button, should be present and enabled, allowing users to interact with them without any issue.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Enter a valid username and password into the respective fields, ensuring that the username and password match the credentials registered in the system.\n" +
        //         "Expected result: The entered credentials should be accepted without error, and no validation messages should appear for these fields, indicating that the input data is correctly formatted\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Click the \"Login\" button to submit the credentials and initiate the authentication process.\n" +
        //         "Expected result: The user should be redirected to the homepage, and the system should authenticate the credentials, granting access to the user's account.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 5. Verify the user information displayed on the homepage, including the user's name, role, and relevant navigation buttons.\n" +
        //         "Expected result: The homepage should display the user's name and role correctly, along with navigation buttons that are relevant to the user's permissions and roles, ensuring that the interface is personalized for the logged-in user.\n " +
        //         "Execution: Manual\n ",       
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 24, "APPS-TC-15", "Incorrect Password", 
        //         "Summary\n " +
        //         "Verifies that the system prevents login with an incorrect password\n " +
        //         "Preconditions\n " + 
        //         "User account exists with known credential\n " + 
        //         "Step actions: 1. Navigate to the login page by entering the URL in the browser's address bar or by clicking on the login link from the homepage.\n" +
        //         "Expected result: The login page should be displayed with the username field, password field, and login button clearly visible, ensuring users can identify where to input their credentials.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Enter a valid username and an incorrect password into the respective fields, ensuring that the username exists in the system while the password does not match the registered one.\n" +
        //         "Expected result: The entered credentials should be accepted without error, but since the password is incorrect, the system should not authenticate the user.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Click the \"Login\" button to submit the credentials and initiate the authentication process.\n" +
        //         "Expected result: An error message should be displayed, indicating that the password is incorrect, and the user should not be logged in or redirected to any other page.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Verify the error message content to ensure it provides clear and accurate information to the user about the incorrect password.\n" +
        //         "Expected result: The error message should clearly state that the password is incorrect and suggest that the user tries again or resets their password if they have forgotten it, providing a link or instructions for password recovery.\n " +
        //         "Execution: Manual\n ",       
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 25, "APPS-TC-16", "Non-existent Username", 
        //         "Summary\n " +
        //         "Verifies that the system prevents login with a non-existent username\n " +
        //         "Preconditions\n " + 
        //         "No preconditions\n " + 
        //         "Step actions: 1. Navigate to the login page by entering the URL in the browser's address bar or by clicking on the login link from the homepage.\n" +
        //         "Expected result: The login page should be displayed with the username field, password field, and login button clearly visible, ensuring users can identify where to input their credentials.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Enter a non-existent username and any password into the respective fields, ensuring that the username does not exist in the system's database.\n" +
        //         "Expected result: The entered credentials should be accepted without error, but since the username does not exist, the system should not authenticate the user.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Click the \"Login\" button to submit the credentials and initiate the authentication process.\n" +
        //         "Expected result: An error message should be displayed, indicating that the username does not exist, and the user should not be logged in or redirected to any other page.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Verify the error message content to ensure it provides clear and accurate information to the user about the non-existent username.\n" +
        //         "Expected result: The error message should clearly state that the username does not exist in the system, and suggest that the user verifies their username or registers a new account if they do not have one.\n " +
        //         "Execution: Manual\n ",       
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 26, "APPS-TC-17", "Login Attempt with Empty Fields", 
        //         "Summary\n " +
        //         "Verifies that the system shows an error when login is attempted with empty username or password fields.\n " +
        //         "Preconditions\n " + 
        //         "No preconditions.\n " + 
        //         "Step actions: 1. Navigate to the login page by entering the URL in the browser's address bar or by clicking on the login link from the homepage.\n" +
        //         "Expected result: The login page should be displayed with the username field, password field, and login button clearly visible, ensuring users can identify where to input their credentials.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Leave the username and password fields empty and do not enter any data into these fields.\n" +
        //         "Expected result: The fields should remain empty, and there should be no validation errors until an attempt to login is made.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Click the \"Login\" button to submit the empty credentials and initiate the authentication process.\n" +
        //         "Expected result: An error message should be displayed, indicating that the username and password fields cannot be empty, and the user should not be logged in or redirected to any other page.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Verify the error message content to ensure it provides clear and accurate information to the user about the requirement to fill in both fields.\n" +
        //         "Expected result: The error message should clearly state that both the username and password fields are required for login and suggest that the user fills in these fields before attempting to log in again.\n " +
        //         "Execution: Manual\n ",       
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 27, "APPS-TC-18", "Successful Logout", 
        //         "Summary\n " +
        //         "users can log out of the system successfull\n " +
        //         "Preconditions\n " + 
        //         "User is logged into the system.\n " + 
        //         "Step actions: 1. Click the \"Logout\" button, usually found in the top navigation bar or user menu, to initiate the logout process.\n" +
        //         "Expected result: User should be redirected to the login page, indicating that the logout process has been initiated successfully.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Verify that the session is terminated by checking that no user-specific data is displayed on the login page or any other page that can be accessed without logging in again.\n" +
        //         "Expected result: The session should be terminated, and the login page should not display any user-specific data, ensuring the user is completely logged out and no session information is retained.\n " +
        //         "Execution: Manual\n ",     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 28, "APPS-TC-19", "Session Termination Post-Logou", 
        //         "Summary\n " +
        //         "Verifies that the user session is terminated after logout.\n " +
        //         "Preconditions\n " + 
        //         "User is logged into the system\n " + 
        //         "Step actions: 1. Click the \"Logout\" button, usually found in the top navigation bar or user menu, to initiate the logout process.\n" +
        //         "Expected result: User should be redirected to the login page, indicating that the logout process has been initiated successfully.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Attempt to navigate back to a protected page using the browser’s back button to verify that the session has been terminated and access is restricted.\n" +
        //         "Expected result: The system should prompt for login, indicating that the session has been terminated and access to protected pages is restricted without re-authentication.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Attempt to access a protected page directly via URL by entering the URL of a protected page in the browser's address bar.\n" +
        //         "Expected result: The system should redirect to the login page, indicating that the session has been terminated and access to protected pages is restricted without re-authentication.\n " +
        //         "Execution: Manual\n ",     
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 29, "APPS-TC-20", "Homepage Display Post-Login", 
        //         "Summary\n " +
        //         "Checks if the homepage displays the user's name, role, and navigation buttons correctly.\n " +
        //         "Preconditions\n " + 
        //         "User is logged into the system\n " + 
        //         "Step actions: 1. Log into the system using valid credentials, ensuring that the username and password match the credentials registered in the system.\n" +
        //         "Expected result: The user should be redirected to the homepage after successful login, indicating that the authentication process was successful.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Verify the presence of the user's name displayed prominently on the homepage, usually in the top navigation bar or a welcome message.\n" +
        //         "Expected result: The homepage should display the user's name correctly, ensuring that the interface is personalized for the logged-in user.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Verify the presence of the user's role displayed prominently on the homepage, usually near the user's name or in a dedicated section for user information.\n" +
        //         "Expected result: The homepage should display the user's role correctly, ensuring that the interface provides relevant information based on the user's permissions and responsibilities.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Verify the presence of navigation buttons relevant to the user's role and permissions, ensuring that the user can access the necessary functionalities.\n" +
        //         "Expected result: The homepage should display navigation buttons relevant to the user's role, ensuring that the user can easily access the functionalities they need without encountering any access restrictions or errors.\n " +
        //         "Execution: Manual\n ",       
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        //     add(new TestCase("", (long) 30, "APPS-TC-21", "Homepage Elements Presence", 
        //         "Summary\n " +
        //         "Ensures all homepage elements (user's name, role, navigation buttons) are present\n " +
        //         "Preconditions\n " + 
        //         "User is logged into the system\n " + 
        //         "Step actions: 1. Log into the system using valid credentials for a user with a specific role (e.g., Admin, Manager, Employee), ensuring that the username and password match the credentials registered in the system.\n" +
        //         "Expected result: The user should be redirected to the homepage after successful login, indicating that the authentication process was successful.\n " +
        //         "Execution: Manual\n " + 
        //         "Step actions: 2. Verify the presence of the user's name and role displayed prominently on the homepage, ensuring that the interface is personalized for the logged-in user.\n" +
        //         "Expected result: The homepage should display the user's name and role correctly, ensuring that the interface provides relevant information based on the user's permissions and responsibilities.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 3. Verify the presence of navigation buttons relevant to the user's role, ensuring that the user can access the necessary functionalities.\n" +
        //         "Expected result: The homepage should display navigation buttons relevant to the user's role, ensuring that the user can easily access the functionalities they need without encountering any access restrictions or errors.\n " +
        //         "Execution: Manual\n " +
        //         "Step actions: 4. Repeat steps 1-3 for users with different roles, ensuring that the homepage displays the correct information and navigation buttons for each role.\n" +
        //         "Expected result: The homepage should display different navigation buttons and information correctly for each user role, ensuring that the system provides a personalized experience based on the user's role and permissions.\n " +
        //         "Execution: Manual\n ",       
        //     "2024-06-18",
        //     "2024-07-4", 
        //     "Pending", 
        //     Arrays.asList(2024, 2025, 2026)));
        }      
    };

    

    @Autowired
    private MailService mailService;

    private static RestTemplate restTemplate = new RestTemplate();

    public ViewCaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String HYPERLEDGER_BASE_URL = "http://172.20.228.232:3000"; // Use ngrok link here instead
    // private static final String HYPERLEDGER_BASE_URL = "https://3028-149-88-106-46.ngrok-free.app"; // Use ngrok link here instead

    // private static final String HYPERLEDGER_BASE_URL =
    // "http://localhost:8090/api";



    // Get all test cases from hyperledger API
    public static List<TestCase> findAllList() throws JsonProcessingException {
        // String url = HYPERLEDGER_BASE_URL + "/getAllTestCases";

        // String jsonString = restTemplate.getForObject(url, String.class);

        // // Parse the JSON data and get the list of TestCase objects
        // List<TestCase> testCaseList = parseJsonToTestCaseList(jsonString);

        // testList = testCaseList;
        // Prepare the request body (assuming field names match API)
        // try {
        //     Map<String, Object> requestBody = new HashMap<>();
        //     String idtestCasesString = testCase.getIdtest_cases().toString();
        //     requestBody.put("id", idtestCasesString);
        //     requestBody.put("pid", testCase.getProjectId());
        //     requestBody.put("tcn", testCase.getTestCaseName());
        //     requestBody.put("tcdesc", testCase.getTest_desc());
        //     requestBody.put("dtc", testCase.getDateCreated());
        //     requestBody.put("dl", testCase.getDeadline());            
        //     // int firstUserID = userID.stream().findFirst().orElse(0);
        //     // // convert userID to String
        //     // String userIDString = Integer.toString(firstUserID);
        //     // requestBody.put("uid", userIDString);
        //     requestBody.put("ostts", testCase.getOverallStatus());
        //     requestBody.put("usrn", testerUsername);
        //     // requestBody.put("crtdby", testCase.getCreatedBy());
        //     // requestBody.put("stts", testCase.getStatus());

        //     // Send POST request using RestTemplate
        //     String url = HYPERLEDGER_BASE_URL + "/createTestCase"; // Replace with your API URL
        //     String response = restTemplate.postForObject(url, requestBody, String.class);
        // } catch (RestClientResponseException e) {
        //     // Handle potential errors from the API call
        //     logger.error("Error creating test case:", e);
        //     // You can potentially handle specific error codes or exceptions here
        // }

        return testList;
    }

    // private static List<TestCase> parseJsonToTestCaseList(String jsonString) throws JsonProcessingException {

    //     ObjectMapper objectMapper = new ObjectMapper();

    //     // Handle potential JSON parsing exceptions
    //     Map<String, Object> responseMap;
    //     try {
    //         responseMap = objectMapper.readValue(jsonString, Map.class);
    //     } catch (JsonProcessingException e) {
    //         throw e; // Re-throw the exception for proper handling
    //     }

    //     // Access the nested array (assuming it's called "message")
    //     List<Map<String, Object>> testCaseMaps = (List<Map<String, Object>>) responseMap.get("message");

    //     return testCaseMaps.stream()
    //             .map(testCaseMap -> {
    //                 TestCase testCase = new TestCase();

    //                 // Set each field of the TestCase object using the map values
    //                 // testCase.setStatus((String) testCaseMap.get("status"));
    //                 String idtestCasesString = (String) testCaseMap.get("idtest_cases");
    //                 Long idtestCasesLong = Long.parseLong(idtestCasesString);
    //                 testCase.setIdtest_cases(idtestCasesLong);
    //                 // testCase.setIdtest_cases((Long) testCaseMap.get("idtest_cases"));
    //                 testCase.setProjectId((String) testCaseMap.get("projectId"));
    //                 // testCase.setSmartContractID((String) testCaseMap.get("smartContractID"));
    //                 testCase.setTestCaseName((String) testCaseMap.get("testCaseName"));
    //                 testCase.setTest_desc((String) testCaseMap.get("test_desc"));
    //                 testCase.setDateCreated((String) testCaseMap.get("dateCreated"));
    //                 testCase.setDeadline((String) testCaseMap.get("deadline"));
    //                 // testCase.setDateUpdated((String) testCaseMap.get("dateUpdated"));
    //                 testCase.setOverallStatus((String) testCaseMap.get("overallStatus"));

    //                 // Handle the userID field (assuming it's an integer)
                    
    //                 List<Integer> userIDList = new ArrayList<>();
    //                 Object userIDObject = testCaseMap.get("userID"); // Get the value from the map

    //                 // Assuming "userID" is a string containing comma-separated IDs
    //                 if (userIDObject instanceof String) {
    //                 String userIDString = (String) userIDObject;
    //                 String[] userIDArray = userIDString.split(","); // Split by comma (delimiter)

    //                 for (String userIDStr : userIDArray) {
    //                     userIDList.add(Integer.parseInt(userIDStr));
    //                 }
    //                 } else if (userIDObject instanceof List) { // Assuming userID is already a List<Integer>
    //                 // Cast the retrieved object to List<Integer> and add elements directly
    //                 userIDList.addAll((List<Integer>) userIDObject);
    //                 } else {
    //                 // Handle unexpected data type (throw exception or log a warning)
    //                 throw new IllegalArgumentException("Unexpected type for userID: " + userIDObject.getClass());
    //                 }

    //                 // Set the userID in the TestCase object
    //                 testCase.setUserID(userIDList);

    //                 // Handle new field (if applicable)
    //                 // If userStatuses is not relevant, ignore it
    //                 Map<String, String> userStatuses = (Map<String, String>) testCaseMap.get("userStatuses");
    //                 testCase.setUserStatuses(userStatuses != null ? userStatuses : Collections.emptyMap());

    //                 System.out.println("Test Case: " + testCase.toString());
    //                 return testCase;
    //             })
    //             .collect(Collectors.toList());
    // }

    public void addTestCaseForm(TestCase testCase, List<Integer> userID, String testerUsername) {
        testCase.setIdtest_cases(RandomNumber.getRandom(31, 50));
        testCase.setUserID(userID);
        testCase.setOverallStatus("Pending");
        testList.add(testCase);
        // incorrect method
        setUserStatusForTestCase(testCase.getIdtest_cases(), testerUsername, "Approved"); // IMPORTANT

        // Prepare the request body (assuming field names match API)
        try {
            Map<String, Object> requestBody = new HashMap<>();
            String idtestCasesString = testCase.getIdtest_cases().toString();
            requestBody.put("id", idtestCasesString);
            requestBody.put("pid", testCase.getProjectId());
            requestBody.put("tcn", testCase.getTestCaseName());
            requestBody.put("tcdesc", testCase.getTest_desc());
            requestBody.put("dtc", testCase.getDateCreated());
            requestBody.put("dl", testCase.getDeadline());            
            // int firstUserID = userID.stream().findFirst().orElse(0);
            // // convert userID to String
            // String userIDString = Integer.toString(firstUserID);
            // requestBody.put("uid", userIDString);
            requestBody.put("ostts", testCase.getOverallStatus());
            requestBody.put("usrn", testerUsername);
            // requestBody.put("crtdby", testCase.getCreatedBy());
            // requestBody.put("stts", testCase.getStatus());

            // Send POST request using RestTemplate
            String url = HYPERLEDGER_BASE_URL + "/createTestCase"; // Replace with your API URL
            String response = restTemplate.postForObject(url, requestBody, String.class);
        } catch (RestClientResponseException e) {
            // Handle potential errors from the API call
            logger.error("Error creating test case:", e);
            // You can potentially handle specific error codes or exceptions here
        }

        sendAssignmentNotification(testCase);
        scheduleDeadlineNotification(testCase);
    }

    private void sendAssignmentNotification(TestCase testCase) {
        List<Integer> assignedUserIDs = testCase.getUserID();
        for (Integer userID : assignedUserIDs) {
            ManageUser user = ManageUserService.getUserById(userID);
            if (user != null && user.getEmail() != null) {
                String userEmail = user.getEmail();
                String subject = "New Test Case Assignment";
                String message = "Dear user, you have been assigned a new test case. Details:\n" +
                        "Test Case ID: " + testCase.getIdtest_cases() + "\n" +
                        "Test Case Name: " + testCase.getTestCaseName() + "\n" +
                        "Deadline: " + testCase.getDeadline() + "\n" +
                        "Please review and approve the test case before the deadline.";
                mailService.sendAssignedMail(userEmail, subject, message);
            }
        }
    }

    private void scheduleDeadlineNotification(TestCase testCase) {
        LocalDateTime deadlineDateTime = LocalDate.parse(testCase.getDeadline()).atStartOfDay();
        long initialDelay = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadlineDateTime);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(() -> sendDeadlineNotification(testCase), initialDelay, 24 * 60 * 60,
                TimeUnit.SECONDS);
        executorService.schedule(() -> executorService.shutdown(), initialDelay + 24 * 60 * 60, TimeUnit.SECONDS);
    }

    private void sendDeadlineNotification(TestCase testCase) {
        List<Integer> assignedUserIDs = testCase.getUserID();
        for (Integer userID : assignedUserIDs) {
            ManageUser user = ManageUserService.getUserById(userID);
            if (user != null && user.getEmail() != null) {
                String userEmail = user.getEmail();
                String subject = "Test Case Deadline Notification";
                String message = "Dear user, the deadline for the assigned test case has been reached. Details:\n" +
                        "Test Case ID: " + testCase.getIdtest_cases() + "\n" +
                        "Test Case Name: " + testCase.getTestCaseName() + "\n" +
                        "Deadline: " + testCase.getDeadline() + "\n" +
                        "Please ensure that the test case is completed.";
                mailService.sendAssignedMail(userEmail, subject, message);
            }
        }
    }

    public void setUserStatusForTestCase(Long testCaseId, String username, String status) {
        Optional<TestCase> testCaseOptional = findById(testCaseId);
        if (testCaseOptional.isPresent()) {
            TestCase testCase = testCaseOptional.get();
            testCase.setUserStatus(username, status);
            String overallStatus = testCase.determineOverallStatus(); // Determine the overall status
            // Assuming you have a method setOverallStatus in your TestCase model
            testCase.setOverallStatus(overallStatus); // Update the overall status
            updateCase(testCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    public void setUserStatusForTestCase(Long testCaseId, String username, String status, String rejectionReason) {
        Optional<TestCase> testCaseOptional = findById(testCaseId);
        if (testCaseOptional.isPresent()) {
            TestCase testCase = testCaseOptional.get();
            testCase.setUserStatus(username, status);
             // Determine the overall status
            if ("Rejected".equals(status)) {
                testCase.setUserReason(username, rejectionReason);
            }
            String overallStatus = testCase.determineOverallStatus();
            testCase.setOverallStatus(overallStatus);
            updateCase(testCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    private Optional<TestCase> findById(Long idtest_cases) {
        return testList.stream()
                .filter(t -> t.getIdtest_cases() == idtest_cases.longValue())
                .findFirst();
    }

    public void updateCaseUser(TestCase updatedTestCase, List<Integer> userID, String testerUsername) {
        Optional<TestCase> existingTestCaseOpt = findById(updatedTestCase.getIdtest_cases());
        if (existingTestCaseOpt.isPresent()) {
            TestCase existingTestCase = existingTestCaseOpt.get();
            existingTestCase.setProjectId(updatedTestCase.getProjectId());
            // existingTestCase.setSmartContractID(updatedTestCase.getSmartContractID());
            existingTestCase.setTestCaseName(updatedTestCase.getTestCaseName());
            existingTestCase.setTest_desc(updatedTestCase.getTest_desc());
            existingTestCase.setDateCreated(updatedTestCase.getDateCreated());
            existingTestCase.setDeadline(updatedTestCase.getDeadline());
            existingTestCase.setUserID(userID);
            // Here, you might also want to update the user statuses if necessary
            // existingTestCase.setUserStatuses(updatedTestCase.getUserStatuses());
            existingTestCase.resetUserStatuses();
            existingTestCase.setUserStatus(testerUsername, "Approved"); //set tester as approved

            String overallStatus = existingTestCase.determineOverallStatus(); // Recalculate overall status
            existingTestCase.setOverallStatus(overallStatus);

            try{
                // Prepare the request body (assuming field names match API)
                Map<String, Object> requestBody = new HashMap<>();
                String idtestCasesString = updatedTestCase.getIdtest_cases().toString();
                requestBody.put("id", idtestCasesString);
                requestBody.put("pid", updatedTestCase.getProjectId());
                requestBody.put("tcn", updatedTestCase.getTestCaseName());
                requestBody.put("tcdesc", updatedTestCase.getTest_desc());
                requestBody.put("dtc", updatedTestCase.getDateCreated());
                requestBody.put("dl", updatedTestCase.getDeadline());
                requestBody.put("usrn", testerUsername);
                requestBody.put("ostts", existingTestCase.getOverallStatus());

                // Send POST request using RestTemplate
                String url = HYPERLEDGER_BASE_URL + "/updateTestCase"; // Replace with your API URL
                String response = restTemplate.postForObject(url, requestBody, String.class);
            } catch (RestClientResponseException e) {
                // Handle potential errors from the API call
                logger.error("Error updating test case:", e);
                // You can potentially handle specific error codes or exceptions here
            }
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + updatedTestCase.getIdtest_cases());
        }
    }

    private void updateCase(TestCase testCase) {
        // deleteCase(testCase.getIdtest_cases());
        Long idtest_cases = testCase.getIdtest_cases();
        testList.removeIf(t -> t.getIdtest_cases() == idtest_cases);
        testList.add(testCase);
        if(testCase.getOverallStatus().equals("Approved")) {
            try{
                // Prepare the request body (assuming field names match API)
                Map<String, Object> requestBody = new HashMap<>();
                String idtestCasesString = testCase.getIdtest_cases().toString();
                requestBody.put("id", idtestCasesString);
                requestBody.put("pid", testCase.getProjectId());
                requestBody.put("tcn", testCase.getTestCaseName());
                requestBody.put("tcdesc", testCase.getTest_desc());
                requestBody.put("dtc", testCase.getDateCreated());
                requestBody.put("dl", testCase.getDeadline());
                requestBody.put("usrn", testCase.getUsername());
                requestBody.put("ostts", testCase.getOverallStatus());
    
                // Send POST request using RestTemplate
                String url = HYPERLEDGER_BASE_URL + "/updateTestCase"; // Replace with your API URL
                String response = restTemplate.postForObject(url, requestBody, String.class);
    
            } catch (RestClientResponseException e) {
                // Handle potential errors from the API call
                logger.error("Error updating test case:", e);
                // You can potentially handle specific error codes or exceptions here
            }
        } else if(testCase.getOverallStatus().equals("Rejected")) {
            try{
                // Prepare the request body (assuming field names match API)
                Map<String, Object> requestBody = new HashMap<>();
                String idtestCasesString = testCase.getIdtest_cases().toString();
                requestBody.put("id", idtestCasesString);
                requestBody.put("pid", testCase.getProjectId());
                requestBody.put("tcn", testCase.getTestCaseName());
                requestBody.put("tcdesc", testCase.getTest_desc());
                requestBody.put("dtc", testCase.getDateCreated());
                requestBody.put("dl", testCase.getDeadline());
                requestBody.put("usrn", testCase.getUsername());
                requestBody.put("ostts", testCase.getOverallStatus());
    
                // Send POST request using RestTemplate
                String url = HYPERLEDGER_BASE_URL + "/updateTestCase"; // Replace with your API URL
                String response = restTemplate.postForObject(url, requestBody, String.class);
    
            } catch (RestClientResponseException e) {
                // Handle potential errors from the API call
                logger.error("Error updating test case:", e);
                // You can potentially handle specific error codes or exceptions here
            }
        }
        
    }

    public void deleteCase(Long idtest_cases) {
        String idtestCasesString = idtest_cases.toString();
        String requestBody = "" + idtestCasesString; // Simple string concatenation
        logger.info("Request body: {}", requestBody);

        String url = HYPERLEDGER_BASE_URL + "/deleteTestCase";
        try {
            restTemplate.delete(url, requestBody);
            // Success scenario (optional)
            logger.info("Test case deleted successfully from Hyperledger Fabric");
            } catch (RestClientResponseException e) {
            // Handle potential errors from the API call
            logger.error("Error deleting test case:", e);
            // You can potentially handle specific error codes or exceptions here
        }
        // Remove the test case from the local list only if deletion on Hyperledger
        // Fabric was successful (optional)
        testList.removeIf(t -> t.getIdtest_cases() == idtest_cases);
    }
    

    // Check if a username exists in the system
    public boolean istestCaseExists(String testCaseName) {
        return testList.stream().anyMatch(Test -> Test.getTestCaseName().equalsIgnoreCase(testCaseName));
    }

    // In ViewCaseService class

    public TestCase getTestCaseById(Long idtest_cases) {
        return testList.stream()
                .filter(testCase -> testCase.getIdtest_cases().equals(idtest_cases))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Test case not found with ID: " + idtest_cases));
    }

    public List<TestCase> findTestCasesByUsername(String username) {
        List<TestCase> userTestCases = new ArrayList<>();

        for (TestCase testCase : testList) {
            if (testCase.getUsernames().contains(username)) {
                userTestCases.add(testCase);
            }
        }

        return userTestCases;
    }

    // ... other existing methods ...

}