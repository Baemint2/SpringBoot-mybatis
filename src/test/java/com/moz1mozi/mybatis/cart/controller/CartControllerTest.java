package com.moz1mozi.mybatis.cart.controller;

import com.moz1mozi.mybatis.cart.dao.CartMapper;
import com.moz1mozi.mybatis.cart.service.CartService;
import com.moz1mozi.mybatis.category.dao.CategoryMapper;
import com.moz1mozi.mybatis.image.dao.ImageMapper;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.dao.ProductMapper;
import com.moz1mozi.mybatis.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private CartMapper cartMapper;

    @MockBean
    private MemberMapper memberMapper;

    @MockBean
    private CategoryMapper categoryMapper;

    @MockBean
    private ImageMapper imageMapper;

    @MockBean
    private ProductMapper productMapper;


    @Test
    @WithMockUser
    void 상품_선택삭제() throws Exception {
        when(cartService.deleteCartItems(anyString())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cart/item/82,85")
                .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        when(cartService.deleteCartItems(anyString())).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/cart/item/82,85")
                .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}