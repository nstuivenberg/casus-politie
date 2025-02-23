

# Requirements
* Java openjdk version "1.8.0_442"
* Wildfly 26.1.3 Final

Om Swagger/OpenAPI te laten werken: Breng de volgende wijzingen aan in: `$WILDFLY_HOME/standalone/configuration/standalone.xml`

```xml
<extensions>
    <!-- Andere extensies -->
    <extension module="org.wildfly.extension.microprofile.openapi-smallrye"/>
</extensions>

```

```xml
<profile>
    <!-- Andere subsystemen -->
    
    <subsystem xmlns="urn:wildfly:microprofile-openapi-smallrye:1.0"/>

    <!-- Andere subsystemen -->
</profile>

```


Swagger is te bereiken op: http://localhost:8080/politie-app/swagger-ui/