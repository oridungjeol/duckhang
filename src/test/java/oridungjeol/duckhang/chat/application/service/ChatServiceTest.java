package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import oridungjeol.duckhang.chat.application.domain.FraudType;
import oridungjeol.duckhang.entity.ChatDataset;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Test
    void filterFraud() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("fraud_dataset.json");

        ChatDataset dataset = objectMapper.readValue(resource.getInputStream(), ChatDataset.class);

        List<String> fraudList = dataset.getFraud();
        List<String> similarList = dataset.getSimilar();
        List<String> normalList = dataset.getNormal();

        for (String fraud : fraudList) {
            FraudType fraudType = chatService.filterFraud(fraud);
            if (fraudType == FraudType.NOT_FRAUD) {
                System.out.println("Fraud,Normal,Incorrect");
            }
            else {
                System.out.println("Fraud,Fraud,Correct");
            }
        }

        for (String similar : similarList) {
            FraudType fraudType = chatService.filterFraud(similar);
            if (fraudType == FraudType.NOT_FRAUD) {
                System.out.println("Similar,Normal,Incorrect");
            }
            else {
                System.out.println("Similar,Fraud,Correct");
            }
        }

        for (String normal : normalList) {
            FraudType fraudType = chatService.filterFraud(normal);
            if (fraudType == FraudType.NOT_FRAUD) {
                System.out.println("Normal,Normal,Correct");
            }
            else {
                System.out.println("Normal,Fraud,Incorrect");
            }
        }
    }
}