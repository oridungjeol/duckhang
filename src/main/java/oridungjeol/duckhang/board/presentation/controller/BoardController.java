package oridungjeol.duckhang.board.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.board.presentation.dto.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.BoardResponseDto;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.application.service.BoardUseCaseFactory;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.presentation.dto.RequestDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardUseCaseFactory boardUsecaseFactory;

    @PostMapping("/{boardType}")
    public ResponseEntity<Long> createBoard(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable BoardType boardType,
            @RequestBody RequestDto requestDto
    ) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        String uuid = principal.getName();
        Long createId = boardUseCase.createBoard(UUID.fromString(uuid), boardType, requestDto);
        return ResponseEntity.ok(createId);
    }

    @GetMapping("/{boardType}")
    public ResponseEntity<List<BoardListResponseDto>> findAllBoards(@PathVariable BoardType boardType) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        List<BoardListResponseDto> boards = boardUseCase.getAllBoards();
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{boardType}/{boardId}")
    public ResponseEntity<BoardResponseDto> findBoardById(
            @PathVariable BoardType boardType,
            @PathVariable Long boardId
    ) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        BoardResponseDto board = boardUseCase.getDetailBoard(boardId);
        return ResponseEntity.ok(board);
    }

    @PatchMapping("/{boardType}/{boardId}")
    public ResponseEntity<Long> updateBoard(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable BoardType boardType,
            @PathVariable Long boardId,
            @RequestBody RequestDto requestDto
    ) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        String uuid = principal.getName();
        Long updateId = boardUseCase.updateBoard(boardId, UUID.fromString(uuid), requestDto);
        return ResponseEntity.ok(updateId);
    }

    @DeleteMapping("/{boardType}/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable BoardType boardType,
            @PathVariable Long boardId
    ) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        String uuid = principal.getName();
        boardUseCase.deleteBoard(UUID.fromString(uuid),boardId);
        return ResponseEntity.ok().build();
    }
}
