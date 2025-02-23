package nl.politie;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Backend-opdracht Nick Stuivenberg",
                version = "1.0",
                description = "Een API om politiebureaus binnen een radius te vinden"
        )
)
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
}
