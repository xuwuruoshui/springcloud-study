package top.xuwuruoshui.orderservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.xuwuruoshui.orderservice.domain.ProductOrder;
import top.xuwuruoshui.orderservice.service.ProductOrderService;

import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private RestTemplate restTemplate;

    public ProductOrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

/*    @Autowired
    private LoadBalancerClient loadBalancer;*/

    @Override
    public ProductOrder save(int userId, int productId) {

        Map<String,Object> map = restTemplate.getForObject("http://product-service/api/v1/product/find?id=" + productId, Map.class);

/*        ServiceInstance instance = loadBalancer.choose("product-service");
        URI storesUri = URI.create(String.format("http://%s:%s/api/v1/product/find?id="+productId, instance.getHost(), instance.getPort()));
        Map<String,Object> map = restTemplate.getForObject(storesUri, Map.class);*/

        System.out.println(map);
        //获取商品详情
        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
        productOrder.setProductName(map.get("name").toString());
        productOrder.setPrice((Integer) map.get("price"));

        return productOrder;
    }
}
