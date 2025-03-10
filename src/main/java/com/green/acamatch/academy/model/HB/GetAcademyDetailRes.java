package com.green.acamatch.academy.model.HB;

import com.green.acamatch.academy.model.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class GetAcademyDetailRes {
    @Schema(title = "학원 PK", example = "1")
    private Long acaId;

    @Schema(title = "학원 이름", example = "그린 컴퓨터 학원")
    private String acaName;

    @Schema(title = "학원 사진", example = "https://example.com/image.jpg")
    private String acaPic;

    @Schema(title = "학원 주소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(title = "상세 주소", example = "2층 203호")
    private String detailAddress;

    @Schema(title = "우편 번호", example = "06123")
    private String postNum;

    @Schema(title = "학원 전화번호", example = "02-1234-5678")
    private String acaPhone;

    @Schema(title = "강사 수", example = "10")
    private Integer teacherNum;

    @Schema(title = "학원 설명", example = "소프트웨어 개발 전문 학원입니다.")
    private String comments;

    @Schema(title = "별점", example = "4.5")
    private Double star;

    @Schema(title = "총 리뷰 수", example = "150")
    private Integer reviewCount;

    @Schema(title = "좋아요 수", example = "120")
    private Integer likeCount;

    @Schema(title = "로그인된 사용자가 좋아요를 했는지 여부", example = "true")
    private Boolean isLiked;

    @Schema(title = "모든 주소 정보")
    private AddressDto addressDto;

    @Schema(title = "학원 태그 목록", example = "[\"IT\", \"프로그래밍\", \"자격증\"]")
    private List<String> tagName;

    @Schema(title = "학원 개설 클래스 정보")
    private List<GetAcademyDetailClassDto> classes;

    @Schema(title = "일반 리뷰 개수", example = "100")
    private Integer generalReviewCount;

    @Schema(title = "미디어 포함 리뷰 개수", example = "50")
    private Integer mediaReviewCount;

    @Schema(title = "일반 리뷰 (미디어 없는 리뷰)")
    private List<GeneralReviewDto> generalReviews;

    @Schema(title = "미디어 포함 리뷰 (이미지가 포함된 리뷰)")
    private List<MediaReviewDto> mediaReviews;

//    @Schema(title = "전체 리뷰 리스트 (일반 리뷰 + 미디어 포함 리뷰)")
//    private List<ReviewDto> reviews;

    @Schema(title = "학원에서 사용하는 책 목록")
    private List<BookDto> books;

}

