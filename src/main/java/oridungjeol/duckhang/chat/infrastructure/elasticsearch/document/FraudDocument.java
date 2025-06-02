package oridungjeol.duckhang.chat.infrastructure.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import oridungjeol.duckhang.chat.application.domain.FraudType;
import oridungjeol.duckhang.chat.application.domain.MessageType;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName="fraud_sentences")
public class FraudDocument {
    @Id
    private String id;

    private FraudType fraud_type;

    private String content;
}
