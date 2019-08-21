package ibm.gse.app.health;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import ibm.gse.kc.dao.DBConfiguration;

@Readiness
@ApplicationScoped
public class AppReadinessCheck implements HealthCheck {

    private boolean isReady() {
        // perform readiness checks, e.g. database connection, etc.
    	
        return DBConfiguration.instance().validateConnection();
    }
	
    @Override
    public HealthCheckResponse call() {
        boolean up = isReady();
        return HealthCheckResponse.named(this.getClass().getSimpleName()).state(up).build();
    }
    
}
