package com.green.acamatch.academyCost;

import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/academyCost")
@RequiredArgsConstructor
public class academyCostController {
    private final academyCostService academyCostService;

    @PostMapping("/kakaopay")
    @Operation(summary = "카카오페이 결제")
    public ResultResponse<String> kakaopay(InputStream inputStream){
        try {
            URL address = new URL("https://open-api.kakaopay.com/online/v1/payment/ready ");
            HttpURLConnection sc = (HttpURLConnection) address.openConnection();
            sc.setRequestMethod("POST");
            sc.setRequestProperty("Authorization", "SECRET_KEY DEVA2DDFFD58F667756DECD347E6F4C82F43713E");
            sc.setRequestProperty("Content-Type", "application/json");
            sc.setDoOutput(true);
            String parameter = "cid=TC0ONETIME&partner_order_id=partner_order_id&partner_user_id=partner_user_id&item_name=초코파이&quantity=1&total_amount=2200&tax_free_amount=200&approval_url=https://localhost:8080/success&cancel_url=https://localhost:8080/cancel&fail_url=https://localhost:8080/fail";
            OutputStream os = sc.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeBytes(parameter);
            dos.flush();
            dos.close();

            int result = sc.getResponseCode();

            InputStream is;
            if(result == 200){
                is = sc.getInputStream();
            }else {
                is = sc.getErrorStream();
            }
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            return ResultResponse.<String>builder()
                    .resultData(br.readLine())
                    .build();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
