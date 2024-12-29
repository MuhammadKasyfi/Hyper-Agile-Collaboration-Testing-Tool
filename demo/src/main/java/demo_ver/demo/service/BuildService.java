package demo_ver.demo.service;

import demo_ver.demo.model.Build;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildService {

    private final List<Build> builds = new ArrayList<>();

    // Add a new build
    public void addBuild(Build build) {
        if (build != null) {
            builds.add(build);
        }
    }

    // Get all builds
    public List<Build> getAllBuilds() {
        return new ArrayList<>(builds); // Return a copy of the list to avoid external modification
    }

    // Create a new build with specific parameters
    public void createBuild(String title, String description, String releaseDate, String isActive, String isOpen) {
        if (title == null || title.isBlank() || 
            description == null || description.isBlank() || 
            releaseDate == null || releaseDate.isBlank()) {
            throw new IllegalArgumentException("Title, description, and release date are required.");
        }

        // Ensure isActive and isOpen are not null; default to "false"
        isActive = (isActive == null || isActive.isBlank()) ? "false" : isActive;
        isOpen = (isOpen == null || isOpen.isBlank()) ? "false" : isOpen;

        Build newBuild = new Build(
                generateUniqueId(),
                title,
                description,
                releaseDate,
                isActive,
                isOpen, isOpen, isOpen
        );
        builds.add(newBuild);
    }

    // View a build by ID
    public Build viewBuildById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Build ID cannot be null or empty.");
        }
        return builds.stream()
                .filter(build -> build.getBId().equals(id))
                .findFirst()
                .orElse(null); // Return null if not found
    }

    // Delete a build by ID
    public boolean deleteBuild(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Build ID cannot be null or empty.");
        }
        return builds.removeIf(build -> build.getBId().equals(id));
    }

    // Filter builds based on a search string
    public List<Build> filterBuilds(String search) {
        if (search == null || search.isBlank()) {
            return getAllBuilds(); // Return all builds if search is empty
        }

        return builds.stream()
                .filter(build -> build.getBuildTitle().toLowerCase().contains(search.toLowerCase()) ||
                        build.getBuildDescription().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Utility method to generate a unique ID
    private String generateUniqueId() {
        return "BUILD-" + (builds.size() + 1);
    }
}
