package oridungjeol.duckhang.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.domain.BoardType;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardUseCaseFactory {
    private final ListableBeanFactory beanFactory;

    public BoardUseCase getBoardUseCase(BoardType boardType) {
        Map<String, BoardUseCase> beans = beanFactory.getBeansOfType(BoardUseCase.class);
        return beans.values().stream().filter(it -> it.supportBoardType(boardType))
                .findFirst().orElseThrow(() -> {
                            return new IllegalArgumentException("지원하지 않는 게시판 타입입니다: " + boardType);
                        }
                );
    }
}
