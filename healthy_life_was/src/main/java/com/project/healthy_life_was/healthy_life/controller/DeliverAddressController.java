package com.project.healthy_life_was.healthy_life.controller;

import com.project.healthy_life_was.healthy_life.common.constant.ApiMappingPattern;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.request.DeliverAddressRequestDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressListResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressResponseDto;
import com.project.healthy_life_was.healthy_life.service.DeliverAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.DELIVER_ADDRESS)
@RequiredArgsConstructor
public class DeliverAddressController {
    private final DeliverAddressService deliverAddressService;

    private final String DELIVER_ADDRESS_GET = "/all";
    private final String DELIVER_ADDRESS_GET_ONE = "/address/{deliverAddressId}";
    private final String DELIVER_ADDRESS_PUT = "/{deliverAddressId}";
    private final String DELIVER_ADDRESS_IS_DEFAULT_PUT = "/is-default/{deliverAddressId}";
    private final String DELIVER_ADDRESS_DELETE = "/delete/{deliverAddressId}";

    @PostMapping
    public ResponseEntity<ResponseDto<DeliverAddressResponseDto>> createAddress (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DeliverAddressRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<DeliverAddressResponseDto> response = deliverAddressService.createAddress(username, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(DELIVER_ADDRESS_GET)
    public ResponseEntity<ResponseDto<DeliverAddressListResponseDto>> getAddressAll (
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<DeliverAddressListResponseDto> response = deliverAddressService.getAddressAll(username);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(DELIVER_ADDRESS_GET_ONE)
    public ResponseEntity<ResponseDto<DeliverAddressResponseDto>> getAddressOne (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long deliverAddressId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<DeliverAddressResponseDto> response = deliverAddressService.getAddressOne(username, deliverAddressId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
    @PutMapping(DELIVER_ADDRESS_IS_DEFAULT_PUT)
    public ResponseEntity<ResponseDto<DeliverAddressResponseDto>> addressIsDefault (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long deliverAddressId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<DeliverAddressResponseDto> response = deliverAddressService.addressIsDefault(username, deliverAddressId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping(DELIVER_ADDRESS_PUT)
    public ResponseEntity<ResponseDto<DeliverAddressResponseDto>> updateAddress (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long deliverAddressId,
            @RequestBody DeliverAddressRequestDto dto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<DeliverAddressResponseDto> response = deliverAddressService.updateAddress(username, dto, deliverAddressId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(DELIVER_ADDRESS_DELETE)
    public ResponseEntity<ResponseDto<Object>> deleteAddress (
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long deliverAddressId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = userDetails.getUsername();
        ResponseDto<Object> response = deliverAddressService.deleteAddress(username, deliverAddressId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }


}
