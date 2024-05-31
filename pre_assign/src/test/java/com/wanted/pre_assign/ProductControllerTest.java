package com.wanted.pre_assign;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
        // when(productService.getProductDetail(productId)).thenReturn(productDTO);

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
}
