package com.green.acamatch.academy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAcademyListRes {
    private long acaId;
    private String acaPic;
    private String acaName;
    private String address;
    private double star;
    private AddressDto addressDto;
    private List<GetAcademyTagDto> tagName;
    private Integer reviewCount;     // 리뷰 개수
    private String comment;          // 리뷰 코멘트
    private Double reviewStar;       // 리뷰 별점
    private String createdAt;        // 리뷰 작성일
    private String nickName;         // 리뷰 작성자 닉네임
    private List<String> classInfo;  // 클래스 정보 리스트
    private Integer likeCount;       // 좋아요 개수
    private List<String> categoryTypes; // 카테고리 유형 리스트
    private List<String> categories;    // 카테고리 이름 리스트
}
