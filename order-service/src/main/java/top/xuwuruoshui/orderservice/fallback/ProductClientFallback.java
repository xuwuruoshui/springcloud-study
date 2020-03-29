package top.xuwuruoshui.orderservice.fallback;

import org.springframework.stereotype.Component;
import top.xuwuruoshui.orderservice.service.ProductClient;

/**
 * catch the error and print it to log
 */
@Component
public class ProductClientFallback implements ProductClient {
    @Override
    public String findById(int id) {
        System.out.println("faild to call product-service");
        return null;
    }
}