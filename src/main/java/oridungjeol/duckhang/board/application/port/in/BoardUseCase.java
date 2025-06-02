package oridungjeol.duckhang.board.application.port.in;

import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.BoardResponseDto;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;

import java.util.List;
import java.util.UUID;

public interface BoardUseCase {
    boolean supportBoardType(BoardType boardType);
    Long createBoard(UUID authorUuid,  BoardType boardType, RequestDto requestDto, MultipartFile imageFile);
    Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto, MultipartFile imageFile);
    void deleteBoard(UUID authorUuid, Long id);

    BoardResponseDto getDetailBoard(Long boardId);
    List<BoardListResponseDto> getAllBoards(BoardType boardType);
}
