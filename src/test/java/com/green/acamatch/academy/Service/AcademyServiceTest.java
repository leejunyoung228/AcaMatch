package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.model.AddressDto;
import com.green.acamatch.config.constant.AddressConst;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AcademyServiceTest {
    @InjectMocks
    private AcademyService academyService;


    @Test
    void address() {
        AddressDto addressDto = new AddressDto();
        addressDto.setAddress("대구 중구 중앙대로 394");
        addressDto.setDetailAddress("제일빌딩 B1");
        addressDto.setPostNum("41937");
        String encodedAddress = academyService.addressEncoding(addressDto);
        System.out.println(encodedAddress);
        AddressDto decodedAddressDto = academyService.addressDecoding(encodedAddress);
        System.out.println(decodedAddressDto);
    }
}