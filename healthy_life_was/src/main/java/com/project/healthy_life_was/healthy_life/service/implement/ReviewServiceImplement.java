package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.ReviewListDto;
import com.project.healthy_life_was.healthy_life.dto.review.request.ReviewCreateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.review.request.ReviewUpdateRequestDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ProductReviewListResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewCreateResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewResponseDto;
import com.project.healthy_life_was.healthy_life.dto.review.response.ReviewUpdateResponseDto;
import com.project.healthy_life_was.healthy_life.entity.order.OrderDetail;
import com.project.healthy_life_was.healthy_life.entity.order.OrderStatus;
import com.project.healthy_life_was.healthy_life.entity.review.Review;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.OrderDetailRepository;
import com.project.healthy_life_was.healthy_life.repository.ReviewRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.ImgService;
import com.project.healthy_life_was.healthy_life.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplement implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ImgService imgService;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseDto<ReviewCreateResponseDto> createReview(String username, Long orderDetailId, ReviewCreateRequestDto dto) {
        ReviewCreateResponseDto data = null;

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "user"));
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "orderDetail"));

            if (orderDetail.getOrderStatus() != OrderStatus.DELIVERED) {
                return ResponseDto.setFailed("배송 완료 후 리뷰 작성 가능");
            }

            if (reviewRepository.existsByOrderDetail_OrderDetailId(orderDetailId)) {
                return ResponseDto.setFailed("이미 리뷰가 존재합니다");
            }

            String reviewImgPath = null;

            if (dto.getReviewImgUrl() != null && !dto.getReviewImgUrl().isEmpty()) {
                reviewImgPath = imgService.convertImgFile(dto.getReviewImgUrl(), "reviewImg");
            }
            Review review = Review.builder()
                    .user(user)
                    .orderDetail(orderDetail)
                    .reviewRating(dto.getReviewRating())
                    .reviewContent(dto.getReviewContent())
                    .reviewImgUrl(reviewImgPath)
                    .reviewCreatAt(LocalDate.now())
                    .build();
            if (!orderDetail.getOrderStatus().equals(OrderStatus.DELIVERED)) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            reviewRepository.save(review);

            data = new ReviewCreateResponseDto(review);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<ProductReviewListResponseDto> getMyReview(String username) {
        ProductReviewListResponseDto data = null;
        try {
            List<Review> reviews = reviewRepository.findByUser_Username(username);
            List<ReviewListDto> reviewList = reviews.stream()
                    .sorted(Comparator.comparing(Review::getReviewCreatAt).reversed())
                    .map(review -> new ReviewListDto(
                            review.getReviewId(),
                            review.getOrderDetail().getProduct().getPName(),
                            review.getOrderDetail().getProduct().getPId(),
                            review.getOrderDetail().getProduct().getPImgUrl(),
                            review.getUser().getUserNickName(),
                            review.getReviewRating(),
                            review.getReviewContent(),
                            review.getReviewImgUrl(),
                            review.getReviewCreatAt(),
                            review.getOrderDetail().getOrder().getOrderDate()
                    ))
                    .toList();
            data = new ProductReviewListResponseDto(reviewList);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<ReviewUpdateResponseDto> updateReview(String username, Long reviewId, ReviewUpdateRequestDto dto) {
        ReviewUpdateResponseDto data = null;
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "review"));
            if (dto.getReviewImgUrl() != null) {
                String reviewImgPath = imgService.convertImgFile(dto.getReviewImgUrl(), "reviewImg");
                review.setReviewImgUrl(reviewImgPath);
            }
            if (ChronoUnit.DAYS.between(review.getReviewCreatAt(), LocalDate.now()) <= 30) {
                review.setReviewRating(dto.getReviewRating());
                review.setReviewContent(dto.getReviewContent());
            }
            
            reviewRepository.save(review);
            data = new ReviewUpdateResponseDto(review);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteReview(String username, Long reviewId) {
        try{
            Review review = reviewRepository.findByUser_UsernameAndReviewId(username, reviewId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA));
            if (review == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            OrderDetail orderDetail = review.getOrderDetail();
            orderDetail.setReview(null);
            reviewRepository.delete(review);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<Boolean> duplicateReview(String username, Long orderDetailId) {
        try {
            boolean result = reviewRepository.existsByUser_usernameAndOrderDetail_orderDetailId(username, orderDetailId);

            if (result) {
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, true);
            } else {
                return ResponseDto.setSuccess(ResponseMessage.DUPLICATED_REVIEW, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<ProductReviewListResponseDto> getAllReview() {
        ProductReviewListResponseDto data = null;
        try {
            List<Review> reviews = reviewRepository.findAll();
            List<ReviewListDto> reviewList = reviews.stream()
                    .sorted(Comparator.comparing(Review::getReviewCreatAt).reversed())
                    .map(review -> new ReviewListDto(
                            review.getReviewId(),
                            review.getOrderDetail().getProduct().getPName(),
                            review.getOrderDetail().getProduct().getPId(),
                            review.getOrderDetail().getProduct().getPImgUrl(),
                            review.getUser().getUserNickName(),
                            review.getReviewRating(),
                            review.getReviewContent(),
                            review.getReviewImgUrl(),
                            review.getReviewCreatAt(),
                            review.getOrderDetail().getOrder().getOrderDate()
                    ))
                    .toList();
            data = new ProductReviewListResponseDto(reviewList);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<ProductReviewListResponseDto> getAllReviewProduct(Long pId) {
        ProductReviewListResponseDto data = null;
        try {
            List<Review> reviews = reviewRepository.findByOrderDetail_Product_pId(pId);
            List<ReviewListDto> reviewList = reviews.stream()
                    .sorted(Comparator.comparing(Review::getReviewCreatAt).reversed())
                    .map(review -> new ReviewListDto(
                            review.getReviewId(),
                            review.getOrderDetail().getProduct().getPName(),
                            review.getOrderDetail().getProduct().getPId(),
                            review.getOrderDetail().getProduct().getPImgUrl(),
                            review.getUser().getUsername(),
                            review.getReviewRating(),
                            review.getReviewContent(),
                            review.getReviewImgUrl(),
                            review.getReviewCreatAt(),
                            review.getOrderDetail().getOrder().getOrderDate()
                    ))
                    .toList();
            data = new ProductReviewListResponseDto(reviewList);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<ReviewResponseDto> getOneReview(String username, Long reviewId) {
        ReviewResponseDto data = null;
        try {
            Review review = reviewRepository.findByUser_UsernameAndReviewId(username, reviewId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA));

            if(review == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

                data = new ReviewResponseDto(review);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}
