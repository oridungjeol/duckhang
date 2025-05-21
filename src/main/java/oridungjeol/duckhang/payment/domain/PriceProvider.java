package oridungjeol.duckhang.payment.domain;

public interface PriceProvider {
    boolean supports(String type);   // 이 Provider가 처리 가능한 타입인지 반환
    int getPrice(Long boardId); // boardId로 가격 조회
}

