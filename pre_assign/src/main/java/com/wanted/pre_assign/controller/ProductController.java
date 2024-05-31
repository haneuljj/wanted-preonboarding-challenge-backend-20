package com.wanted.pre_assign.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wanted.pre_assign.dto.OrderDTO;
import com.wanted.pre_assign.dto.ProductDTO;
import com.wanted.pre_assign.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    // 제품등록
    @PostMapping("/create")
    public void createProduct(
        @RequestBody ProductDTO productDTO
    ) {
        productService.createProduct(productDTO);
    }

    // 제품 목록 조회
    @GetMapping("/getList")
    public ResponseEntity<List<ProductDTO>> getProductList() {
        List<ProductDTO> productResponseList = productService.getProductList();
        
        return ResponseEntity.ok(productResponseList);
    }

    // 제품 상세 조회
    @GetMapping("/getList/detail")
    public ResponseEntity<ProductDTO> getProductDetail(
        @RequestParam(name = "productId") Long productId
    ) {
        ProductDTO productResponse = productService.getProductDetail(productId);

        return ResponseEntity.ok(productResponse);
    }

    // 제품 구매 - 구매버튼을 눌러 예약 걸기
    @GetMapping("/startPurchase")
    public ResponseEntity<OrderDTO> startPurchase(
        @RequestParam(name = "productId") Long productId,
        @RequestParam(name = "memberId") Long buyerId
    ) {
        OrderDTO orderResponse = productService.startPurchase(productId, buyerId);

        return ResponseEntity.ok(orderResponse);
    }

    // 제품 구매 - 판매자가 판매승인 버튼을 눌러 판매 완료하기
    

    // 구매한 제품 목록 조회

    // 예약중인 제품 목록 조회
}
