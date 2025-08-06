package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.request.DeliverAddressRequestDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressResponseDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.DeliverAddressRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.DeliverAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliverAddressServiceImplement implements DeliverAddressService {
    private final DeliverAddressRepository deliverAddressRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDto<DeliverAddressResponseDto> createAddress(String username, DeliverAddressRequestDto dto) {
        try {

            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

            DeliverAddress deliverAddress = DeliverAddress.builder()
                    .postNum(dto.getPostNum())
                    .address(dto.getAddress())
                    .addressDetail(dto.getAddressDetail())
                    .build();

            deliverAddressRepository.save(deliverAddress);

            DeliverAddressResponseDto response = new DeliverAddressResponseDto(deliverAddress);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS,response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<DeliverAddressResponseDto> getAddressAll(String username) {
        try {
            List<DeliverAddress> deliverAddressList = deliverAddressRepository.findByUser_Username(username);
            if (deliverAddressList.isEmpty()) {
                throw new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "deliverAddress");
            }

            List<DeliverAddressDto> response = deliverAddressList.stream()
                    .map(DeliverAddressDto::new)
                    .collect(Collectors.toList());

            DeliverAddressResponseDto data = new DeliverAddressResponseDto((DeliverAddress) response);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<DeliverAddressResponseDto> updateAddress(String username, DeliverAddressRequestDto dto, Long addressDeliverId) {
        try {
           DeliverAddress deliverAddress = deliverAddressRepository.findByDeliverAddressId(addressDeliverId);
           if (deliverAddress == null) {
               return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "deliverAddress");
           }

           deliverAddress.setAddress(dto.getAddress());
           deliverAddress.setAddressDetail(dto.getAddressDetail());
           deliverAddress.setPostNum(dto.getPostNum());

            deliverAddressRepository.save(deliverAddress);

            DeliverAddressResponseDto data = new DeliverAddressResponseDto(deliverAddress);

           return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<Object> deleteAddress(String username, Long deliverAddressId) {
        try {
            DeliverAddress deliverAddress = deliverAddressRepository.findByDeliverAddressId(deliverAddressId);
            if (deliverAddress == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "deliverAddress");
            }
            deliverAddressRepository.delete(deliverAddress);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
}
