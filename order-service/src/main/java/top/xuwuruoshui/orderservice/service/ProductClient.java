package top.xuwuruoshui.orderservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.xuwuruoshui.orderservice.fallback.ProductClientFallback;

/**
 * product-client
 */
@FeignClient(value = "product-service",fallback = ProductClientFallback.class)
public interface ProductClient {
    @GetMapping("/api/v1/product/find")
    String findById(@RequestParam(value ="id")int id);
}
