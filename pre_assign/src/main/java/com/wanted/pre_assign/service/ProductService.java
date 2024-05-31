package com.wanted.pre_assign.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wanted.pre_assign.domain.Members;
import com.wanted.pre_assign.domain.Orders;
import com.wanted.pre_assign.domain.Products;
import com.wanted.pre_assign.dto.OrderDTO;
import com.wanted.pre_assign.dto.ProductDTO;
import com.wanted.pre_assign.repository.MemberRepository;
import com.wanted.pre_assign.repository.OrderRepository;
import com.wanted.pre_assign.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    /**
     * 제품 정보를 전달받아 제품등록하기
     * @param productDTO
     */
    public void createProduct(ProductDTO productDTO) {
        // 존재하는 회원인지 확인
        Members member = memberRepository.findById(productDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Products product = Products.toEntity(productDTO, member);

        productRepository.save(product);
    }

    /**
     * 제품 목록 조회하기
     * @return List<ProductDTO>
     */
    public List<ProductDTO> getProductList() {
        List<Products> productsList = productRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();

        productsList.forEach(
                (product) -> productDTOList.add(ProductDTO.toDTO(product, product.getMembers().getMemberId())));

        return productDTOList;
    }

    /**
     * 입력받은 아이디에 해당하는 제품 상세 정보 조회하기
     * - 상품정보, 주문정보(거래내역)도 반환해야함
     * @param productId
     * @return ProductDTO
     */
    public ProductDTO getProductDetail(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        return ProductDTO.toDTO(product, product.getMembers().getMemberId());
    }
    public OrderDTO getOrderDetail(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        Orders order = orderRepository.findByProducts(product)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));;

        return OrderDTO.toDTO(order, order.getMembers().getMemberId(), order.getProducts().getMembers().getMemberId(), productId);
    }

    /**
     * 입력받은 아이디에 해당하는 제품의 거래 시작
     * - 회원인지 확인
     * - 회원이라면 제품의 예약 상태를 예약중으로 변경
     * - 새로운 Order엔티티 생성
     * @param productId
     * @return 
     */
    @Transactional
    public OrderDTO startPurchase(Long productId, Long buyerId) {
        // 존재하는 회원인지 확인
        Members member = memberRepository.findById(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        // 제품의 예약상태를 예약중으로 수정
        product.setReserveState("예약중");

        // 새로운 Order객체 생성
        Orders order = new Orders(member, product);
        orderRepository.save(order);

        return OrderDTO.toDTO(order, buyerId, product.getMembers().getMemberId(), productId);
    }

    /**
     * 입력받은 제품 아이디에 해당하는 제품을 판매완료로 수정하여 거래 완료하기
     * - 제품의 예약상태 -> 완료로 바꾸기
     * - 주문의 상태 -> 판매승인완료로 바꾸기
     * @param productId
     * @return
     */
    @Transactional
    public ProductDTO completePurchase(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
        Orders order = orderRepository.findByProducts(product)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 주문입니다."));;

        product.setReserveState("완료");
        order.setOrderState("판매승인완료");

        return ProductDTO.toDTO(product, product.getMembers().getMemberId());
    }

    /**
     * 구매자의 구매했던 물품 리스트 조회
     * - 전달받은 회원 아이디로 주문 객체들 조회
     * - 주문 객체로 제품 조회
     * @param memberId
     * @return
     */
    public List<ProductDTO> getProductsIBought(Long memberId) {
        // 존재하는 회원인지 확인
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        
        // 회원에 해당하는 주문 정보들 조회
        List<Orders> orderList = orderRepository.findAllByMembers(member);

        List<Products> productList = new ArrayList<>();
        // 주문 객체들로 제품 정보들 조회 -> 제품 상태가 '완료'인경우만 리스트 추가
        for(Orders order : orderList) {
            Products product = productRepository.findByOrders(order)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
            
            if(product.getReserveState().equals("완료")) {
                productList.add(product);
            } 
        }

        List<ProductDTO> productDTOList = new ArrayList<>();
        productList.forEach((product) -> ProductDTO.toDTO(product, product.getMembers().getMemberId()));

        return productDTOList;
    }

    public List<ProductDTO> getReservedProduct(Long memberId) {
        // 존재하는 회원인지 확인
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 1) 구매자로서 예약중인 물건
        // 회원에 해당하는 주문 정보들 조회
        List<Orders> orderList = orderRepository.findAllByMembers(member);

        List<Products> productList = new ArrayList<>();
        // 주문 객체들로 제품 정보들 조회 -> 제품 상태가 '예약중'인경우만 리스트 추가
        for(Orders order : orderList) {
            Products product = productRepository.findByOrders(order)
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
            
            if(product.getReserveState().equals("예약중")) {
                productList.add(product);
            } 
        }

        // 2) 판매자로서 예약중인 물건
        List<Products> allProductList = productRepository.findAllByMembers(member);
        for(Products product : allProductList) {
            if(product.getReserveState().equals("예약중")) {
                productList.add(product);
            }
        }

        List<ProductDTO> productDTOList = new ArrayList<>();
        productList.forEach((product) -> ProductDTO.toDTO(product, product.getMembers().getMemberId()));

        return productDTOList;
    }


    
}
