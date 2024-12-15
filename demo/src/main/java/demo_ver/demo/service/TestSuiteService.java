// package demo_ver.demo.service;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.concurrent.Executors;
// import java.util.concurrent.ScheduledExecutorService;
// import java.util.concurrent.TimeUnit;
// import java.util.NoSuchElementException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import demo_ver.demo.model.ManageUser;
// import demo_ver.demo.model.TestSuite;
// import demo_ver.demo.mail.MailService;
// import demo_ver.demo.utils.RandomNumber;

// @Service
// public class TestSuiteService {

//     private static List<TestSuite> suiteList = new ArrayList<>();

//     @Autowired
//     private MailService mailService;

//     // Retrieve all test suites
//     public List<TestSuite> findAllSuites() {
//         return suiteList;
//     }

//     // Add a new test suite
//     public void addTestSuite(TestSuite testSuite, List<Integer> userID) {
//         testSuite.setId(RandomNumber.getRandom(100, 999));
//         testSuite.setUserID(userID);
//         suiteList.add(testSuite);

//         sendAssignmentNotification(testSuite);
//         scheduleDeadlineNotification(testSuite);
//     }

//     private void sendAssignmentNotification(TestSuite testSuite) {
//         List<Integer> assignedUserIDs = testSuite.getUserID();
//         for (Integer userID : assignedUserIDs) {
//             ManageUser user = ManageUserService.getUserById(userID);
//             if (user != null && user.getEmail() != null) {
//                 String userEmail = user.getEmail();
//                 String subject = "New Test Suite Assignment";
//                 String message = "Dear user, you have been assigned a new test suite. Details:\n" +
//                         "Test Suite ID: " + testSuite.getId() + "\n" +
//                         "Test Suite Name: " + testSuite.getSuiteName() + "\n" +
//                         "Deadline: " + testSuite.getDeadline() + "\n" +
//                         "Please review and approve the test suite before the deadline.";
//                 mailService.sendAssignedMail(userEmail, subject, message);
//             }
//         }
//     }

//     private void scheduleDeadlineNotification(TestSuite testSuite) {
//         LocalDateTime deadlineDateTime = LocalDate.parse(testSuite.getDeadline()).atStartOfDay();
//         long initialDelay = ChronoUnit.SECONDS.between(LocalDateTime.now(), deadlineDateTime);

//         ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//         executorService.scheduleWithFixedDelay(() -> sendDeadlineNotification(testSuite), initialDelay, 24 * 60 * 60,
//                 TimeUnit.SECONDS);
//         executorService.schedule(() -> executorService.shutdown(), initialDelay + 24 * 60 * 60, TimeUnit.SECONDS);
//     }

//     private void sendDeadlineNotification(TestSuite testSuite) {
//         List<Integer> assignedUserIDs = testSuite.getUserID();
//         for (Integer userID : assignedUserIDs) {
//             ManageUser user = ManageUserService.getUserById(userID);
//             if (user != null && user.getEmail() != null) {
//                 String userEmail = user.getEmail();
//                 String subject = "Test Suite Deadline Notification";
//                 String message = "Dear user, the deadline for the assigned test suite has been reached. Details:\n" +
//                         "Test Suite ID: " + testSuite.getId() + "\n" +
//                         "Test Suite Name: " + testSuite.getSuiteName() + "\n" +
//                         "Deadline: " + testSuite.getDeadline() + "\n" +
//                         "Please ensure that the test suite is completed.";
//                 mailService.sendAssignedMail(userEmail, subject, message);
//             }
//         }
//     }

//     // Set user-specific status for a test suite
//     public void setUserStatusForTestSuite(Long suiteId, String username, String status) {
//         Optional<TestSuite> suiteOptional = findById(suiteId);
//         if (suiteOptional.isPresent()) {
//             TestSuite suite = suiteOptional.get();
//             suite.setUserStatus(username, status);
//             String overallStatus = suite.determineOverallStatus();
//             suite.setOverallStatus(overallStatus);
//             updateSuite(suite, null);
//         } else {
//             throw new NoSuchElementException("Test suite not found with ID: " + suiteId);
//         }
//     }

