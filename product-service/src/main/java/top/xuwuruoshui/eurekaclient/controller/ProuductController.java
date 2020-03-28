package top.xuwuruoshui.eurekaclient.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuwuruoshui.eurekaclient.domain.Product;
import top.xuwuruoshui.eurekaclient.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProuductController {

    @Value("${server.port}")
    private int port;

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
        product_new.setName(product.getName()+"data from port"+port);
        return product_new;
    }
}
