package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishListItemDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishlistCountResponseDto;
import com.project.healthy_life_was.healthy_life.dto.wishList.response.WishlistResponseDto;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.entity.whishList.WishList;
import com.project.healthy_life_was.healthy_life.entity.whishList.WishListItem;
import com.project.healthy_life_was.healthy_life.repository.ProductRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.repository.WishListItemRepository;
import com.project.healthy_life_was.healthy_life.repository.WishListRepository;
import com.project.healthy_life_was.healthy_life.service.WishListService;
import com.sun.jdi.InternalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImplement implements WishListService {
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;
    private final ProductRepository productRepository;

    private ResponseDto<WishlistResponseDto> getWishlistResponseDtoResponseDto(WishList wishList) {
        List<WishListItem> items = wishListItemRepository.findByWishList_WishListId(wishList.getWishListId());
        List<WishListItemDto> itemDtos = items.stream()
                .map(item -> new WishListItemDto(item.getProduct(), item))
                .collect(Collectors.toList());

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS,
                new WishlistResponseDto(wishList, itemDtos));
    }

    @Override
    public ResponseDto<WishlistResponseDto> addProduct(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        // Find product, throw exception if not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_PRODUCT));

        // Check if product is already in wishlist
        if (wishListItemRepository.findByPIdAndUserId(productId, user.getUserId()).isPresent()) {
            return ResponseDto.setFailed(ResponseMessage.ALREADY_EXIST_IN_WISHLIST);
        }

        WishList wishList = wishListRepository.findByUser(user)
                .orElseGet(() -> {
                    WishList newWishList = new WishList();
                    newWishList.setUser(user);
                    return wishListRepository.save(newWishList);
                });

        WishListItem newItem = new WishListItem();
        newItem.setWishList(wishList);
        newItem.setProduct(product);
        newItem.setWishListAddedAt(LocalDateTime.now());
        wishListItemRepository.save(newItem);

        return getWishlistResponseDtoResponseDto(wishList);
    }

    @Override
    public ResponseDto<WishlistResponseDto> getMyWishList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_WISH_LIST));

        return getWishlistResponseDtoResponseDto(wishList);
    }

    @Override
    public ResponseDto<WishlistCountResponseDto> getWishListCount(Long pId) {
        WishlistCountResponseDto data = null;
        int count = 0;

        Product product = productRepository.findById(pId)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_PRODUCT));

        count = wishListItemRepository.countByProductId(pId);
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS,
                new WishlistCountResponseDto(pId, count));
    }

    @Transactional
    @Override
    public ResponseDto<Void> resetWishList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        wishListItemRepository.deleteByWishList_User(user);

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<WishlistResponseDto> deleteProductFromWishList(String username, Long pId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_USER));

        Optional<WishListItem> wishListItem = wishListItemRepository.findByPIdAndUserId(pId, user.getUserId());

        if(wishListItem.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_PRODUCT_IN_WISH_LIST);
        }

        wishListItemRepository.deleteById(wishListItem.get().getWishListItemId());

        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new InternalException(ResponseMessage.NOT_EXIST_WISH_LIST));

        return getWishlistResponseDtoResponseDto(wishList);
    }
}
