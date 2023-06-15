package com.penek.shortUrl.config;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox"),
    IE("trident"),
    SAFARI("safari"),
    EDGE("edge"),
    OPERA("opera"),
    WHALE("whale"),
    SAMSUNG_INTERNET("samsung"),
    UNKNOWN_BROWSER("Unknown Browser");

    private final String signature;

    Browser(String signature) {
        this.signature = signature;
    }

    public static Browser of(String userAgent) {
        Browser[] userAgents = Browser.values();
        for (Browser ua : userAgents) {
            if (userAgent.contains(ua.getSignature())) {
                return ua;
            }
        }
        return UNKNOWN_BROWSER;
    }

    public String browserName() {
        return this.name();
    }

    public String getSignature() {
        return signature;
    }
}

