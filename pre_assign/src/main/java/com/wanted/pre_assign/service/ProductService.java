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

@Service
@RequiredArgsConstructor
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
     * @param productId
     * @return ProductDTO
     */
    public ProductDTO getProductDetail(Long productId) {
        Products product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        return ProductDTO.toDTO(product, product.getMembers().getMemberId());
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
    
}
