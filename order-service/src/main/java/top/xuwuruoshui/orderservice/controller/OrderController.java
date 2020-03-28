package top.xuwuruoshui.orderservice.controller;

import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuwuruoshui.orderservice.domain.ProductOrder;
import top.xuwuruoshui.orderservice.service.ProductOrderService;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private ProductOrderService productOrderService;

    public OrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @RequestMapping("save")
    public Object save(@RequestParam("user_id")int userId,@RequestParam("product_id")int productId){
        ProductOrder productOrder = productOrderService.save(userId, productId);
        return productOrder;
    }
}
