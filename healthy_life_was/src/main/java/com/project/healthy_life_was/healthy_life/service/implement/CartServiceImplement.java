package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.CartItemDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.CartAddRequestDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.CartUpdateQuantityRequestDto;
import com.project.healthy_life_was.healthy_life.dto.cart.request.DeleteCartItemsDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartAddResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartDetailResponseDto;
import com.project.healthy_life_was.healthy_life.dto.cart.response.CartUpdateResponseDto;
import com.project.healthy_life_was.healthy_life.entity.cart.Cart;
import com.project.healthy_life_was.healthy_life.entity.cart.CartItem;
import com.project.healthy_life_was.healthy_life.entity.product.Product;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.CartItemRepository;
import com.project.healthy_life_was.healthy_life.repository.CartRepository;
import com.project.healthy_life_was.healthy_life.repository.ProductRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImplement implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ResponseDto<CartAddResponseDto> createCart(String username, Long pId, CartAddRequestDto dto) {
        CartAddResponseDto data = null;
        int quantity = dto.getProductQuantity();
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "username"));
            Product product = productRepository.findById(pId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "pId"));

            if (quantity > product.getPStockStatus()) {
                return ResponseDto.setFailed(ResponseMessage.PURCHASE_INVENTORY); // 재고 부족 처리
            }

            Cart cart = cartRepository.findByUser(user)
                    .orElseGet(() -> {
                        Cart newCart = Cart.builder().user(user).build();
                        return cartRepository.save(newCart);
                    });

            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart,product);

            if(existingCartItem.isPresent()){
                CartItem cartItem = existingCartItem.get();
                cartItem.setProductQuantity(quantity + cartItem.getProductQuantity());
                cartItem.setProductPrice(cartItem.getProduct().getPPrice());
                cartItemRepository.save(cartItem);
                data = new CartAddResponseDto(cartItem);
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
            } else {
                CartItem cartItem = CartItem.builder()
                        .product(product)
                        .cart(cart)
                        .productQuantity(quantity)
                        .productPrice(product.getPPrice())
                        .build();
                cartItemRepository.save(cartItem);
                data = new CartAddResponseDto(cartItem);
                return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<CartDetailResponseDto> getCartUser(String username) {
        try {
            List<CartItem> cartItems = cartItemRepository.findByCart_User_Username(username);
            List<CartItemDto> cartItemDto = cartItems.stream()
                    .map(cart -> new CartItemDto(
                            cart.getCartItemId(),
                            cart.getProduct().getPId(),
                            cart.getProduct().getPName(),
                            cart.getProductQuantity(),
                            cart.getProductPrice(),
                            cart.getProduct().getPImgUrl()
                    ))
                    .toList();
            CartDetailResponseDto cartDetailResponseDto = new CartDetailResponseDto(cartItemDto);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, cartDetailResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
    @Override
    public ResponseDto<CartUpdateResponseDto> updateCart(String username, Long cartItemId, CartUpdateQuantityRequestDto dto) {
        CartUpdateResponseDto data = null;
        int quantity = dto.getProductQuantity();
        try {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "cartItem"));

            if (quantity > cartItem.getProduct().getPStockStatus()) {
                return ResponseDto.setFailed(ResponseMessage.PURCHASE_INVENTORY);
            }
            if(!cartItem.getCart().getUser().getUsername().equals(username)){
                return ResponseDto.setFailed(ResponseMessage.NO_PERMISSION);
            }
            cartItem.setProductQuantity(quantity);
            cartItem.setProductPrice(cartItem.getProduct().getPPrice());
            cartItemRepository.save(cartItem);
            data = new CartUpdateResponseDto(cartItem);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<Object> deleteCartItemIds(String username, DeleteCartItemsDto dto) {
        List<Long> cartItemIds = dto.getCartItemIds();
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "cart items");
        }
        try {
            List<CartItem> cartItems = cartItemRepository.findAllById(cartItemIds);
            if (cartItems.isEmpty()) {
                throw new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "cartitems");
            }
            if (cartItems.size() != cartItemIds.size()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "some cart items not found");
            }
            cartItemRepository.deleteAll(cartItems);
        } catch (Exception e) {
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }

    @Override
    public ResponseDto<Object> deleteCartAll(String username) {
        try {
            Cart cart = cartRepository.findByUser_Username(username)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA));

            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            cartItemRepository.deleteAll(cartItems);
            cartRepository.delete(cart);
        } catch (Exception e) {
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}
