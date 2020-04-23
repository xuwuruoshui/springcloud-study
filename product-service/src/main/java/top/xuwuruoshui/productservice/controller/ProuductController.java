package top.xuwuruoshui.productservice.controller;

import ch.qos.logback.core.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuwuruoshui.productservice.domain.Product;
import top.xuwuruoshui.productservice.service.ProductService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/product")
@RefreshScope
public class ProuductController {

    @Value("${server.port}")
    private int port;

    @Value("${env}")
    private String env;

    private ProductService productService;

    public ProuductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 获取所有商品列表
     * @return
     */
    @GetMapping("list")
    public List<Product> list(){
        return productService.listProduct();
    }

    /**
     * 根据id查找商品
     * @param id
     * @return
     */
    @GetMapping("find")
    public Product findById(@RequestParam("id") int id){


        Product product = productService.findById(id);

        Product product_new = new Product();
        BeanUtils.copyProperties(product,product_new);
        product_new.setName(product.getName()+"data from port"+port+",env = "+env);
        return product_new;
    }
}
