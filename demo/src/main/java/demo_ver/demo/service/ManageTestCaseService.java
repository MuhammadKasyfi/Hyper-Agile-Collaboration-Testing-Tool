package demo_ver.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import demo_ver.demo.mail.MailService;
import demo_ver.demo.model.ManageUser;
import demo_ver.demo.model.TestCase;
import demo_ver.demo.repository.ManageTestCaseRepository;

@Service
public class ManageTestCaseService {

    @Autowired
    private ManageTestCaseRepository testCaseRepository;
    
    @Autowired
    private MailService mailService;

    @Autowired
    private ManageUserService manageUserService;

    private static RestTemplate restTemplate = new RestTemplate();

    public ManageTestCaseService(RestTemplate restTemplate, ManageUserService manageUserService) {
        this.restTemplate = restTemplate;
        this.manageUserService = manageUserService;
    }

    public List<TestCase> findAllList() {
        return (List<TestCase>) testCaseRepository.findAll();
    }

    // Do not add to hyperledger yet, unless overall status is approved
    public void addTestCaseForm(TestCase testCase, List<Integer> userID, String testerUsername) {
        testCase.setUserID(userID);
        testCaseRepository.save(testCase);
        sendAssignmentNotification(testCase);
        scheduleDeadlineNotification(testCase);
    }

    private void sendAssignmentNotification(TestCase testCase) {
        List<Integer> assignedUserIDs = testCase.getUserID();
        for (Integer userID : assignedUserIDs) {
            ManageUser user = retrieveUserById(userID); // pass manageuser object directly
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

    private ManageUser retrieveUserById(Integer userID) {
        // This method encapsulates the logic for retrieving a user without acting as a
        // middleman
        return manageUserService.getUserById(userID);
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
            ManageUser user = manageUserService.getUserById(userID);
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

    public void setUserStatusForTestCase(int testCaseId, String username, String status) {
        Optional<TestCase> testCaseOptional = testCaseRepository.findById(testCaseId);
        if (testCaseOptional.isPresent()) {
            TestCase testCase = testCaseOptional.get();
            testCase.setUserStatus(username, status);
            String overallStatus = testCase.determineOverallStatus();
            testCase.setOverallStatus(overallStatus);
            testCaseRepository.save(testCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    //only if reject
    public void setUserStatusForTestCase(int testCaseId, String username, String status, String rejectionReason) {
        Optional<TestCase> testCaseOptional = testCaseRepository.findById(testCaseId);
        if (testCaseOptional.isPresent()) {
            TestCase testCase = testCaseOptional.get();
            testCase.setUserStatus(username, status);
            if ("Rejected".equals(status)) {
                testCase.setUserReason(username, rejectionReason);
            }
            String overallStatus = testCase.determineOverallStatus();
            testCase.setOverallStatus(overallStatus);
            testCaseRepository.save(testCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    public void updateCaseUser(TestCase updatedTestCase, List<Integer> userID) {
        Optional<TestCase> existingTestCaseOpt = testCaseRepository.findById(updatedTestCase.getIdtest_cases());
        if (existingTestCaseOpt.isPresent()) {
            TestCase existingTestCase = existingTestCaseOpt.get();
            existingTestCase.setProjectId(updatedTestCase.getProjectId());
            existingTestCase.setSmartContractID(updatedTestCase.getSmartContractID());
            existingTestCase.setTestCaseName(updatedTestCase.getTestCaseName());
            existingTestCase.setTest_desc(updatedTestCase.getTest_desc());
            existingTestCase.setDateCreated(updatedTestCase.getDateCreated());
            existingTestCase.setDeadline(updatedTestCase.getDeadline());
            existingTestCase.setUserID(userID);
            // Here, you might also want to update the user statuses if necessary
            existingTestCase.setUserStatuses(updatedTestCase.getUserStatuses());
            existingTestCase.resetUserStatuses();

            String overallStatus = existingTestCase.determineOverallStatus(); // Recalculate overall status
            existingTestCase.setOverallStatus(overallStatus);

            testCaseRepository.save(existingTestCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + updatedTestCase.getIdtest_cases());
        }
    }

    public void updateTestCaseOverallStatus(int testCaseId) {
        Optional<TestCase> testCaseOpt = testCaseRepository.findById(testCaseId);
        if (testCaseOpt.isPresent()) {
            TestCase testCase = testCaseOpt.get();
            String overallStatus = testCase.determineOverallStatus();
            testCase.setOverallStatus(overallStatus);
            testCaseRepository.save(testCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    public void deleteTestCaseById(int testCaseId) {
        if (testCaseRepository.existsById(testCaseId)) {
            testCaseRepository.deleteById(testCaseId);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    public TestCase getTestCaseById(int id) {
        return testCaseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Test case not found with ID: " + id));
    }

    public boolean istestCaseExists(String testCaseName) {
        return testCaseRepository.existsByTestCaseName(testCaseName);
    }

    public List<TestCase> findTestCasesByUsername(String username) {
        return testCaseRepository.findByUsername(username);
    }

    // // Check if a username exists in the system
    // public boolean istestCaseExists(String testCaseName) {
    //     return testList.stream().anyMatch(Test -> Test.getTestCaseName().equalsIgnoreCase(testCaseName));
    // }

    // // In ViewCaseService class
    // public TestCase getTestCaseById(Long idtest_cases) {
    //     return testList.stream()
    //             .filter(testCase -> testCase.getIdtest_cases().equals(idtest_cases))
    //             .findFirst()
    //             .orElseThrow(() -> new NoSuchElementException("Test case not found with ID: " + idtest_cases));
    // }

    // public List<TestCase> findTestCasesByUsername(String username) {
    //     List<TestCase> userTestCases = new ArrayList<>();

    //     for (TestCase testCase : testList) {
    //         if (testCase.getUsernames().contains(username)) {
    //             userTestCases.add(testCase);
    //         }
    //     }

    //     return userTestCases;
    // }
}