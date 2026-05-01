package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.product.response.ProductListResponseDto;
import com.project.healthy_life_was.healthy_life.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.PRODUCT)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final String PRODUCT_GET_PHYSIQUE_ID = "/physiques";

    @GetMapping(PRODUCT_GET_PHYSIQUE_ID)
    public ResponseEntity<ResponseDto<List<ProductListResponseDto>>> getPhysiqueProduct (@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<List<ProductListResponseDto>> response = productService.getPhysiqueProduct(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
