package eu.chrost.tasktracker.info;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppInfoProperties(String description) {
}
