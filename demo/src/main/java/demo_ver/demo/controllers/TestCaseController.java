package demo_ver.demo.controllers;

import java.security.Principal; // Import Principal for getting logged-in user's information
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// import org.hyperledger.fabric.gateway.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestCase;
import demo_ver.demo.service.ManageTestCaseService;
import demo_ver.demo.service.ManageUserService;

@Controller
public class TestCaseController {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private ManageTestCaseService viewCaseService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ManageUserService manageUserService;

    @GetMapping("/view")
    public String viewCase(Model model, Principal principal, @AuthenticationPrincipal UserDetails userDetails) {
        List<TestCase> testCases = viewCaseService.findAllList();

        // Assuming ManageUserService.getAllUsers() returns a List<ManageUser>
        List<ManageUser> allUsers = manageUserService.getAllUsers();
        String username = principal.getName();

        // Set username for each test case
        for (TestCase testCase : testCases) {
            List<Integer> userIds = testCase.getUserID();
            List<String> usernames = userIds.stream()
                    .map(userId -> {
                        ManageUser user = manageUserService.getUserById(userId);
                        return (user != null) ? user.getUsername() : "";
                    })
                    .collect(Collectors.toList());

            // Assuming you want to concatenate usernames into a single string
            testCase.setUsername(String.join(", ", usernames));
        }

        List<TestCase> userTestCases = viewCaseService.findTestCasesByUsername(username);

        model.addAttribute("testCase", userTestCases);
        model.addAttribute("users1", allUsers);
        model.addAttribute("allTestCases", testCases);
        model.addAttribute("userTestCases",
        viewCaseService.findTestCasesByUsername(username));
        // remove edit and delete if not tester
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        boolean isTester = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_Tester"));
        model.addAttribute("isTester", isTester);
        return "viewTestCase";
    }

    @GetMapping("/add")
    public String showAddTestCaseForm(Model model, Authentication authentication,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("testCase", new TestCase());
        model.addAttribute("users", manageUserService.getAllUsersWithRoles());
        return "addTestCase";
    }

    @PostMapping("/save")
    public String addTestCaseForm(TestCase testCase, @RequestParam("userID") List<Integer> userID,@AuthenticationPrincipal UserDetails userDetails, Model model)
            throws JsonProcessingException {
        model.addAttribute("tests", viewCaseService.findAllList());
        model.addAttribute("users", manageUserService.getAllUsersWithRoles()); // I added this so that user list will always show
                                                                      // even if got validation errors

        // Check if the test case name already exists
        if (viewCaseService.istestCaseExists(testCase.getTestCaseName())) {
            model.addAttribute("testCaseNameExists", true);
            return "addTestCase";
        }
        // Check if the deadline is later than the date created
        if (!isDeadlineLaterThanDateCreated(testCase.getDateCreated(), testCase.getDeadline())) {
            model.addAttribute("deadlineInvalid", true);
            return "addTestCase";
        }

        // Proceed with adding the test case
        viewCaseService.addTestCaseForm(testCase, userID, userDetails.getUsername());
        return "redirect:/view";
    }

    // Helper method to check if deadline is later than date created
    private boolean isDeadlineLaterThanDateCreated(String dateCreated, String deadline) {
        LocalDate createdDate = LocalDate.parse(dateCreated);
        LocalDate deadlineDate = LocalDate.parse(deadline);
        return deadlineDate.isAfter(createdDate);
    }

    @GetMapping("/deleteCase/{idtest_cases}")
    public String deleteCase(@PathVariable("idtest_cases") int idtest_cases) {
        viewCaseService.deleteTestCaseById(idtest_cases);
        return "redirect:/view";
    }

    @GetMapping("/editCase/{idtest_cases}")
    public String editCase(@PathVariable("idtest_cases") int idtest_cases, Model model) {
        TestCase testCaseToEdit = viewCaseService.getTestCaseById(idtest_cases);
        model.addAttribute("testCase", testCaseToEdit);
        model.addAttribute("users", manageUserService.getAllUsersWithRoles()); // Add users for assigning to the test case
        return "EditTestCase"; // The name of the edit form template
    }

    @PostMapping("/update")
    public String editTestCaseForm(TestCase testCase, @RequestParam("userID") List<Integer> userID, Model model)
            throws JsonProcessingException {

        model.addAttribute("tests", viewCaseService.findAllList());
        model.addAttribute("users", manageUserService.getAllUsersWithRoles()); // I added this so that user list will always show
                                                                      // even if got validation errors
        if (viewCaseService.istestCaseExists(testCase.getTestCaseName())) {
        model.addAttribute("testCaseNameExists", true);
        return "EditTestCase";
        }
        // Check if the deadline is later than the date created
        if (!isDeadlineLaterThanDateCreated(testCase.getDateCreated(), testCase.getDeadline())) {
            model.addAttribute("deadlineInvalid", true);
            return "EditTestCase";
        }
        viewCaseService.updateCaseUser(testCase, userID);
        return "redirect:/view";
    }

    @PostMapping("/setUserStatus")
    public String setUserStatus(@RequestParam int testCaseId, @RequestParam String status,@RequestParam(required = false) String rejectionReason, Principal principal) {
        String username = principal.getName(); // Get logged-in username
        viewCaseService.setUserStatusForTestCase(testCaseId, username, status, rejectionReason);
        return "redirect:/view";
    }
}