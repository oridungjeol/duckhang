package oridungjeol.duckhang.chat.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.application.domain.FraudType;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository.ChatESRepositoryNative;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatTestService {

    private final ChatESRepositoryNative chatESRepositoryNative;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ChatTestService(ChatESRepositoryNative chatESRepositoryNative) {
        this.chatESRepositoryNative = chatESRepositoryNative;
    }

    public long checkFraud() throws IOException {
        long start = System.currentTimeMillis();

        String external_keywords = "카카오톡 OR 카톡 OR 텔레그램 OR 오픈채팅 OR 옾챗 OR 010 OR 문자 OR ㅋㅋㅇㅌ OR 전화번호 OR 카톡아이디 OR 톡디";
        String deposit_keywords = "선입금 OR 보증금 OR 페이팔";
        String personal_info_keywords = "주민등록번호 OR 신분증 OR 인증번호 OR 카드번호 OR 비밀번호";

        List<ChatDocument> external_fraud = chatESRepositoryNative.searchFraud(external_keywords);
        List<ChatDocument> deposit_fraud = chatESRepositoryNative.searchFraud(deposit_keywords);
        List<ChatDocument> personal_fraud = chatESRepositoryNative.searchFraud(personal_info_keywords);

        System.out.println("external: " + external_fraud.size());
        System.out.println("deposit: " + deposit_fraud.size());
        System.out.println("personal: " + personal_fraud.size());

        long end = System.currentTimeMillis();
        System.out.println("running time: " + (end - start) + "ms");

        return end - start;
    }

}
