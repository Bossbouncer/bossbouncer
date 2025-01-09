package com.rating.bossbouncer.utility;

public enum OrganizationApiKey {

    ORG1("Perkins Coie", "Perkins-Coie-Key"),
    ORG2("Egizell", "Egizell-Key"),//Equity Residential and equity group Investment, Chicago
    ORG3("Pnc", "Pnc-Key");

    private final String organization;
    private final String apiKey;

    OrganizationApiKey(String organization, String apiKey) {
        this.organization = organization;
        this.apiKey = apiKey;
    }

    public String getOrganization() {
        return organization;
    }

    public String getApiKey() {
        return apiKey;
    }

    // This method checks if the given organization and API key are valid
    public static boolean isValidApiKey(String organization, String apiKey) {
        for (OrganizationApiKey value : values()) {
            if (value.getOrganization().equalsIgnoreCase(organization) && value.getApiKey().equals(apiKey)) {
                return true;
            }
        }
        return false;
    }
}

