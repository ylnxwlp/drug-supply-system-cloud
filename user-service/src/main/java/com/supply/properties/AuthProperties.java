package com.supply.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@Primary
@ConfigurationProperties(prefix = "drug.auth")
public class AuthProperties {

    private List<String> includePaths;

    private List<String> excludePaths;
}
