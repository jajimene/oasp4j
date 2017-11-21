package io.oasp.module.service.common.impl.header;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import io.oasp.module.logging.common.api.LoggingConstants;
import io.oasp.module.service.common.api.config.ServiceConfig;
import io.oasp.module.service.common.api.header.ServiceHeaderContext;
import io.oasp.module.service.common.api.header.ServiceHeaderCustomizer;

/**
 * Implementation of {@link ServiceHeaderCustomizer} that passes the {@link LoggingConstants#CORRELATION_ID} to a
 * subsequent {@link io.oasp.module.service.common.api.Service} invocation.
 *
 * @since 3.0.0
 */
public class ServiceHeaderCustomizerOAuth implements ServiceHeaderCustomizer {

  /**
   * The constructor.
   */
  public ServiceHeaderCustomizerOAuth() {

    super();
  }

  @Override
  public void addHeaders(ServiceHeaderContext<?> context) {

    String auth = context.getConfig().getChildValue(ServiceConfig.KEY_SEGMENT_AUTH);
    if (!"oauth".equals(auth)) {
      return;
    }
    SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext == null) {
      return;
    }
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null) {
      return;
    }
    Object details = authentication.getDetails();
    Object oauthToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZW1vIiwiZXhwIjoxNTExMjc5Mjg2LCJyb2xlcyI6WyJERU1PIl19.Wg_eqo_T5nTOzwqKqdFfHvQeUud84-0OtPod7Rl77bxIUupQ3YKwNsX42Zfhh9V_hTdIAccrS5WR1rqu1EfaHg";
    if (details instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) details;
      oauthToken = map.get("oauth.token");

    } else { // details instaceof WebAuthenticationDetails
      // oauthToken = (authentication);
    }

    if (oauthToken == null) {
      return;
    }

    // String authorizationHeader = "Bearer " + oauthToken;
    String authorizationHeader = "" + oauthToken;
    context.setHeader("Authorization", authorizationHeader);
  }

}
