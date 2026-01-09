package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ProductCrawler;
import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.product.CrawledProductDto;
import com.project.healthy_life_was.healthy_life.dto.product.request.CrawlRequestDto;
import com.project.healthy_life_was.healthy_life.dto.product.response.ProductDetailResponseDto;
import com.project.healthy_life_was.healthy_life.dto.product.response.ProductListResponseDto;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.product.ProductCategoryDetail;
import com.project.healthy_life_was.healthy_life.repository.ProductCategoryDetailRepository;
import com.project.healthy_life_was.healthy_life.repository.ProductRepository;
import com.project.healthy_life_was.healthy_life.repository.ReviewRepository;
import com.project.healthy_life_was.healthy_life.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {

    public final ProductRepository productRepository;
    public final ReviewRepository reviewRepository;
    public final ProductCategoryDetailRepository productCategoryDetailRepository;
    public final ProductCrawler crawler;

    @Override
    public ResponseDto<List<ProductListResponseDto>> getAllProduct() {
        List<ProductListResponseDto> data = null;
        try {
            List<Product> products = productRepository.findAll();
            data = products.stream()
                    .map(product -> {
                        ProductCategoryDetail productCategoryDetail = productCategoryDetailRepository.findByPId(product.getPId());
                        double averageRating = reviewRepository.findAverageRatingByProductId(product.getPId());
                        return new ProductListResponseDto(product, averageRating, productCategoryDetail);
                    })
                    .collect(Collectors.toList());
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
                    .map(product -> {
                        ProductCategoryDetail productCategoryDetail = productCategoryDetailRepository.findByPId(product.getPId());
                        double averageRating = reviewRepository.findAverageRatingByProductId(product.getPId());
                        return new ProductListResponseDto(product, averageRating, productCategoryDetail);
                    })
                    .collect(Collectors.toList());

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
                    .map(product -> {
                        ProductCategoryDetail productCategoryDetail = productCategoryDetailRepository.findByPId(product.getPId());
                        double averageRating = reviewRepository.findAverageRatingByProductId(product.getPId());
                        return new ProductListResponseDto(product, averageRating, productCategoryDetail);
                    })
                    .collect(Collectors.toList());
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
                    .map(product -> {
                        ProductCategoryDetail productCategoryDetail = productCategoryDetailRepository.findByPId(product.getPId());
                        double averageRating = reviewRepository.findAverageRatingByProductId(product.getPId());
                        return new ProductListResponseDto(product, averageRating, productCategoryDetail);
                    })
                    .collect(Collectors.toList());
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
            System.out.println(username);
            List<Product> productList = productRepository.findByUsername(username);
            System.out.println(username);
            data = productList.stream()
                    .map(product -> {
                        ProductCategoryDetail productCategoryDetail = productCategoryDetailRepository.findByPId(product.getPId());
                        double averageRating = reviewRepository.findAverageRatingByProductId(product.getPId());
                        return new ProductListResponseDto(product, averageRating, productCategoryDetail);
                    })
                    .collect(Collectors.toList());
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<List<ProductListResponseDto>> crawl(CrawlRequestDto dto) {
        List<ProductListResponseDto> data = new ArrayList<>();
        List<String> urls = dto.getUrls();

        for (String url: urls) {

            CrawledProductDto crawlDto = crawler.crawl(url);

            if (productRepository.existsByPName(crawlDto.getName())) {
                continue;
            }

            Product product = Product.builder()
                    .pName(crawlDto.getName())
                    .pPrice(crawlDto.getPrice())
                    .pDescription(crawlDto.getDescription())
                    .pIngredients(crawlDto.getIngredients())
                    .pNutritionInfo(crawlDto.getNutrition())
                    .pOrigin(crawlDto.getOrigin())
                    .pUsage("냉장보관")
                    .pExpirationDate(Date.valueOf(LocalDate.now().plusMonths(6)))
                    .pManufacturer("랭킹닭컴")
                    .pImgUrl(crawlDto.getImageUrl())
                    .pStockStatus(1)
                    .build();

            productRepository.save(product);

            data.add(
                    ProductListResponseDto.from(product)
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}




