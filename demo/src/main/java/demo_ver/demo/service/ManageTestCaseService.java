package demo_ver.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
import demo_ver.demo.model.TestCaseUserId;
import demo_ver.demo.model.TestCaseUserReason;
import demo_ver.demo.model.TestCaseUserStatus;
import demo_ver.demo.repository.ManageTestCaseRepository;
import demo_ver.demo.repository.TestCaseUserIdRepository;
import demo_ver.demo.repository.TestCaseUserReasonRepository;
import demo_ver.demo.repository.TestCaseUserStatusRepository;

@Service
public class ManageTestCaseService {

    @Autowired
    private ManageTestCaseRepository manageTestCaseRepository;

    @Autowired
    private TestCaseUserReasonRepository testCaseUserReasonRepository;

    @Autowired
    private TestCaseUserStatusRepository testCaseUserStatusRepository;

    @Autowired
    private TestCaseUserIdRepository testCaseUserIdRepository;

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
        return (List<TestCase>) manageTestCaseRepository.findAll();
    }

    public List<TestCase> findTestCasesByUsername(String username) {
        return manageTestCaseRepository.findByUsername(username);
    }

    public TestCase getTestCaseById(int id) {
        return manageTestCaseRepository.findById(id).orElse(null);
    }

    // Do not add to hyperledger yet, unless overall status is approved
    public void addTestCaseForm(TestCase testCase, List<Integer> userIDs, String createdBy) {
        testCase.setCreatedBy(createdBy);
        TestCase savedTestCase = manageTestCaseRepository.save(testCase);

        for (Integer userId : userIDs) {
            TestCaseUserId testCaseUserId = new TestCaseUserId(savedTestCase, userId.intValue());
            testCaseUserIdRepository.save(testCaseUserId);
        }

        // sendAssignmentNotification(testCase);
        // scheduleDeadlineNotification(testCase);
    }

    private void sendAssignmentNotification(TestCase testCase) {
        List<TestCaseUserId> assignedUserIDs = testCase.getUserIDs();
        for (TestCaseUserId userID : assignedUserIDs) {
            ManageUser user = retrieveUserById(userID.getUserId()); // pass manageuser object directly
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
        List<TestCaseUserId> assignedUserIDs = testCase.getUserIDs();
        for (TestCaseUserId userID : assignedUserIDs) {
            ManageUser user = manageUserService.getUserById(userID.getUserId());
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
        TestCase testCase = manageTestCaseRepository.findById(testCaseId).orElse(null);
        if (testCase != null) {
            TestCaseUserStatus userStatus = new TestCaseUserStatus(testCase, username, status);
            testCaseUserStatusRepository.save(userStatus);
            updateTestCaseOverallStatus(testCaseId);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }

    }

    // only if reject
    public void setUserStatusForTestCase(int testCaseId, String username, String status, String rejectionReason) {
        TestCase testCase = manageTestCaseRepository.findById(testCaseId).orElse(null);
        if (testCase != null) {
            TestCaseUserStatus userStatus = new TestCaseUserStatus(testCase, username, status);
            testCaseUserStatusRepository.save(userStatus);

            if (rejectionReason != null && !rejectionReason.isEmpty()) {
                TestCaseUserReason userReason = new TestCaseUserReason(testCase, username, rejectionReason);
                testCaseUserReasonRepository.save(userReason);
            }
            updateTestCaseOverallStatus(testCaseId);
        }
    }

    public void updateCaseUser(TestCase testCase, List<Integer> userIDs) {
        TestCase savedTestCase = manageTestCaseRepository.save(testCase);

        testCaseUserIdRepository.deleteAll(testCaseUserIdRepository.findByTestCase(savedTestCase));

        for (Integer userId : userIDs) {
            TestCaseUserId testCaseUserId = new TestCaseUserId(savedTestCase, userId);
            testCaseUserIdRepository.save(testCaseUserId);
        }
    }

    public void updateTestCaseOverallStatus(int testCaseId) {
        Optional<TestCase> testCaseOpt = manageTestCaseRepository.findById(testCaseId);
        if (testCaseOpt.isPresent()) {
            TestCase testCase = testCaseOpt.get();
            String overallStatus = testCase.determineOverallStatus();
            testCase.setOverallStatus(overallStatus);
            manageTestCaseRepository.save(testCase);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + testCaseId);
        }
    }

    public void deleteTestCaseById(int id) {
        if (manageTestCaseRepository.existsById(id)) {
            manageTestCaseRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Test case not found with ID: " + id);
        }
    }

    public boolean istestCaseExists(String testCaseName) {
        return manageTestCaseRepository.existsByTestCaseName(testCaseName);
    }

    public List<Integer> getTestCaseUserIds(TestCase testCaseToEdit) {

        // Check if testCaseToEdit.getUserIDs() returns a list of user objects
        if (testCaseToEdit.getUserIDs() == null) {
            return Collections.emptyList(); // Return an empty list if user IDs are not available
        }

        List<Integer> userIds = new ArrayList<>();
        for (int i =0; i<testCaseToEdit.getUserIDs().size();i++) {
            userIds.add(testCaseToEdit.getUserIDs().get(i).getId());
        }
        return userIds;
    }
}
