package top.xuwuruoshui.orderservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuwuruoshui.orderservice.domain.ProductOrder;
import top.xuwuruoshui.orderservice.service.ProductOrderService;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private ProductOrderService productOrderService;

    public OrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public ResponseEntity<?> save(@RequestParam("user_id")int userId,@RequestParam("product_id")int productId){
        ProductOrder productOrder = productOrderService.save(userId, productId);

        return new ResponseEntity<>(productOrder,HttpStatus.OK);
    }

    private ResponseEntity<?> saveOrderFail(int userId,int productId){
        HashMap<Object, Object> msg = new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","The system is busy,please retry");
        return new ResponseEntity<>(msg,HttpStatus.SERVICE_UNAVAILABLE);
    }
}
