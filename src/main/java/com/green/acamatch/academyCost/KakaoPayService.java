package com.green.acamatch.academyCost;

import com.green.acamatch.academy.PremiumRepository;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.premium.model.PremiumPostReq;
import com.green.acamatch.academyCost.model.GetAcademyCostInfoRes;
import com.green.acamatch.academyCost.model.KakaoApproveResponse;
import com.green.acamatch.academyCost.model.KakaoPayPostReq;
import com.green.acamatch.academyCost.model.KakaoReadyResponse;
import com.green.acamatch.book.BookRepository;
import com.green.acamatch.entity.academy.PremiumAcademy;
import com.green.acamatch.entity.academyCost.AcademyCost;
import com.green.acamatch.entity.academyCost.Book;
import com.green.acamatch.entity.academyCost.Product;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.joinClass.model.JoinClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    /**
     * 결제 완료 요청
     */
    @Transactional
    public KakaoReadyResponse kakaoPayReady(KakaoPayPostReq req) {


        Map<String, Object> parameters = new HashMap<>();

        Optional<Product> product = productRepository.findById(req.getProductId());

        parameters.put("cid", payProperties.getCid());
        parameters.put("partner_order_id", req.getProductId()); // 실제 주문 번호로 교체
        parameters.put("partner_user_id", req.getUserId());   // 실제 사용자 ID로 교체
        parameters.put("item_name", product.get().getProductName());       // 실제 상품명으로 교체
        parameters.put("quantity", req.getQuantity());                 // 수량, 숫자는 문자열로 전달
        parameters.put("total_amount", (product.get().getProductPrice() + (product.get().getProductPrice()/10)) * req.getQuantity());          // 총 금액, 숫자는 문자열로 전달
        parameters.put("vat_amount", ((product.get().getProductPrice()/10)) * req.getQuantity());             // 부가세, 숫자는 문자열로 전달
        parameters.put("tax_free_amount", "0");          // 비과세 금액, 숫자는 문자열로 전달
        parameters.put("approval_url", "http://localhost:8080/success");
        parameters.put("fail_url", "http://localhost:8080/fail");
        parameters.put("cancel_url", "http://localhost:8080/cancel");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());


        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoReadyResponse kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);

        AcademyCost academyCost = new AcademyCost();
        long productId = academyCostMapper.getBookIdByProductId(req.getProductId());
        Book book = bookRepository.findById(productId).orElse(null);
        book.getBookAmount();

        Optional<Product> product2 = productRepository.findById(productId);
        academyCost.setAmount(req.getQuantity());
        if(product2.get().getBookId() != null) {
            if((book.getBookAmount()-academyCost.getAmount()) < 0){
                academyCostMessage.setMessage("남은 수량보다 많이 구매하였습니다. 구매가 불가능합니다.");
                return null;
            }
        }


        academyCost.setUserId(req.getUserId());
        academyCost.setPrice((product.get().getProductPrice() + (product.get().getProductPrice()/10)) * req.getQuantity());
        academyCost.setStatus(0);
        if(academyCost.getAcademyId() != null){
            academyCost.setOrderType(0);
        }
        academyCost.setOrderType(1);
        academyCost.setCost_status(0);
        academyCost.setProductId(req.getProductId());
        if (req.getJoinClassId() != 0) {
            JoinClass joinClass = joinClassRepository.findById(req.getJoinClassId()).orElse(null);
            academyCost.setJoinClass(joinClass);
        }
        if(req.getProductId() == 1){
            academyCost.setAcademyId(req.getAcaId());
        }
        String tid = kakaoReady.getTid();
        academyCost.setTId(tid);
        academyCost.setFee(academyCost.getPrice() * 0.01);
        academyCostRepository.save(academyCost);

        academyCostMessage.setMessage("결제 시작~");
        return kakaoReady;
    }

    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse approveResponse (String pgToken, String TId){

        // 카카오 요청
        GetAcademyCostInfoRes result = academyCostMapper.getInfoByTid(TId);
        String tid = result.getTId();
        long userId = result.getUserId();
        long productId = result.getProductId();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", payProperties.getCid());
        parameters.put("tid", tid);
        parameters.put("partner_order_id", String.format("%d", productId));
        parameters.put("partner_user_id", String.format("%s", userId));
        parameters.put("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        System.out.println();
        System.out.println();
        System.out.println(requestEntity);
        System.out.println();
        System.out.println();

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(approveResponse);
        System.out.println();
        System.out.println();
        System.out.println();

        //academyCostRepository.updateCostStatus(1, TId);
        AcademyCost acaResult = academyCostRepository.findById(result.getCostId()).orElse(null);
        acaResult.setCost_status(1);
        academyCostRepository.save(acaResult);
        if(acaResult.getAcademyId() != null){
            PremiumAcademy premiumAcademy = new PremiumAcademy();
            premiumAcademy.setAcademy(acaResult.getAcademyId());
            premiumAcademy.setPreCheck(0);
            premiumAcademy.setPrice(acaResult.getPrice());
            premiumRepository.save(premiumAcademy);
        }

        Product product = productRepository.findById(productId).orElse(null);

        if(product.getBookId() != null){
            Book book = bookRepository.findById(academyCostMapper.getBookIdByProductId(productId)).orElse(null);
            book.setBookAmount(book.getBookAmount() - acaResult.getAmount());
            bookRepository.save(book);
        }


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