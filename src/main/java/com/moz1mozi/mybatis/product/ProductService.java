package com.moz1mozi.mybatis.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;

    public Long insertProduct(ProductDto productDto) {
        return productDao.insertProduct(productDto);
    }

    public int deleteProduct(Long prodId) {
        return productDao.deleteProduct(prodId);
    }
}
