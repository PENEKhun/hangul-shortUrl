package com.penek.shortUrl.config;

import java.util.HashMap;
import java.util.Map;

public enum OperatingSystem {
    WINDOWS("win"),
    MAC("mac"),
    LINUX("linux"),
    ANDROID("android"),
    IOS("like mac"),
    UNKNOWN_OS("Unknown OS");
    private final String signature;

    OperatingSystem(String signature) {
        this.signature = signature;
    }

    public static OperatingSystem of(String userAgent) {
        OperatingSystem[] OperatingSystems = OperatingSystem.values();
        for (OperatingSystem os : OperatingSystems) {
            if (userAgent.contains(os.getSignature())) {
                return os;
            }
        }
        return UNKNOWN_OS;
    }

    public static Map<String, Integer> loadPlatformSet() {
        OperatingSystem[] operatingSystems = OperatingSystem.values();
        Map<String, Integer> platformSet = new HashMap<>();

        for (OperatingSystem operatingSystem : operatingSystems) {
            platformSet.put(operatingSystem.name(), 0);
        }
        return platformSet;
    }

    public String osName() {
        return this.name();
    }

    public String getSignature() {
        return signature;
    }
}
