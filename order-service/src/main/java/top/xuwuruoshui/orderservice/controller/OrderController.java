package top.xuwuruoshui.orderservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.xuwuruoshui.orderservice.domain.ProductOrder;
import top.xuwuruoshui.orderservice.service.ProductOrderService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private ProductOrderService productOrderService;
    private StringRedisTemplate redisTemplate;

    public OrderController(ProductOrderService productOrderService, StringRedisTemplate redisTemplate) {
        this.productOrderService = productOrderService;
        this.redisTemplate = redisTemplate;
    }



    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public ResponseEntity<?> save(@RequestParam("user_id")int userId,@RequestParam("product_id")int productId,HttpServletRequest request){
        ProductOrder productOrder = productOrderService.save(userId, productId);

        return new ResponseEntity<>(productOrder,HttpStatus.OK);
    }

    private ResponseEntity<?> saveOrderFail(int userId, int productId, HttpServletRequest request){
        //monitor
        String saveOrderKey = "save-order";
        String sendValue = redisTemplate.opsForValue().get(saveOrderKey);
        final String ipAddress = request.getRemoteAddr();

        new Thread(()->{
            if(StringUtils.isBlank(sendValue)){
                System.out.println("Emergency SMS, user failed to place an order,please find the cause of the error, the IP address is\t"+ipAddress);
                // send a http request to call SMS service TODO
                redisTemplate.opsForValue().set(saveOrderKey,"save-order",20, TimeUnit.SECONDS);
            }else{
                System.out.println("Sent SMS, no resending within 20 seconds");
            }
        }).start();

        HashMap<Object, Object> msg = new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","The system is busy,please retry");
        return new ResponseEntity<>(msg,HttpStatus.SERVICE_UNAVAILABLE);
    }




}
