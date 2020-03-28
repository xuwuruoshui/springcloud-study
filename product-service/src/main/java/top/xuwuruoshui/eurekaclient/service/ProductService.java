package top.xuwuruoshui.eurekaclient.service;

import top.xuwuruoshui.eurekaclient.domain.Product;

import java.util.List;

public interface ProductService {
    List<Product> listProduct();

    Product findById(int id);
}
