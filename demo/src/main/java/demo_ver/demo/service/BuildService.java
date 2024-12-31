package demo_ver.demo.service;

import demo_ver.demo.model.Build;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Optional;

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
    public void createBuild(String buildTitle, String buildDescription, String buildReleaseDate, String isBuildActive,
            String isBuildOpen, String buildVersion) {
        if (buildTitle == null || buildTitle.isBlank() ||
                buildDescription == null || buildDescription.isBlank() ||
                buildReleaseDate == null || buildReleaseDate.isBlank()||
                buildVersion == null || buildVersion.isBlank()) {
            throw new IllegalArgumentException("Title, description, version and release date are required.");
        }

        isBuildActive = (isBuildActive != null && !isBuildActive.isEmpty()) ? isBuildActive : "false";
        isBuildOpen = (isBuildOpen != null && !isBuildOpen.isEmpty()) ? isBuildOpen : "false";

        Build newBuild = new Build(
                generateUniqueId(),
                buildTitle,
                buildDescription,
                buildReleaseDate,
                isBuildActive, // Store isActive as a String
                isBuildOpen, // Store isOpen as a String
                buildVersion
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

    // Filter builds based on a search string and isActive status
    public List<Build> filterBuilds(String search, String isActive) {
        return builds.stream()
                .filter(build -> (search == null || build.getBuildTitle().toLowerCase().contains(search.toLowerCase()) ||
                        build.getBuildDescription().toLowerCase().contains(search.toLowerCase())) &&
                        (isActive == null || isActive.isBlank() || build.getIsBuildActive().equalsIgnoreCase(isActive)))
                .collect(Collectors.toList());
    }

    // Utility method to generate a unique ID for each build
    private String generateUniqueId() {
        return "BUILD-" + (builds.size() + 1);
    }

    // Update an existing build by ID
    public Build updateBuild(String bId, String buildTitle, String buildDescription, String buildReleaseDate,
            String isBuildActive, String isBuildOpen, String buildVersion) {
        Optional<Build> buildOptional = builds.stream()
                .filter(build -> build.getBId().equals(bId))
                .findFirst();

        if (buildOptional.isPresent()) {
            Build build = buildOptional.get();
            build.setBuildTitle(buildTitle);
            build.setBuildDescription(buildDescription);
            build.setBuildReleaseDate(buildReleaseDate);
            build.setBuildVersion(buildVersion);
            build.setIsBuildActive(isBuildActive != null ? isBuildActive : build.getIsBuildActive());
            build.setIsBuildOpen(isBuildOpen != null ? isBuildOpen : build.getIsBuildOpen());
            return build;
        } else {
            throw new NoSuchElementException("Build not found with ID: " + bId);
        }
            }
        }

//     // Overloaded method to update using a Build object
//     public void updateBuild(Build build) {
//         updateBuild(build.getBId(), build.getBuildTitle(), build.getBuildDescription(), build.getBuildReleaseDate(),
//                 build.getIsBuildActive(), build.getIsBuildOpen(), null);
//     }
// }
