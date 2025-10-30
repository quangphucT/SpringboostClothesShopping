package com.example.demo.service.for_cart;

import com.example.demo.entity.for_account.CustomerProfile;
import com.example.demo.entity.for_order.Cart;
import com.example.demo.entity.for_order.CartItem;
import com.example.demo.entity.for_productVariant.ProductVariant;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.for_cart.CartItemRequest;
import com.example.demo.model.for_cart.CartItemResponse;
import com.example.demo.model.for_cart.CartResponse;
import com.example.demo.repository.for_cart.CartItemRepository;
import com.example.demo.repository.for_cart.CartRepository;
import com.example.demo.repository.for_productVariant.ProductVariantRepository;
import com.example.demo.service.for_authen.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductVariantRepository productVariantRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    AuthenticationService authenticationService;
    public void addProductToCart(CartItemRequest cartItemRequest){

          // kiểm tra productVariant có tồn tại không
        ProductVariant variant = productVariantRepository.findById(cartItemRequest.getProductVariantId())
                .orElseThrow(() -> new NotFoundException("Product Variant not found"));

         // Lấy ra cái cart của thằng customer
        CustomerProfile customerProfile = authenticationService.getCurrentAccount().getCustomerProfile();
            Cart cartOfCustomer = customerProfile.getCart();

        // Kiểm tra  variant đã có trong giỏ hàng chưa
        CartItem cartItem = cartItemRepository.findByCartAndProductVariant(cartOfCustomer, variant);

        if(cartItem != null){
          cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
          cartItemRepository.save(cartItem);
        }else{
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cartOfCustomer);
            newCartItem.setProductVariant(variant);
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            cartItemRepository.save(newCartItem);
        }
    }

    public CartResponse getCartMe(){
        CustomerProfile customerProfile = authenticationService.getCurrentAccount().getCustomerProfile();
        Cart cartOfCustomer = customerProfile.getCart();
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartId(cartOfCustomer.getId());

        List<CartItemResponse> items = cartOfCustomer.getCartItems().stream().map((item) -> {
            CartItemResponse cartItemResponse = new CartItemResponse();
            cartItemResponse.setId(item.getId());
            cartItemResponse.setQuantity(item.getQuantity());
            cartItemResponse.setProductVariantId(item.getProductVariant().getId());
            cartItemResponse.setColorName(item.getProductVariant().getColor().getName());
            cartItemResponse.setSizeName(item.getProductVariant().getSize().getName());
            cartItemResponse.setPrice(item.getProductVariant().getPrice());
            cartItemResponse.setProductName(item.getProductVariant().getProduct().getName());
            cartItemResponse.setProductThumbnail(item.getProductVariant().getProduct().getThumbnail());
return cartItemResponse;
        }).toList();
    cartResponse.setCartItemResponses(items);
    return cartResponse;

    }
}
