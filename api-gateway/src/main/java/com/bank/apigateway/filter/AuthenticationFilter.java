package com.bank.apigateway.filter;

import com.bank.apigateway.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
   private JwtService jwtService;
    // private RestTemplate restTemplate;

    public AuthenticationFilter(){
        super(Config.class);
    }
    public static class Config{

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader=authHeader.substring(7);
                }
                try {
                    jwtService.validateToken(authHeader);
                    //rest call to auth service
                    //restTemplate.getForObject("http://AUTH-SERVICE/auth/validate?token="+authHeader,String.class);

                }catch (Exception e){
                    throw new RuntimeException("Un authorized access to application");
                }
            }
            return chain.filter(exchange);
        };
    }

}
