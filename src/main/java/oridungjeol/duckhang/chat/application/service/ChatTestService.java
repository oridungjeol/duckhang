package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.application.domain.FraudType;
import oridungjeol.duckhang.chat.application.dto.FraudDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatTestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<String> checkFraud() throws IOException {
        String[] external_keywords = {"카카오톡", "카톡", "텔레그램", "오픈채팅", "옾챗", "010", "문자", "ㅋㅋㅇㅌ",
                "전화번호", "카톡아이디", "톡디"};
        String[] deposit_keywords = {"선입금", "보증금", "페이팔"};
        String[] personal_info_keywords = {"주민등록번호", "신분증", "인증번호", "카드번호", "비밀번호"};

        List<String> alert_list = new ArrayList<>();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("fraud_dataset.json");
        ObjectMapper mapper = new ObjectMapper();
        FraudDataSet dataset = mapper.readValue(inputStream, new TypeReference<>() {});

        boolean flag1 = false;
        for (String sentence : dataset.getFraud()) {
            for (int i = 0; i < external_keywords.length; i++) {
                if (sentence.contains(external_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.EXTERNAL));
                    System.out.println("Fraud,Fraud,TP"); //실제로 사기이고 사기라고 잘 판단함
                    flag1 = true;
                    break;
                }
            }

            for (int i = 0; i < deposit_keywords.length; i++) {
                if (sentence.contains(deposit_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.DEPOSIT));
                    System.out.println("Fraud,Fraud,TP"); //실제로 사기이고 사기라고 잘 판단함
                    flag1 = true;
                    break;
                }
            }

            for (int i = 0; i < personal_info_keywords.length; i++) {
                if (sentence.contains(personal_info_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.PERSONAL_INFO));
                    System.out.println("Fraud,Fraud,TP"); //실제로 사기이고 사기라고 잘 판단함
                    flag1 = true;
                    break;
                }
            }
            if (!flag1) {
                System.out.println("Fraud,Normal,FN"); //사기이지만 사기가 아니라고 잘못 판단함
            }
        }

        boolean flag2 = false;
        for (String sentence : dataset.getSimilar()) {
            for (int i = 0; i < deposit_keywords.length; i++) {
                if (sentence.contains(deposit_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.DEPOSIT));
                    System.out.println("Similar,Fraud,TP"); //실제로 사기이고 사기라고 잘 판단함
                    flag2 = true;
                    break;
                }
            }

            for (int i = 0; i < deposit_keywords.length; i++) {
                if (sentence.contains(deposit_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.DEPOSIT));
                    System.out.println("Similar,Fraud,TP"); //실제로 사기이고 사기라고 잘 판단함
                    flag2 = true;
                    break;
                }
            }

            for (int i = 0; i < personal_info_keywords.length; i++) {
                if (sentence.contains(personal_info_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.PERSONAL_INFO));
                    System.out.println("Similar,Fraud,TP"); //실제로 사기이고 사기라고 잘 판단함
                    flag2 = true;
                    break;
                }
            }

            if (!flag2) {
                System.out.println("Similar,Normal,FN"); //사기이지만 사기가 아니라고 잘못 판단함
            }
        }

        boolean flag3 = false;
        for (String sentence : dataset.getNormal()) {
            for (int i = 0; i < personal_info_keywords.length; i++) {
                if (sentence.contains(personal_info_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.PERSONAL_INFO));
                    System.out.println("Normal,Fraud,FP"); //사기가 아니지만 사기라고 잘못 판단함
                    flag3 = true;
                    break;
                }
            }

            for (int i = 0; i < deposit_keywords.length; i++) {
                if (sentence.contains(deposit_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.DEPOSIT));
                    System.out.println("Normal,Fraud,FP"); //사기가 아니지만 사기라고 잘못 판단함
                    flag3 = true;
                    break;
                }
            }

            for (int i = 0; i < personal_info_keywords.length; i++) {
                if (sentence.contains(personal_info_keywords[i])) {
                    alert_list.add(String.valueOf(FraudType.PERSONAL_INFO));
                    System.out.println("Normal,Fraud,FP"); //사기가 아니지만 사기라고 잘못 판단함
                    flag3 = true;
                    break;
                }
            }

            if (!flag3) {
                System.out.println("Normal,Normal,TN"); //실제로 사기가 아니고 사기가 아니라고 잘 판단함
            }
        }

        System.out.println(alert_list);
        return alert_list;
    }

}
