package app.config;

import app.config.request.filters.TraceFilter;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

    @Bean
    TraceFilter traceFilter(Tracer tracer) {
        return new TraceFilter(tracer);
    }
}
