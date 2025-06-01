package oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;

import java.io.IOException;
import java.util.List;

@Repository
public class ChatESRepositoryNative {
    private final ElasticsearchOperations elasticsearchOperations;

    public ChatESRepositoryNative(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<ChatDocument> searchFraud(String keywords) throws IOException {
        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(m1 -> m1.term(t -> t.field("roomId").value(3)))
                                .must(m2 -> m2.queryString(qs -> qs
                                        .defaultField("content")
                                        .query(
                                                keywords
                                        )
                                ))
                )
        ).build();

        List<ChatDocument> response = elasticsearchOperations.search(nativeQuery, ChatDocument.class)
                .map(SearchHit::getContent)
                .toList();

        return response;
    }
}
