package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.product.response.ProductDetailResponseDto;
import com.project.healthy_life_was.healthy_life.dto.product.response.ProductListResponseDto;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.repository.ProductRepository;
import com.project.healthy_life_was.healthy_life.repository.ReviewRepository;
import com.project.healthy_life_was.healthy_life.repository.UserPhysiqueTagRepository;
import com.project.healthy_life_was.healthy_life.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    public final ProductRepository productRepository;
    public final ReviewRepository reviewRepository;
    private final UserPhysiqueTagRepository userPhysiqueTagRepository;

    private Map<Long, Double> ratingMap() {
        return reviewRepository.findAllAverageRatings()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Double) row[1]
                ));
    }

    @Override
    public ResponseDto<List<ProductListResponseDto>> getAllProduct() {
        List<ProductListResponseDto> data = null;
        try {
            List<Product> products = productRepository.findAll();

            data = products.stream()
                    .map(product -> new ProductListResponseDto(
                            product,
                            ratingMap().getOrDefault(product.getPId(), 0.0),
                            product.getProductCategoryDetail()
                    ))
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<ProductDetailResponseDto> getPIdProduct(Long pId) {
        ProductDetailResponseDto data = null;
        try {
            Optional<Product> optionalProduct = productRepository.findById(pId);
            if (optionalProduct.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            Product product = optionalProduct.get();
            double averageRating = reviewRepository.findAverageRatingByProductId(product.getPId());
            data = new ProductDetailResponseDto(product, averageRating);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<ProductListResponseDto>> getPCategoryProduct(String pCategoryName) {
        List<ProductListResponseDto> data = null;
        try {
            List<Product> productList = productRepository.findByPCategoryName(pCategoryName);

            data = productList.stream()
                    .map(product -> new ProductListResponseDto(
                            product,
                            ratingMap().getOrDefault(product.getPId(), 0.0),
                            product.getProductCategoryDetail()
                    ))
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<ProductListResponseDto>> getCategoryDetailProduct(String pCategoryName, String pCategoryDetailName) {
        List<ProductListResponseDto> data = null;
        try {
            List<Product> productList = productRepository.findByPCategoryNameAndPCategoryDetailsName(pCategoryName, pCategoryDetailName);

            data = productList.stream()
                    .map(product -> new ProductListResponseDto(
                            product,
                            ratingMap().getOrDefault(product.getPId(), 0.0),
                            product.getProductCategoryDetail()
                    ))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<ProductListResponseDto>> getPNameProduct(String pName) {
        List<ProductListResponseDto> data = null;

        try {
            List<Product> productList = productRepository.findByPName(pName);

            data = productList.stream()
                    .map(product -> new ProductListResponseDto(
                            product,
                            ratingMap().getOrDefault(product.getPId(), 0.0),
                            product.getProductCategoryDetail()
                    ))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<ProductListResponseDto>> getPhysiqueProduct(String username) {
        List<ProductListResponseDto> data = null;

        try {
            List<Long> userPhysiqueTagIds = userPhysiqueTagRepository.findByUserName(username);

            List<Product> productList =
                    productRepository.findProductsByTagTypes(userPhysiqueTagIds);

            if (productList.isEmpty()) {
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, new ArrayList<>());
            }

            data = productList.stream()
                    .map(product -> new ProductListResponseDto(
                            product,
                            ratingMap().getOrDefault(product.getPId(), 0.0),
                            product.getProductCategoryDetail()
                    ))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}




