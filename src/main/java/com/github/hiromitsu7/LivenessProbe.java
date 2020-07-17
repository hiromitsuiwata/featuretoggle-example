package com.github.hiromitsu7;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

/**
 * Liveness probe用のエンドポイント.常にupを返す.
 */
@Liveness
@ApplicationScoped
public class LivenessProbe implements HealthCheck {

  @Override
  public HealthCheckResponse call() {
    return HealthCheckResponse.up("up");
  }
}
