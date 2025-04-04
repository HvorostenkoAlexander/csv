package com.mm.anonymisation.config;

import com.mm.base.config.AppConfigurationConfig;
import com.mm.base.config.WebMvcConfig;
import com.mm.common.config.JacksonConfig;
import com.mm.common.config.SwaggerConfig;
import com.mm.jooq.config.JooqContextConfig;
import com.mm.metrics.config.MetricsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    AppConfigurationConfig.class,
    ControllerConfig.class,
    JacksonConfig.class,
    JooqContextConfig.class,
    MetricsConfig.class,
    SwaggerConfig.class,
    WebMvcConfig.class
})
public class KzSolvaAnonymisationConfig {

}
