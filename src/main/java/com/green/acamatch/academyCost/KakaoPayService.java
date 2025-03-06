package com.green.acamatch.academyCost;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.premium.model.PremiumPostReq;
import com.green.acamatch.academyCost.model.*;
import com.green.acamatch.book.BookRepository;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.academyCost.AcademyCost;
import com.green.acamatch.entity.academyCost.Book;
import com.green.acamatch.entity.academyCost.Product;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.joinClass.JoinClassRepository;
import com.green.acamatch.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
    private final AcademyCostRepository academyCostRepository;
    private final JoinClassRepository joinClassRepository;
    private final KakaoPayProperties payProperties;
    private final ProductRepository productRepository;
    private final PremiumRepository premiumRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private final AcademyCostMapper academyCostMapper;
    private final PremiumService premiumService;
    private final AcademyCostMessage academyCostMessage;


    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + payProperties.getSecretKey();
        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    public String getBaseUrl(HttpServletRequest request, String urlType) {
        String scheme = request.getScheme(); // http 또는 https
        String serverName = request.getServerName(); // localhost
        int serverPort = request.getServerPort(); // 요청받은 포트

        String baseUrl = scheme + "://" + serverName + ":" + serverPort;

        // URL 타입에 맞는 경로를 붙여줍니다.
        if ("success".equals(urlType)) {
            return baseUrl + "/success";
        } else if ("fail".equals(urlType)) {
            return baseUrl + "/fail";
        } else if ("cancel".equals(urlType)) {
            return baseUrl + "/cancel";
        }

        return baseUrl; // 기본 URL 반환
    }
