package com.banquito.core.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.core.product.dto.ProductDTO;
import com.banquito.core.product.model.Product;
import com.banquito.core.product.model.ProductPK;
import com.banquito.core.product.service.ProductService;
import com.banquito.core.product.util.mapper.ProductMapper;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT })
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(ProductMapper.INSTANCE::toProductDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{codeProductType}/{code}")
    public ProductDTO getProductById(@PathVariable String codeProductType, @PathVariable String code) {
        ProductPK pk = new ProductPK(codeProductType, code);
        return ProductMapper.INSTANCE.toProductDTO(productService.getProductById(pk));
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return ProductMapper.INSTANCE.toProductDTO(
                productService.saveProduct(ProductMapper.INSTANCE.toProduct(productDTO)));
    }

    @PutMapping("/{codeProductType}/{code}")
    public ProductDTO updateProduct(@PathVariable String codeProductType, @PathVariable String code,
            @RequestBody ProductDTO productDTO) {
        ProductPK pk = new ProductPK(codeProductType, code);
        Product product = ProductMapper.INSTANCE.toProduct(productDTO);
        product.setPk(pk); // Set primary key values
        return ProductMapper.INSTANCE.toProductDTO(
                productService.saveProduct(product));
    }

    @DeleteMapping("/{codeProductType}/{code}")
    public void deleteProduct(@PathVariable String codeProductType, @PathVariable String code) {
        ProductPK pk = new ProductPK(codeProductType, code);
        productService.deleteProduct(pk);
    }
}
