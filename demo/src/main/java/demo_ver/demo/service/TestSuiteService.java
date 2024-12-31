package demo_ver.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import demo_ver.demo.model.Build;
import demo_ver.demo.model.TestSuite;

@Service
public class TestSuiteService {

    private List<TestSuite> testSuites = new ArrayList<>();
    private List<Build> builds = new ArrayList<>(); // Simulated Build storage

    // Create a test suite
    public TestSuite createTestSuite(String name, String description) {
        String id = String.valueOf(testSuites.size() + 1); // Simple String ID generation
        TestSuite testSuite = new TestSuite(id, name, description);
        testSuites.add(testSuite);
        return testSuite;
    }

    // View all test suites
    public List<TestSuite> viewTestSuites() {
        return testSuites;
    }

    // Update a test suite by ID
    public TestSuite updateTestSuite(String id, String name, String description, String status, String importance) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(id))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            testSuite.setName(name);
            testSuite.setDescription(description);
            testSuite.setStatus(status);
            testSuite.setImportance(importance);
            return testSuite;
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + id);
        }
    }

    // Delete a test suite by ID
    public boolean deleteTestSuite(String id) {
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
    public TestSuite viewTestSuiteById(String id) {
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
    public void assignUsersToTestSuite(String testSuiteId, List<String> userIds) {
        Optional<TestSuite> testSuiteOptional = testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();

        if (testSuiteOptional.isPresent()) {
            TestSuite testSuite = testSuiteOptional.get();
            // Add the logic to assign users if required
        } else {
            throw new NoSuchElementException("Test suite not found with ID: " + testSuiteId);
        }
    }

    // // Assign builds to a test suite
    // public void assignBuildsToTestSuite(String testSuiteId, List<String> buildIds) {
    //     Optional<TestSuite> testSuiteOptional = testSuites.stream()
    //             .filter(suite -> suite.getId().equals(testSuiteId))
    //             .findFirst();

    //     if (testSuiteOptional.isPresent()) {
    //         TestSuite testSuite = testSuiteOptional.get();
    //         List<Build> assignedBuilds = new ArrayList<>();
    //         // Simulating fetching builds from in-memory storage
    //         for (String buildId : buildIds) {
    //             Build build = findBuildById(buildId); // Fetch build from the in-memory list
    //             assignedBuilds.add(build);
    //         }
    //         testSuite.setAssignedBuilds(assignedBuilds);
    //     } else {
    //         throw new NoSuchElementException("Test suite not found with ID: " + testSuiteId);
    //     }
    // }

    // // Simulated method to find Build by ID (from in-memory list)
    // private Build findBuildById(String buildId) {
    //     Optional<Build> build = builds.stream()
    //             .filter(b -> b.getId().equals(buildId))
    //             .findFirst();

    //     if (build.isPresent()) {
    //         return build.get();
    //     } else {
    //         throw new NoSuchElementException("Build not found with ID: " + buildId);
    //     }
    // }

    // Save or update a test suite (simulating persistence in-memory)
    public void save(TestSuite testSuite) {
        Optional<TestSuite> existingTestSuite = findById(testSuite.getId());
        if (existingTestSuite.isPresent()) {
            // If the test suite exists, update it
            TestSuite existing = existingTestSuite.get();
            existing.setName(testSuite.getName());
            existing.setDescription(testSuite.getDescription());
            existing.setStatus(testSuite.getStatus());
            existing.setImportance(testSuite.getImportance());
        } else {
            // Otherwise, add the new test suite
            testSuites.add(testSuite);
        }
    }

    // Get all test suites
    public List<TestSuite> getAllTestSuites() {
        return testSuites;
    }

    // Find Test Suite by ID
    public Optional<TestSuite> findById(String testSuiteId) {
        return testSuites.stream()
                .filter(suite -> suite.getId().equals(testSuiteId))
                .findFirst();
    }

    public List<TestSuite> getAssignedTestSuitesByTestPlanId(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAssignedTestSuitesByTestPlanId'");
    }

    // // Simulate creating and adding builds for demonstration
    // public void createBuild(String id, String name, String description) {
    //     Build build = new Build(id, name, description);
    //     builds.add(build);
    // }

    // // Get all builds (simulated)
    // public List<Build> getAllBuilds() {
    //     return builds;
    // }
}
