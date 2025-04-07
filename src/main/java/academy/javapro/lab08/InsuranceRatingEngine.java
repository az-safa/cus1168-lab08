package lab08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class InsuranceRatingEngine {

    // Knowledge base (facts about insurance rates)
	// TODO: Create a instance variable named knowledgeBase of type Map<String, Object> and initialize it with a new HashMap
    private Map<String, Object> knowledgeBase = new HashMap<>();

    // Rules list
    // TODO: Create a instance variable named rules of type List<Rule> and initialize it with a new ArrayList
    private List<Rule> rules = new ArrayList<>();

    // Constructor initializes the knowledge base and rules
    // TODO: Create a public constructor for InsuranceRatingEngine
    // TODO: Call the initializeKnowledgeBase method
    // TODO: Call the initializeRules method
    public InsuranceRatingEngine() {
        initializeKnowledgeBase();
        initializeRules();
    }

    private void initializeKnowledgeBase() {
        // Base rates by vehicle category
        // TODO: Add the following key baseRate.sedan with value 1000.0 to the knowledgeBase map
        // TODO: Add the following key baseRate.suv with value 1200.0 to the knowledgeBase map
        // TODO: Add the following key baseRate.luxury with value 1500.0 to the knowledgeBase map
        // TODO: Add the following key baseRate.sports with value 1800.0 to the knowledgeBase map
        knowledgeBase.put("baseRate.sedan", 1000.0);
        knowledgeBase.put("baseRate.suv", 1200.0);
        knowledgeBase.put("baseRate.luxury", 1500.0);
        knowledgeBase.put("baseRate.sports", 1800.0);

        // Age risk factors
        // TODO: Add the following key ageFactor.16-19 with value 2.0 to the knowledgeBase map
        // TODO: Add the following key ageFactor.20-24 with value 1.5 to the knowledgeBase map
        // TODO: Add the following key ageFactor.25-65 with value 1.0 to the knowledgeBase map
        // TODO: Add the following key ageFactor.66+ with value 1.3 to the knowledgeBase map
        knowledgeBase.put("ageFactor.16-19", 2.0);
        knowledgeBase.put("ageFactor.20-24", 1.5);
        knowledgeBase.put("ageFactor.25-65", 1.0);
        knowledgeBase.put("ageFactor.66+", 1.3);

        // Accident surcharges
        // TODO: Add the following key accidentSurcharge.0 with value 0.0 to the knowledgeBase map
        // TODO: Add the following key accidentSurcharge.1 with value 300.0 to the knowledgeBase map
        // TODO: Add the following key accidentSurcharge.2+ with value 600.0 to the knowledgeBase map
        knowledgeBase.put("accidentSurcharge.0", 0.0);
        knowledgeBase.put("accidentSurcharge.1", 300.0);
        knowledgeBase.put("accidentSurcharge.2+", 600.0);
    }

    private void initializeRules() {
    	// Base rate rule - determines the starting premium based on vehicle type
        // TODO: Call the rules.add method with a new Rule object where the first argument is "base rate"
        // TODO: Create a new Predicate object profile that always returns true
        // TODO: Create a new BiConsumer object with profile and premium as arguments
        // TODO: Create a new String variable vehicleCategory and assign the result of the determineVehicleCategory method with profile as argument
        // TODO: Create a new double variable baseRate and assign the value of the knowledgeBase map with the key "baseRate." + vehicleCategory
        // TODO: Call the setBaseRate method on the premium object with baseRate as argument
        rules.add(new Rule("base rate", profile -> true, (profile, premium) -> {
            String vehicleCategory = determineVehicleCategory(profile);
            double baseRate = (double) knowledgeBase.get("baseRate." + vehicleCategory);
            premium.setBaseRate(baseRate);
        }));

        // Age factor rule - adjusts premium based on driver's age
        // TODO: Call the rules.add method with a new Rule object where the first argument is "age factor"
        // TODO: Create a profile predicate that always returns true
        // TODO: Create a new BiConsumer object with profile and premium as arguments
        // TODO: Create a new int variable age and assign the result of the getAge method on the profile object
        // TODO: Create a new double variable factor
        // TODO: Create a new String variable explanation
        rules.add(new Rule("age factor", profile -> true, (profile, premium) -> {
            int age = profile.getAge();
            double factor;
            String explanation;
            // TODO: If age is less than 20 then assign the value of the knowledgeBase map with the key "ageFactor.16-19" to the factor variable and assign the value "Drivers under 20 have higher statistical risk" to the explanation variable
            if (age < 20) {
                factor = (double) knowledgeBase.get("ageFactor.16-19");
                explanation = "Drivers under 20 have higher statistical risk";
            
            // TODO: If age is less than 25 then assign the value of the knowledgeBase map with the key "ageFactor.20-24" to the factor variable and assign the value "Drivers 20-24 have moderately higher risk" to the explanation variable
            // TODO: If age is less than 66 then assign the value of the knowledgeBase map with the key "ageFactor.25-65" to the factor variable and assign the value "Standard rate for drivers 25-65" to the explanation variable
            } else if (age < 25) {
                factor = (double) knowledgeBase.get("ageFactor.20-24");
                explanation = "Drivers 20-24 have moderately higher risk";
            } else if (age < 66) {
                factor = (double) knowledgeBase.get("ageFactor.25-65");
                explanation = "Standard rate for drivers 25-65";
            
            // TODO: Otherwise, assign the value of the knowledgeBase map with the key "ageFactor.66+" to the factor variable and assign the value "Slight increase for senior drivers" to the explanation variable
            } else {
                factor = (double) knowledgeBase.get("ageFactor.66+");
                explanation = "Slight increase for senior drivers";
            }
            // TODO: Create a new double variable adjustment and assign the result of the getBaseRate method on the premium object multiplied by (factor - 1.0)
            // TODO: Call the addAdjustment method on the premium object with "Age factor", adjustment, and explanation as arguments
            double adjustment = premium.getBaseRate() * (factor - 1.0);
            premium.addAdjustment("Age factor", adjustment, explanation);
        }));

     // Accident history rule - adds surcharges for recent accidents
        // TODO: Call the rules.add method with a new Rule object where the first argument is "accident history"
        // TODO: Create a new Predicate object profile that checks if the number of accidents in the last five years is greater than 0
        // TODO: Create a new BiConsumer object with profile and premium as arguments
        // TODO: Create a new int variable accidents and assign the result of the getAccidentsInLastFiveYears method on the profile object
        // TODO: Create a new variable surcharge of type double
        // TODO: Create a new variable explanation of type String
        // TODO: Create a if statement that checks if accidents is equal to 1
        // TODO: If the condition is true, assign the value of the knowledgeBase map with the key "accidentSurcharge.1" to the surcharge variable
        // TODO: Assign the value "Surcharge for 1 accident in past 5 years" to the explanation variable
        // TODO: Otherwise, assign the value of the knowledgeBase map with the key "accidentSurcharge.2+" to the surcharge variable
        // TODO: Assign the value "Major surcharge for 2+ accidents in past 5 years" to the explanation variable
        rules.add(new Rule("accident history", profile -> profile.getAccidentsInLastFiveYears() > 0, (profile, premium) -> {
            int accidents = profile.getAccidentsInLastFiveYears();
            double surcharge;
            String explanation;
            if (accidents == 1) {
                surcharge = (double) knowledgeBase.get("accidentSurcharge.1");
                explanation = "Surcharge for 1 accident in past 5 years";
            } else {
                surcharge = (double) knowledgeBase.get("accidentSurcharge.2+");
                explanation = "Major surcharge for 2+ accidents in past 5 years";
            }
            // TODO: Call the addAdjustment method on the premium object with "Accident history", surcharge, and explanation as arguments
            premium.addAdjustment("Accident history", surcharge, explanation);
        }));
    }

    // Helper method to determine vehicle category
    private String determineVehicleCategory(DriverProfile profile) {

        // TODO: Create a new String variable make and assign the result of the getVehicleMake method on the profile object
        // TODO: Create a new String variable model and assign the result of the getVehicleModel method on the profile object
    	String make = profile.getVehicleMake();
        String model = profile.getVehicleModel();

        // Simple classification logic
        // TODO: If make is equal to "bmw", "mercedes", "lexus", or "audi", return "luxury"
        // TODO: If make is equal to "ferrari", "porsche", "mustang", or "corvette", return "sports"
        // TODO: If model is equal to "suv", "explorer", "tahoe", or "highlander", return "suv"
        // TODO: Otherwise, return "sedan"
        if (make.equalsIgnoreCase("bmw") || make.equalsIgnoreCase("mercedes") ||
            make.equalsIgnoreCase("lexus") || make.equalsIgnoreCase("audi")) {
            return "luxury";
        } else if (make.equalsIgnoreCase("ferrari") || make.equalsIgnoreCase("porsche") ||
                   make.equalsIgnoreCase("mustang") || make.equalsIgnoreCase("corvette")) {
            return "sports";
        } else if (model.equalsIgnoreCase("suv") || model.equalsIgnoreCase("explorer") ||
                   model.equalsIgnoreCase("tahoe") || model.equalsIgnoreCase("highlander")) {
            return "suv";
        } else {
            return "sedan";
        }
    }

    // Calculate premium by applying all applicable rules
    public Premium calculatePremium(DriverProfile profile) {
    	// TODO: Create a new Premium object named premium
    	Premium premium = new Premium();

    	// Apply all rules that match the profile
        // TODO: For each rule, if the rule matches the profile, apply the rule to the profile and premium
        for (Rule rule : rules) {
            if (rule.matches(profile)) {
                rule.apply(profile, premium);
            }
        }

        // TODO: Return the premium object
        return premium;
    }

    // Rule class
    static class Rule {
        // TODO: Create a private String variable named name
        // TODO: Create a private Predicate<DriverProfile> variable named condition
        // TODO: Create a private BiConsumer<DriverProfile, Premium> variable named action
        private String name;
        private Predicate<DriverProfile> condition;
        private BiConsumer<DriverProfile, Premium> action;

        // TODO: Create a public constructor for Rule with name, condition, and action as arguments and assign them to the corresponding instance variables
        public Rule(String name, Predicate<DriverProfile> condition, BiConsumer<DriverProfile, Premium> action) {
            this.name = name;
            this.condition = condition;
            this.action = action;
        }

        // TODO: Create a public method named matches that takes a DriverProfile object as an argument and returns the result of the test method on the condition object with the profile as an argument
        public boolean matches(DriverProfile profile) {
            return condition.test(profile);
        }

        // TODO: Create a public method named apply that takes a DriverProfile and Premium object as arguments and calls the accept method on the action object with the profile and premium as arguments
        public void apply(DriverProfile profile, Premium premium) {
            action.accept(profile, premium);
        }

        // TODO: Create a public method named getName that returns the name instance variable
        public String getName() {
            return name;
        }
    }
}
