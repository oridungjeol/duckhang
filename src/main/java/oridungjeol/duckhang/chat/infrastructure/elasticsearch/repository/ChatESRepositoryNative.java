package oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository;

import co.elastic.clients.util.Pair;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.application.domain.FraudType;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.FraudDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ChatESRepositoryNative {
    private final ElasticsearchOperations elasticsearchOperations;

    public ChatESRepositoryNative(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    /**
     * ES에 저장된 사기 문장을 기반으로 문장 유사성 체크
     * @param content 검사할 문장
     * @return 유사한 문장 객체, 유사도 score
     * @throws IOException
     */
    public List<Pair<FraudDocument, Float>> searchFraud(String content) throws IOException {
        List<String> field = new ArrayList<>();
        field.add("content");

        NativeQuery fraudQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .moreLikeThis(m -> m
                                .fields("content")
                                .like(l -> l.text(content))
                                .minTermFreq(1)
                                .minDocFreq(1)
                                .minimumShouldMatch("30%")
                        )
                )
                .build();

        List<Pair<FraudDocument, Float>> results = elasticsearchOperations
                .search(fraudQuery, FraudDocument.class)
                .stream()
                .map(hit -> Pair.of(hit.getContent(), hit.getScore()))
                .collect(Collectors.toList());

        return results;
    }
}
