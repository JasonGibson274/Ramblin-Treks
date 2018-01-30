package data_svc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="data-svc.map")
public class MapGeneratorProperties {

    @NotNull
    private double separationDistance;

    public double getSeparationDistance() {
        return separationDistance;
    }

    public void setSeparationDistance(double separationDistance) {
        this.separationDistance = separationDistance;
    }
}
