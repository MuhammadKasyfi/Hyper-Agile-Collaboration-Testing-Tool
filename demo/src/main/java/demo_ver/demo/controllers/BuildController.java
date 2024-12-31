package demo_ver.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import demo_ver.demo.model.Build;
import demo_ver.demo.service.BuildService;

@Controller
public class BuildController {

    @Autowired
    private BuildService buildService;

    // Utility method to check if a user has a specific role
    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ROLE_" + role));
    }

    // View the list of builds
    @GetMapping("/viewBuilds")
    public String viewBuilds(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String isActive,
            Model model,
            Authentication authentication) {
        boolean isStakeholder = hasRole(authentication, "STAKEHOLDER");

        // Call service to filter builds based on search and isActive
        List<Build> builds = buildService.filterBuilds(search != null ? search.trim() : "", isActive);

        model.addAttribute("builds", builds);
        model.addAttribute("isStakeholder", isStakeholder);
        model.addAttribute("search", search);
        model.addAttribute("isActive", isActive);

        return "viewBuilds";
    }

    // Create Build - GET
    @GetMapping("/createBuild")
    public String createBuildForm(Model model) {
        model.addAttribute("build", new Build()); // Create and add a new Build object
        return "createBuild";
    }

    // Create Build - POST
    @PostMapping("/createBuild")
    public String createBuild(@Valid @ModelAttribute Build build, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "createBuild"; // Return to the form in case of validation errors
        }

        // Call the service method to create the build
        buildService.createBuild(build.getBuildTitle(), build.getBuildDescription(), build.getBuildReleaseDate(),
                build.getIsBuildActive(), build.getIsBuildOpen());
        redirectAttributes.addFlashAttribute("success", "Build created successfully.");
        return "redirect:/viewBuilds"; // Redirect to the list of builds
    }

    // Edit Build - GET
    @GetMapping("/editBuild/{id}")
    public String editBuildForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Build build = buildService.viewBuildById(id); // Ensure the ID exists
            model.addAttribute("build", build);
            return "editBuild"; // Pass the build to the view for editing
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Build not found.");
            return "redirect:/viewBuilds"; // Redirect if build not found
        }
    }

    // Edit Build - POST
    @PostMapping("/editBuild/{bId}")
    public String updateBuild(@PathVariable String bId,
            @RequestParam String buildTitle,
            @RequestParam String buildDescription,
            @RequestParam String buildReleaseDate,
            @RequestParam(required = false) String isBuildActive,
            @RequestParam(required = false) String isBuildOpen,
            @RequestParam(required = false) String version,
            RedirectAttributes redirectAttributes) {

        try {
            // Default to "Inactive"/"Closed" if null or empty
            isBuildActive = (isBuildActive != null && !isBuildActive.isEmpty()) ? isBuildActive : "Inactive";
            isBuildOpen = (isBuildOpen != null && !isBuildOpen.isEmpty()) ? isBuildOpen : "Closed";
            version = (version != null && !version.isEmpty()) ? version : "1.0"; // Ensure version is set

            // Call the service to update the build
            buildService.updateBuild(bId, buildTitle, buildDescription, buildReleaseDate,
                    isBuildActive, isBuildOpen, version);
            redirectAttributes.addFlashAttribute("success", "Build updated successfully.");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update build: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        return "redirect:/viewBuilds"; // Redirect to the list of builds
    }

    // Delete Build
    @PostMapping("/deleteBuild")
    public String deleteBuild(@RequestParam String id, RedirectAttributes redirectAttributes) {
        if (buildService.deleteBuild(id)) {
            redirectAttributes.addFlashAttribute("success", "Build deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete build.");
        }
        return "redirect:/viewBuilds";
    }

    // View Build Details
    @GetMapping("/viewBuildDetails/{id}")
    public String viewBuildDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Build build = buildService.viewBuildById(id);
            model.addAttribute("build", build);
            return "viewBuildDetails";
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Build not found.");
            return "redirect:/viewBuilds";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred.");
            return "redirect:/viewBuilds";
        }
    }
}
