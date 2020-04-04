package top.xuwuruoshui.orderservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.xuwuruoshui.orderservice.domain.ProductOrder;
import top.xuwuruoshui.orderservice.service.ProductClient;
import top.xuwuruoshui.orderservice.service.ProductOrderService;
import top.xuwuruoshui.orderservice.util.JsonUtils;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductClient productClient;

    @Override
    public ProductOrder save(int userId, int productId) {


        String response = productClient.findById(productId);
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);



        //获取商品详情
        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.valueOf(jsonNode.get("price").toString()));

        return productOrder;
    }
}
