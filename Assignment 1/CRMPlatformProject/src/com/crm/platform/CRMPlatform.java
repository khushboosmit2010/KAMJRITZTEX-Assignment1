package com.crm.platform;

import java.util.*;
import java.util.stream.Collectors;

// Enum for User Roles
enum UserRole {
    USER,
    ADMIN,
    SUPER_ADMIN
}

// Interaction model to store interaction details
class Interaction {
    String customerId;
    String channel; // email, phone, chat, social media
    String content;
    Date timestamp;
    boolean isHighPriority;
    String category; // Sales, Support, Complaint, etc.
    String agentId;
    long responseTime; // In minutes
    double satisfactionScore; // Range from 1.0 to 5.0

    public Interaction(String customerId, String channel, String content, boolean isHighPriority, String category, String agentId, long responseTime, double satisfactionScore) {
        this.customerId = customerId;
        this.channel = channel;
        this.content = content;
        this.timestamp = new Date();
        this.isHighPriority = isHighPriority;
        this.category = category;
        this.agentId = agentId;
        this.responseTime = responseTime;
        this.satisfactionScore = satisfactionScore;
    }

    // Getter for isHighPriority
    public boolean getIsHighPriority() {
        return isHighPriority;
    }
    @Override
    public String toString() {
        return "Customer: " + customerId + ", Channel: " + channel + ", Content: " + content + ", Priority: " + isHighPriority + ", Category: " + category + ", Response Time: " + responseTime + " mins, Satisfaction: " + satisfactionScore;
    }
}

// Service class for managing interactions
class InteractionService {
    List<Interaction> interactions = new ArrayList<>();
    Map<String, List<Interaction>> customerJourneys = new HashMap<>();

    // Store interaction
    public void storeInteraction(Interaction interaction) {
        interactions.add(interaction);
        customerJourneys.computeIfAbsent(interaction.customerId, k -> new ArrayList<>()).add(interaction);
        System.out.println("Interaction stored for customer: " + interaction.customerId);
    }

    // Real-time categorization of interaction
    public void categorizeInteraction(Interaction interaction) {
        if (interaction.content.toLowerCase().contains("refund")) {
            interaction.category = "Support";
        } else if (interaction.content.toLowerCase().contains("complaint")) {
            interaction.category = "Complaint";
        } else {
            interaction.category = "Sales";
        }
        System.out.println("Interaction categorized as: " + interaction.category);
    }

    // Analysis for customer insights
    public void analyzeInteractions() {
        Map<String, Long> categoryCount = interactions.stream()
                .collect(Collectors.groupingBy(i -> i.category, Collectors.counting()));
        System.out.println("Interaction Analysis:");
        categoryCount.forEach((category, count) -> System.out.println(category + ": " + count + " interactions"));
    }

    // Search for customer interactions by customer ID
    public List<Interaction> searchInteractions(String customerId) {
        return interactions.stream()
                .filter(i -> i.customerId.equals(customerId))
                .collect(Collectors.toList());
    }

    // Visualize interactions
    public void visualizeInteractions() {
        System.out.println("Visualizing Interaction Data:");
        interactions.forEach(interaction -> System.out.println(interaction));
    }

    // Recommend action based on interaction data
    public String recommendAction(Interaction interaction) {
        if (interaction.category.equals("Complaint")) {
            return "Escalate to senior agent";
        } else if (interaction.satisfactionScore < 3.0) {
            return "Offer a discount or refund";
        } else {
            return "No action needed";
        }
    }

    // Automated routing based on priority
    public String routeInteraction(Interaction interaction) {
        if (interaction.isHighPriority) {
            return "Route to senior support agent";
        } else {
            return "Route to regular support agent";
        }
    }

    // Flag high-priority interactions
    public void flagHighPriorityInteractions() {
    	List<Interaction> highPriority = interactions.stream()
    	        .filter(Interaction::getIsHighPriority)
    	        .collect(Collectors.toList());

        System.out.println("High-Priority Interactions:");
        highPriority.forEach(System.out::println);
    }

    // Track customer journey
    public void trackCustomerJourney(String customerId) {
        List<Interaction> journey = customerJourneys.get(customerId);
        System.out.println("Customer Journey for " + customerId + ":");
        if (journey != null) {
            journey.forEach(interaction -> System.out.println(interaction.timestamp + " - " + interaction.channel + " - " + interaction.category));
        } else {
            System.out.println("No journey data available.");
        }
    }
}

// API class simulating endpoint interaction
class CRMPlatformAPI {
    InteractionService service = new InteractionService();

    // Store interaction via API
    public void storeInteractionAPI(String customerId, String channel, String content, boolean isHighPriority, String agentId, long responseTime, double satisfactionScore) {
        Interaction interaction = new Interaction(customerId, channel, content, isHighPriority, "", agentId, responseTime, satisfactionScore);
        service.storeInteraction(interaction);
        service.categorizeInteraction(interaction);
    }

    // Get insights via API
    public void analyzeInteractionsAPI() {
        service.analyzeInteractions();
    }

    // Search interactions via API
    public void searchInteractionAPI(String customerId) {
        List<Interaction> results = service.searchInteractions(customerId);
        System.out.println("Search Results for customer " + customerId + ":");
        results.forEach(System.out::println);
    }

    // Visualize interactions via API
    public void visualizeDataAPI() {
        service.visualizeInteractions();
    }

    // Recommend actions via API
    public void recommendActionAPI(Interaction interaction) {
        String recommendation = service.recommendAction(interaction);
        System.out.println("Recommended Action: " + recommendation);
    }

    // Flag high-priority interactions via API
    public void flagHighPriorityAPI() {
        service.flagHighPriorityInteractions();
    }

    // Track customer journey via API
    public void trackCustomerJourneyAPI(String customerId) {
        service.trackCustomerJourney(customerId);
    }
}

// Main class to run the CRM system
public class CRMPlatform {
    public static void main(String[] args) {
        CRMPlatformAPI api = new CRMPlatformAPI();

        // Simulate storing interactions
        api.storeInteractionAPI("CUST001", "email", "I need a refund for my order", true, "AGENT01", 30, 2.0);
        api.storeInteractionAPI("CUST001", "phone", "Where is my order?", false, "AGENT02", 15, 3.5);
        api.storeInteractionAPI("CUST002", "chat", "Interested in your new product", false, "AGENT03", 20, 4.5);

        // Analyze interactions
        api.analyzeInteractionsAPI();

        // Search customer interactions
        api.searchInteractionAPI("CUST001");

        // Visualize interaction data
        api.visualizeDataAPI();

        // Flag high-priority interactions
        api.flagHighPriorityAPI();

        // Track customer journey
        api.trackCustomerJourneyAPI("CUST001");
    }
}

