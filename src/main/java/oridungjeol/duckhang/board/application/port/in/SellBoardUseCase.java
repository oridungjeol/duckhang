package oridungjeol.duckhang.board.application.port.in;

import oridungjeol.duckhang.board.presentation.dto.RequestDto;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;

import java.util.List;
import java.util.UUID;

public interface SellBoardUseCase {
    Long createBoard(UUID authorUuid, RequestDto requestDto);
    TradeDetailDto getDetailBoard(Long boardId);
    List<TradeListDto> getAllBoards();
    Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto);
    void deleteBoard(UUID authorUuid, Long id);
}