//     private Optional<TestSuite> findById(long suiteId) {
//         return suiteList.stream()
//                 .filter(s -> s.getId() == suiteId)
//                 .findFirst();
//     }

//     public void updateSuite(TestSuite updatedSuite, List<Integer> userID) {
//         Optional<TestSuite> existingSuiteOpt = findById(updatedSuite.getId());
//         if (existingSuiteOpt.isPresent()) {
//             TestSuite existingSuite = existingSuiteOpt.get();
//             existingSuite.setSuiteName(updatedSuite.getSuiteName());
//             existingSuite.setDescription(updatedSuite.getDescription());
//             existingSuite.setDateCreated(updatedSuite.getDateCreated());
//             existingSuite.setDeadline(updatedSuite.getDeadline());
//             existingSuite.setUserID(userID);

//             String overallStatus = existingSuite.determineOverallStatus();
//             existingSuite.setOverallStatus(overallStatus);
//         } else {
//             throw new NoSuchElementException("Test suite not found with ID: " + updatedSuite.getId());
//         }
//     }

//     public void deleteSuite(long suiteId) {
//         suiteList.removeIf(s -> s.getId() == suiteId);
//     }

//     // Check if a test suite name exists
//     public boolean isTestSuiteExists(String suiteName) {
//         return suiteList.stream().anyMatch(suite -> suite.getSuiteName().equalsIgnoreCase(suiteName));
//     }

//     // Retrieve a specific test suite by ID
//     public TestSuite getTestSuiteById(Long suiteId) {
//         return suiteList.stream()
//                 .filter(suite -> suite.getId().equals(suiteId))
//                 .findFirst()
//                 .orElseThrow(() -> new NoSuchElementException("Test suite not found with ID: " + suiteId));
//     }

//     // Find test suites assigned to a specific username
//     public List<TestSuite> findTestSuitesByUsername(String username) {
//         List<TestSuite> userTestSuites = new ArrayList<>();

//         for (TestSuite suite : suiteList) {
//             if (suite.getUsernames().contains(username)) {
//                 userTestSuites.add(suite);
//             }
//         }

//         return userTestSuites;
//     }
// }


package demo_ver.demo.service;

import org.springframework.stereotype.Service;
import demo_ver.demo.model.TestSuite;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class TestSuiteService {

    private List<TestSuite> testSuites = new ArrayList<>();

    // Create a test suite
    public TestSuite createTestSuite(String name, String description) {
        Long id = (long) (testSuites.size() + 1); // Simple ID generation, consider alternatives for production
        TestSuite testSuite = new TestSuite(id, name, description);
        testSuites.add(testSuite);
        return testSuite;
    }

    // View all test suites
    public List<TestSuite> viewTestSuites() {
        return testSuites;
    }

   // Update a test suite by ID
   public TestSuite updateTestSuite(Long id, String name, String description, String status, String importance, String testCases) {
    Optional<TestSuite> testSuiteOptional = testSuites.stream()
            .filter(suite -> suite.getId().equals(id))
            .findFirst();

    if (testSuiteOptional.isPresent()) {
        TestSuite testSuite = testSuiteOptional.get();
        testSuite.setName(name);
        testSuite.setDescription(description);
        testSuite.setStatus(status);
        testSuite.setImportance(importance);
        //testSuite.setTestCases(testCases); // Assuming testCases is stored as a String
        return testSuite;
    } else {
        throw new NoSuchElementException("Test suite not found with ID: " + id);
    }
}

    // Delete a test suite by ID
    public boolean deleteTestSuite(Long id) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            testSuites.remove(testSuiteOptional.get());
            return true;
        } else {
            return false; // No test suite found with the given ID
        }
    }

    // View a test suite by ID
    public TestSuite viewTestSuiteById(Long id) {
        Optional<TestSuite> testSuite = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuite.isPresent()) {
            return testSuite.get();
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + id);
        }
    }

    // Assign users to a test suite
    public void assignUsersToTestSuite(Long testSuiteId, List<Integer> userIds) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            testSuite.setAssignedUserIds(userIds); // Assuming TestSuite has a field for assigned user IDs
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + testSuiteId);
        }
    }

    public TestSuite findById(Long testSuiteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    public void save(TestSuite testSuite) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
