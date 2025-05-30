package oridungjeol.duckhang.board.application.port.in;

import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;

import java.util.List;
import java.util.UUID;

public interface MyPageUseCase {
    List<BoardListResponseDto> getAllUserBoards(UUID userId);
}
