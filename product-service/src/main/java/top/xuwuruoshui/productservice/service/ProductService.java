package top.xuwuruoshui.productservice.service;

import top.xuwuruoshui.productservice.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> listProduct();

    Product findById(int id);
}
