package com.project.healthy_life_was.healthy_life.service.implement;

import com.project.healthy_life_was.healthy_life.common.constant.ResponseMessage;
import com.project.healthy_life_was.healthy_life.dto.ResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.DeliverAddressDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.request.DeliverAddressRequestDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressListResponseDto;
import com.project.healthy_life_was.healthy_life.dto.deliverAddress.response.DeliverAddressResponseDto;
import com.project.healthy_life_was.healthy_life.entity.deliverAddress.DeliverAddress;
import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.repository.DeliverAddressRepository;
import com.project.healthy_life_was.healthy_life.repository.UserRepository;
import com.project.healthy_life_was.healthy_life.service.DeliverAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliverAddressServiceImplement implements DeliverAddressService {
    private final DeliverAddressRepository deliverAddressRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDto<DeliverAddressResponseDto> createAddress(String username, DeliverAddressRequestDto dto) {
        try {

            User user = userRepository.findBUser(username);
            if (user == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

            DeliverAddress deliverAddress = DeliverAddress.builder()
                    .postNum(dto.getPostNum())
                    .address(dto.getAddress())
                    .addressDetail(dto.getAddressDetail())
                    .user(user)
                    .build();

            deliverAddressRepository.save(deliverAddress);

            DeliverAddressDto deliverAddressDto = new DeliverAddressDto(deliverAddress);

            DeliverAddressResponseDto response = new DeliverAddressResponseDto(deliverAddressDto);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS,response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<DeliverAddressListResponseDto> getAddressAll(String username) {
        try {
            List<DeliverAddress> deliverAddressList = deliverAddressRepository.findByUser_Username(username);
            if (deliverAddressList.isEmpty()) {
                throw new IllegalArgumentException(ResponseMessage.NOT_EXIST_DATA + "deliverAddress");
            }

            List<DeliverAddressDto> response = deliverAddressList.stream()
                    .map(DeliverAddressDto::new)
                    .collect(Collectors.toList());

            DeliverAddressListResponseDto data = new DeliverAddressListResponseDto(response);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<DeliverAddressResponseDto> updateAddress(String username, DeliverAddressRequestDto dto, Long deliverAddressId) {
        try {
           DeliverAddress deliverAddress = deliverAddressRepository.findByDeliverAddressId(deliverAddressId);
           if (deliverAddress == null) {
               return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA + "deliverAddress");
           }

           deliverAddress.setAddress(dto.getAddress());
           deliverAddress.setAddressDetail(dto.getAddressDetail());
           deliverAddress.setPostNum(dto.getPostNum());

            deliverAddressRepository.save(deliverAddress);

            DeliverAddressDto deliverAddressDto = new DeliverAddressDto(deliverAddress);

            DeliverAddressResponseDto data = new DeliverAddressResponseDto(deliverAddressDto);

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

    @Override
    public ResponseDto<DeliverAddressResponseDto> getAddressOne(String username, Long deliverAddressId) {
        try {
            DeliverAddress deliverAddress = deliverAddressRepository.findByDeliverAddressId(deliverAddressId);
            if (deliverAddress == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
            DeliverAddressDto deliverAddressDto = new DeliverAddressDto(deliverAddress);

            DeliverAddressResponseDto data = new DeliverAddressResponseDto(deliverAddressDto);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public ResponseDto<DeliverAddressResponseDto> addressIsDefault(String username, Long deliverAddressId) {
        try {
            List<DeliverAddress> addressList = deliverAddressRepository.findByUser_Username(username);
            if (addressList.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

            for (DeliverAddress a : addressList) {
                if (a.isDefault()) {
                    a.setDefault(false);
                    deliverAddressRepository.save(a);
                }
            }

            DeliverAddress deliverAddress = deliverAddressRepository.findByDeliverAddressId(deliverAddressId);
            if (deliverAddress == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }

            deliverAddress.setDefault(true);

            deliverAddressRepository.save(deliverAddress);

            DeliverAddressDto deliverAddressDto = new DeliverAddressDto(deliverAddress);

            DeliverAddressResponseDto data = new DeliverAddressResponseDto(deliverAddressDto);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
}
