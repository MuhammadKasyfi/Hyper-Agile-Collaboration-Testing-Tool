package demo_ver.demo.service;

import java.net.http.HttpClient;
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
            add(new TestCase("", (long) 1, "15", "Package", "desc23", "2023-11-07",
                    "2023-11-17", "Pending", Arrays.asList(2002, 2001)));
            add(new TestCase("", (long) 2, "17", "Behavioral", "desc34", "2023-12-05",
                    "2023-11-15", "Pending", Arrays.asList(2002, 2001)));
            add(new TestCase("", (long) 3, "19", "Diagram", "desc56", "2023-12-20",
                    "2024-01-07", "Pending", Arrays.asList(2002, 2003, 2001)));
            add(new TestCase("", (long) 4, "34", "Add Role", "Not able to add role", "2023-11-20",
                    "2024-02-04", "Pending", Arrays.asList(2002, 2004)));
        }        
    };

    

    @Autowired
    private MailService mailService;

    private static RestTemplate restTemplate = new RestTemplate();

    public ViewCaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    // private static final String HYPERLEDGER_BASE_URL = "http://172.20.228.232:3000"; // Use ngrok link here instead
    private static final String HYPERLEDGER_BASE_URL = "https://3028-149-88-106-46.ngrok-free.app"; // Use ngrok link here instead

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
        testCase.setIdtest_cases(RandomNumber.getRandom(1, 50));
        testCase.setUserID(userID);
        testCase.setOverallStatus("Pending");
        testList.add(testCase);
        // incorrect method
        setUserStatusForTestCase(testCase.getIdtest_cases(), testerUsername, "Approved");

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
            // No need to call deleteCase; just update the object directly in the list
            // updateCase(existingTestCase); // This method might be redundant
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
                String url = HYPERLEDGER_BASE_URL + "/createTestCase"; // Replace with your API URL
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