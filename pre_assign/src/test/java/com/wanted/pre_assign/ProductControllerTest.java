package com.wanted.pre_assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.pre_assign.dto.OrderDTO;
import com.wanted.pre_assign.dto.ProductDTO;
import com.wanted.pre_assign.service.ProductService;

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    private List<ProductDTO> mockProductList;

    @BeforeEach
    void setUp() {
        mockProductList = productService.getProductList();
    }

    @DisplayName("제품 등록 테스트")
    @Test
    void createProduct() throws Exception {
        // 등록할 제품
        ProductDTO productDTO = new ProductDTO(8L, 3L, "클라이밍화", 45000, "판매중");
        String productJson = objectMapper.writeValueAsString(productDTO);

        mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andDo(print());

        List<ProductDTO> updatedProductList = productService.getProductList();
        assertEquals(mockProductList.size() + 1, updatedProductList.size());
    }

    @DisplayName("상품 목록 조회 테스트")
    @Test
    void getProductList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/product/getList")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<ProductDTO> productResponseList = objectMapper.readValue(responseContent, new TypeReference<>() {
        });
        assertEquals(productService.getProductList(), productResponseList);
    }

    @DisplayName("제품 상세 조회 테스트")
    @Test
    void getProductDetail() throws Exception {
        // Given
        Long productId = 1L;
        ProductDTO productDTO = productService.getProductDetail(productId);

        // When & Then
        MvcResult mvcResult = mockMvc.perform(get("/product/getList/detail")
                            .param("productId", productId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8"))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        ProductDTO productResponse 
                = objectMapper.readValue(responseContent, ProductDTO.class);

        assertEquals(productDTO, productResponse);
    }

    @DisplayName("제품 구매 시작 테스트")
    @Test
    void startPurchase() throws Exception {
        Long productId = 1L;
        Long memberId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/product/startPurchase")
                .param("productId", productId.toString())
                .param("memberId", memberId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        OrderDTO orderResponse = objectMapper.readValue(responseContent, OrderDTO.class);

        assertEquals(memberId, orderResponse.getMemberId());
        assertEquals(productId, orderResponse.getProductId());
    }

    @DisplayName("제품 구매 완료 테스트")
    @Test
    void completePurchase() throws Exception {
        Long productId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/product/completePurchase")
                .param("productId", productId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        ProductDTO productResponse = objectMapper.readValue(responseContent, ProductDTO.class);

        assertEquals("완료", productResponse.getReserveState());
    }

    @DisplayName("구매한 제품 목록 조회 테스트")
    @Test
    void getProductsIBought() throws Exception {
        Long memberId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/product/getProductsIBought")
                .param("memberId", memberId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<ProductDTO> boughtListResponse = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertEquals(productService.getProductsIBought(memberId), boughtListResponse);
    }

    @DisplayName("예약중인 제품 목록 조회 테스트")
    @Test
    void getReservedProduct() throws Exception {
        Long memberId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/product/getReservedProduct")
                .param("memberId", memberId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        List<ProductDTO> reservedListResponse = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        assertEquals(productService.getReservedProduct(memberId), reservedListResponse);
    }

    @DisplayName("구매자, 판매자의 제품 상세 조회 테스트")
    @Test
    void getOrderDetail() throws Exception {
        Long productId = 1L;

        MvcResult mvcResult = mockMvc.perform(get("/product/orderDetail")
                .param("productId", productId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        Map<String, Object> orderDetailResponse = objectMapper.readValue(responseContent, new TypeReference<>() {
        });

        ProductDTO productResponse = objectMapper.convertValue(orderDetailResponse.get("product"), ProductDTO.class);
        OrderDTO orderResponse = objectMapper.convertValue(orderDetailResponse.get("order"), OrderDTO.class);

        assertEquals(productService.getProductDetail(productId), productResponse);
        assertEquals(productService.getOrderDetail(productId), orderResponse);
    }
}
