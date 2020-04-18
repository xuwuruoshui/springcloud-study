package top.xuwuruoshui.orderservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xuwuruoshui.orderservice.domain.ProductOrder;
import top.xuwuruoshui.orderservice.service.ProductClient;
import top.xuwuruoshui.orderservice.service.ProductOrderService;
import top.xuwuruoshui.orderservice.util.JsonUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductClient productClient;

    @Override
    public ProductOrder save(int userId, int productId) {


        String response = productClient.findById(productId);
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);



        logger.info("service save order");
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
