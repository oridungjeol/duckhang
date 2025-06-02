package oridungjeol.duckhang.board.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.board.application.port.in.MyPageUseCase;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.BoardResponseDto;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.application.service.BoardUseCaseFactory;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;
import oridungjeol.duckhang.common.firebase.storage.FirebaseStorageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardUseCaseFactory boardUsecaseFactory;
    private final MyPageUseCase myPageUseCase;
    private final FirebaseStorageService firebaseStorageService;

    @PostMapping(value = "/{boardType}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createBoard(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestPart("dto") RequestDto requestDto,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageFile,
            @PathVariable BoardType boardType
    ) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        String uuid = principal.getName();
        Long createId = boardUseCase.createBoard(UUID.fromString(uuid), boardType, requestDto, imageFile);
        return ResponseEntity.ok(createId);
    }

    @GetMapping("/{boardType}")
    public ResponseEntity<Page<BoardListResponseDto>> findAllBoards(
            @PathVariable BoardType boardType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        Page<BoardListResponseDto> boards = boardUseCase.getAllBoards(boardType, page, size);
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
            @RequestBody RequestDto requestDto,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageFile
    ) {
        BoardUseCase boardUseCase = boardUsecaseFactory.getBoardUseCase(boardType);
        String uuid = principal.getName();
        Long updateId = boardUseCase.updateBoard(boardId, UUID.fromString(uuid), requestDto, imageFile);
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoardListResponseDto>> findMyBoards(
            @PathVariable String userId
    ) {
        UUID userUuid = UUID.fromString(userId);
        List<BoardListResponseDto> boards = myPageUseCase.getAllUserBoards(userUuid);
        return ResponseEntity.ok(boards);
    }

}
