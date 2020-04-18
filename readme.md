# 1.CAP

- Consistency: consistency equivalent to haveing a single up-to-date copy of the data 
- Availaility: high availability of that data(for updates)
- Partition tolerace: tolerance to network partitions

# 2.Eureka
> UseAge
1. add `@EnableEurekaServer` to startup class
2. add configurations to `pom` and `application.yml`

> Server
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
```
```yml
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

> Client
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
```yml
server:
  port: 8771

# eureka server path
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
# server name
spring:
  application:
    name: product-service
```

# 3. Ribbon
>Usage
1. Maven
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>
```
>method1:
1. add to startup class
```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
```
2. injection restTemplate to server class
```java
Map<String,Object> map = restTemplate.getForObject("http://product-service/api/v1/product/find?id=" + productId, Map.class);
```

>method2:
1. add to starup class
```java
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
```
2. injection restTemplate and  LoadBalancerClient to server class
```java
//add loadBalance by programming
        ServiceInstance instance = loadBalancer.choose("product-service");
        URI storesUri = URI.create(String.format("http://%s:%s/api/v1/product/find?id="+productId, instance.getHost(), instance.getPort()));
        Map<String,Object> map = restTemplate.getForObject(storesUri, Map.class);
```
3. if you want to change rules, you need to add the following configuartions to your applications.yml:
```java
#RoundRule is defualt,you can find the implementation classes of IRuls.
#If your machine configuration is similar, it is recommended to use the default roundrule. If some machines have good configuration and performance, you can use weighted response time rule
product-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```
# 4. Feign
>Usage
1. Maven
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```
1. add to starup class
```java
@EnableFeignClients
```
2. add a service interface
>@FeignClient("you will call the product'name")
>The method is the same as the method of the service to be called
```java
@FeignClient("product-service")
public interface ProductClient {
    @GetMapping("/api/v1/product/find")
    String findById(@RequestParam(value ="id")int id);
}
```
3. timeout
>hytrix default 1s
```yml
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: basic
```
4. Feign's load balancing strategy is the same as ribbon configuration
```java
product-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```
# 5.Hytrix
>Solutions to various abnormal situations such as high system load,sudden traffic or network
1. Fuse
when there is a problem with a service and no normal call is issued,in order to make the system run normally,stop calling the service
2. Downgrade
when the load of a certain module increase,the call volume becomes larger,and some unimportant function modules are closed

>Usage
1. Maven
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
```
2. let hystrix enable
```yml
feign:
  hystrix:
    enabled: true
```
2. add to startup class
```java
@EnableCircuitBreaker
```
3. add to the api method,and add the fallbackMethod 
```java
@HystrixCommand(fallbackMethod = "saveOrderFail")
```
>the fallbackMethod's args is the same as api method 
4. downgrad 
  1. create a package and class that implements order-service
  2. add to ProductClient's `@FeignClient` args
```java
@FeignClient(value = "product-service",fallback = ProductClientFallback.class)
```
5. dashboard
  1. Maven
```pom
        <!--dashboard-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```
  2. change the defualt timeoutInMilliseconds
```yaml
#default 1s
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 2000
```
  3. Expose "hystrix.stream" monitoring information
```yaml
management:
  endpoints:
    web:
      exposure:
        include: hystrix.stream
```
  4. visit 
<http://localhost:8781/hystrix>
  5. Fill in the url in the space
<http://localhost:8781/actuator/hystrix.stream>
>Judging the condition of the server according to the dashboard

# 6.zuul
>Usage
1. Maven
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
```
2. add `@EnableZuulPoxy` to start up class
3. add to `application.yml`
```yml
server:
  port: 9000

# server name
spring:
  application:
    name: api-gateway

# eureka path
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/


zuul:
  # custom routes mapping
  routes:
    product-service: /apigateway/product/**
    order-service: /apigateway/order/**
  #ignore product-service
  #ignored-services: product-service
  ignored-patterns: /*-service/**
```
4. add login filter
  1. create a package and a class
  2. extends `ZuulFilter`,override some method
  ```java
/**
 * login filter
 */
@Component
public class LoginFilter extends ZuulFilter {
    /**
     * filter type,pre filter
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * filter execution order,the smaller the first
     * @return
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    /**
     * Whether the filter is effective
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // System.out.println(request.getRequestURI());
        // System.out.println(request.getRequestURL());

        if("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }

        //login business logic
        if(StringUtils.isBlank(token)){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
```
7. rate limit
> the same as `LoginFilter`
```java
/**
 * order limit
 */
@Component
public class OrderRateLimiterFilter extends ZuulFilter {

    // 1000token/s
    private static final RateLimiter RATE_LIMITER = RateLimiter.create(1000);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -4;
    }


    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //Only limit the order interface
        if("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();

        if(!RATE_LIMITER.tryAcquire()){
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());
        };
        return null;
    }
}
```

# 7.Sleuth/ZipKin
>Sleuth is actually a tool that can track the process of a user request in the entire distributed system (including data collection, data transmission, data storage, data analysis, data visualization)
>Usage
1. Maven
> add to all server modules
```pom
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
```
2. log info
>example:
`INFO [order-service,a04acb78b43642b0,a04acb78b43642b0,false]`
1. spring application name
2. Trace ID,sleuth will generate a ID which named Trace ID and used to identify a request link. A request link has only one Trace ID
3. Span ID,it will get  metadata and can have multiple
4. Whether to output this information to the zipkin service(or other service) for collection and display
