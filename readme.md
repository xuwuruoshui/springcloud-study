# 1.CAP

- Consistency: consistency equivalent to haveing a single up-to-date copy of the data 
- Availaility: high availability of that data(for updates)
- Partition tolerace: tolerance to network partitions

# 2.Eureka
> UseAge
1. add `@EnableEurekaServer` to startup class
2. add configurations to `application.yml`

> Server
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
>usage
method1:
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
method2:
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
>if you want to change rules, you need to add the following configuartions to your applications.yml:
```java
#RoundRule is defualt,you can find the implementation classes of IRuls.
#If your machine configuration is similar, it is recommended to use the default roundrule. If some machines have good configuration and performance, you can use weighted response time rule
product-service:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```
# 4. Feign
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
```java
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: basic
```