//
//    /**
//     * 결제 완료 요청
//     */
//    @Transactional
//    public KakaoReadyResponse kakaoPayReady(KakaoPayPostReq req) {
//
//
//        Map<String, Object> parameters = new HashMap<>();
//
//        Optional<Product> product = productRepository.findById(req.getProductId());
//
//        if (!product.isPresent()) {
//            throw new RuntimeException("상품을 찾을 수 없습니다.");  // 상품이 없을 경우 처리
//        }
//
//        parameters.put("cid", payProperties.getCid());
//        parameters.put("partner_order_id", req.getProductId()); // 실제 주문 번호로 교체
//        parameters.put("partner_user_id", req.getUserId());   // 실제 사용자 ID로 교체
//        parameters.put("item_name", product.get().getProductName());       // 실제 상품명으로 교체
//        parameters.put("quantity", req.getQuantity());                 // 수량, 숫자는 문자열로 전달
//        parameters.put("total_amount", (product.get().getProductPrice() + (product.get().getProductPrice()/10)) * req.getQuantity());          // 총 금액, 숫자는 문자열로 전달
//        parameters.put("vat_amount", ((product.get().getProductPrice()/10)) * req.getQuantity());             // 부가세, 숫자는 문자열로 전달
//        parameters.put("tax_free_amount", "0");          // 비과세 금액, 숫자는 문자열로 전달
//        parameters.put("approval_url", "http://localhost:8080/success");
//        parameters.put("fail_url", "http://localhost:8080/fail");
//        parameters.put("cancel_url", "http://localhost:8080/cancel");
//
//        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//
//
//        // 외부에 보낼 url
//        RestTemplate restTemplate = new RestTemplate();
//
//        KakaoReadyResponse kakaoReady = restTemplate.postForObject(
//                "https://open-api.kakaopay.com/online/v1/payment/ready",
//                requestEntity,
//                KakaoReadyResponse.class);
//
//        AcademyCost academyCost = new AcademyCost();
//
//        if(product.get().getBookId() != null){
//            long productId = academyCostMapper.getBookIdByProductId(req.getProductId());
//            Book book = bookRepository.findById(productId).orElse(null);
//            book.getBookAmount();
//            Optional<Product> product2 = productRepository.findById(productId);
//            academyCost.setAmount(req.getQuantity());
//            if(product2.get().getBookId() != null) {
//                if((book.getBookAmount()-academyCost.getAmount()) < 0){
//                    academyCostMessage.setMessage("남은 수량보다 많이 구매하였습니다. 구매가 불가능합니다.");
//                    return null;
//                }
//            }
//        }
//
//        academyCost.setProductId(req.getProductId());
//        academyCost.setUserId(req.getUserId());
//        academyCost.setPrice((product.get().getProductPrice() + (product.get().getProductPrice()/10)) * req.getQuantity());
//        academyCost.setStatus(0);
//        if(academyCost.getAcademyId() != null){
//            academyCost.setOrderType(0);
//        }
//        academyCost.setOrderType(1);
//        academyCost.setCost_status(0);
//        academyCost.setProductId(req.getProductId());
//        if (req.getJoinClassId() != 0) {
//            JoinClass joinClass = joinClassRepository.findById(req.getJoinClassId()).orElse(null);
//            academyCost.setJoinClass(joinClass);
//        }
//        if(req.getProductId() == 1){
//            academyCost.setAcademyId(req.getAcaId());
//        }
//        String tid = kakaoReady.getTid();
//        academyCost.setTId(tid);
//        academyCost.setFee(academyCost.getPrice() * 0.01);
//        academyCostRepository.save(academyCost);
//
//        academyCostMessage.setMessage("결제 시작~");
//        return kakaoReady;
//    }
//
//    /**
//     * 결제 완료 승인
//     */
//    public KakaoApproveResponse approveResponse (String pgToken, String TId){
//
//        // 카카오 요청
//        GetAcademyCostInfoRes result = academyCostMapper.getInfoByTid(TId);
//        String tid = result.getTId();
//        long userId = result.getUserId();
//        long productId = result.getProductId();
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("cid", payProperties.getCid());
//        parameters.put("tid", tid);
//        parameters.put("partner_order_id", String.format("%d", productId));
//        parameters.put("partner_user_id", String.format("%s", userId));
//        parameters.put("pg_token", pgToken);
//
//        // 파라미터, 헤더
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//        System.out.println();
//        System.out.println();
//        System.out.println(requestEntity);
//        System.out.println();
//        System.out.println();
//
//        // 외부에 보낼 url
//        RestTemplate restTemplate = new RestTemplate();
//
//        KakaoApproveResponse approveResponse = restTemplate.postForObject(
//                "https://open-api.kakaopay.com/online/v1/payment/approve",
//                requestEntity,
//                KakaoApproveResponse.class);
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(approveResponse);
//        System.out.println();
//        System.out.println();
//        System.out.println();
//
//        //academyCostRepository.updateCostStatus(1, TId);
//        AcademyCost acaResult = academyCostRepository.findById(result.getCostId()).orElse(null);
//        acaResult.setCost_status(2);
//        academyCostRepository.save(acaResult);
//        if(acaResult.getAcademyId() != null){
//            PremiumAcademy premiumAcademy = new PremiumAcademy();
//            premiumAcademy.setAcademy(acaResult.getAcademyId());
//            premiumAcademy.setPreCheck(0);
//            premiumAcademy.setPrice(acaResult.getPrice());
//            premiumRepository.save(premiumAcademy);
//        }
//
//        Product product = productRepository.findById(productId).orElse(null);
//
//
//        if(product.getBookId() != null){
//            Book book = bookRepository.findById(academyCostMapper.getBookIdByProductId(productId)).orElse(null);
//            book.setBookAmount(book.getBookAmount() - acaResult.getAmount());
//            bookRepository.save(book);
//        }
//
//
//
//        return approveResponse;
//    }

    public KakaoReadyResponse kakaoPayReady(HttpServletRequest request, KakaoPayPostReq req) {
        Map<String, Object> parameters = new HashMap<>();
        List<ProductRequest> products = req.getProducts(); // 상품 ID와 수량 리스트 가져오기

        if (products == null || products.isEmpty()) {
            throw new RuntimeException("상품 목록이 비어 있습니다.");
        }

        int totalAmount = 0;
        int totalVatAmount = 0;
        StringBuilder itemNames = new StringBuilder();

        for (ProductRequest productReq : products) {
            Long productId = productReq.getProductId();
            int quantity = productReq.getQuantity();

            Optional<Product> productOpt = productRepository.findById(productId);
            if (!productOpt.isPresent()) {
                throw new RuntimeException("상품을 찾을 수 없습니다: ID=" + productId);
            }

            Product product = productOpt.get();
            int productPrice = product.getProductPrice();
            int vatAmount = (productPrice / 10) * quantity;

            totalAmount += (productPrice + vatAmount) * quantity;
            totalVatAmount += vatAmount;

            if (itemNames.length() > 0) {
                itemNames.append(", ");
            }
            itemNames.append(product.getProductName()).append(" x").append(quantity);
        }

        parameters.put("cid", payProperties.getCid());
        String partnerOrderId = UUID.randomUUID().toString();
        parameters.put("partner_order_id", partnerOrderId);
        parameters.put("partner_user_id", req.getUserId());
        parameters.put("item_name", itemNames.toString());
        parameters.put("quantity", products.size()); // 상품 종류 개수
        parameters.put("total_amount", totalAmount);
        parameters.put("vat_amount", totalVatAmount);
        parameters.put("tax_free_amount", "0");
//        List<Integer> allowedPorts = List.of(8080, 5173, 4173);
//        String successUrl = getBaseUrl(request, "success");
//        String failUrl = getBaseUrl(request, "fail");
//        String cancelUrl = getBaseUrl(request, "cancel");
//        parameters.put("approval_url", successUrl);
//        parameters.put("fail_url", failUrl);
//        parameters.put("cancel_url", cancelUrl);
        parameters.put("approval_url", "http://localhost:5173/success");
        parameters.put("fail_url",  "http://localhost:5173/fail");
        parameters.put("cancel_url", "http://localhost:5173/cancel");


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        KakaoReadyResponse kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        for (ProductRequest productReq : products) {
            Long productId = productReq.getProductId();
            int quantity = productReq.getQuantity();
            Product product = productRepository.findById(productId).get();

            AcademyCost academyCost = new AcademyCost();
            academyCost.setProductId(product);
            academyCost.setUserId(req.getUserId());
            academyCost.setAmount(quantity);
            academyCost.setPrice((product.getProductPrice() + (product.getProductPrice() / 10)) * quantity);
            academyCost.setPartnerOrderId(partnerOrderId);
            academyCost.setStatus(0);
            if(req.getAcaId() != null) {
                academyCost.setOrderType(2);
            }
            if(req.getAcaId() == null){
                if(product.getBookId() != null){
                    academyCost.setOrderType(1);
                    //academyCost.setAcademyId();

                }
                if(product.getClassId() != null){
                    academyCost.setOrderType(0);
                }
            }
            academyCost.setCost_status(0);
            academyCost.setTId(kakaoReady.getTid());
            academyCost.setFee(academyCost.getPrice() * 0.01);
            if(req.getAcaId() != null){
                Academy academy = academyRepository.findById(req.getAcaId()).orElse(null);
                academyCost.setAcademyId(academy);
            }
            academyCostRepository.save(academyCost);
        }

        academyCostMessage.setMessage("결제 시작~");
        return kakaoReady;
    }



    public KakaoApproveResponse approveResponse(String pgToken, String TId) {
        GetAcademyCostInfoRes result = academyCostMapper.getInfoByTid(TId);
        String tid = result.getTId();
        long userId = result.getUserId();
        String partnerOrderId = result.getPartnerOrderId();  // 저장된 주문번호 가져오기

        List<AcademyCost> costs = academyCostRepository.findByTId(TId);
        for (AcademyCost cost : costs) {
            cost.setCost_status(2);
            if (cost.getProductId() != null && cost.getProductId().getProductId() == 1) {
                cost.setStatus(1);
            }
            academyCostRepository.save(cost);

            if(cost.getAcademyId() != null){
                PremiumAcademy premiumAcademy = new PremiumAcademy();
                premiumAcademy.setAcademy(cost.getAcademyId());
                premiumAcademy.setPreCheck(0);
                premiumAcademy.setPrice(cost.getPrice());
                premiumRepository.save(premiumAcademy);
            }

            Product product = productRepository.findById(cost.getProductId().getProductId()).orElse(null);
            if (product != null && product.getBookId() != null) {
                Book book = bookRepository.findById(product.getBookId().getBookId()).orElse(null);
                if (book != null) {
                    book.setBookAmount(book.getBookAmount() - cost.getAmount());
                    bookRepository.save(book);
                }
            }

            if(product.getClassId() != null){
                JoinClass joinClass = new JoinClass();
                joinClass.setAcaClass(product.getClassId());
                User user = userRepository.findByUserId(userId).orElse(null);
                joinClass.setUser(user);
                joinClass.setRegistrationDate(LocalDate.now());
                joinClass.setCertification(0);
                joinClassRepository.save(joinClass);
            }
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", tid);
        parameters.put("partner_order_id", partnerOrderId);  // 올바른 주문번호 사용
        parameters.put("partner_user_id", String.valueOf(userId));
        parameters.put("pg_token", pgToken);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);

        // 결제 완료 후 상태 변경 로직 유지
        return approveResponse;
    }



    public List<AcademyCost> getRecentPayments() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);  // 한 달 전 날짜 계산
        return academyCostRepository.findRecentPayments(oneMonthAgo);
    }

    public List<AcademyCost> getRecentPaymentsByUserId(Long userId) {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);  // 한 달 전 날짜 계산
        return academyCostRepository.findRecentPaymentsByUserId(userId, oneMonthAgo);
    }
}