package onlinestore.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import onlinestore.gateway.security.AuthenticationFilter;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter filter;

    private final String SWAGGER = "/api/v3/api-docs";

    @Autowired
    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        "auth_route", r -> r.path("/auth/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://AUTH-SERVICE")
                )
                .route(
                        "order_route", r -> r.path("/api/order**", "/api/order/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://ORDER-SERVICE")
                )
                .route(
                        "payment_route", r -> r.path(
                                    "/api/payment**","/api/payment/**",
                                        "/api/client**", "/api/client/**",
                                        "/api/transaction**", "/api/transaction/**"
                                )
                                .filters(f -> f.filter(filter))
                                .uri("lb://PAYMENT-SERVICE")
                )
                .route(
                        "inventory_route", r -> r.path(
                                    "/api/inventory**", "/api/inventory/**",
                                        "/api/product**", "/api/product/**"
                                )
                                .filters(f -> f.filter(filter))
                                .uri("lb://INVENTORY-SERVICE")
                )
                .route(
                        "delivery_route", r -> r.path("/api/delivery**", "/api/delivery/**")
                                .filters(f -> f.filter(filter))
                                .uri("lb://DELIVERY-SERVICE")
                )
                .route(
                        "order_swagger_route", r -> r.path("/order"+SWAGGER)
                                .filters(f -> f.rewritePath("/order"+SWAGGER, SWAGGER))
                                .uri("lb://ORDER-SERVICE")
                )
                .route(
                        "order_swagger_route", r -> r.path("/payment"+SWAGGER)
                                .filters(f -> f.rewritePath("/payment"+SWAGGER, SWAGGER))
                                .uri("lb://PAYMENT-SERVICE")
                )
                .route(
                        "order_swagger_route", r -> r.path("/inventory"+SWAGGER)
                                .filters(f -> f.rewritePath("/inventory"+SWAGGER, SWAGGER))
                                .uri("lb://INVENTORY-SERVICE")
                )
                .route(
                        "order_swagger_route", r -> r.path("/delivery"+SWAGGER)
                                .filters(f -> f.rewritePath("/delivery"+SWAGGER, SWAGGER))
                                .uri("lb://DELIVERY-SERVICE")
                )
                .build();
    }
}
