package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.application.mapper.RentalDtoMapper;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.RentalRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.domain.RentalPost;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.RentalDetailDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalBoardService implements BoardUseCase {
    private final BoardRepository boardRepository;
    private final RentalRepository rentalRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    @Override
    public boolean supportBoardType(BoardType boardType) {
        return BoardType.RENTAL == boardType;
    }

    @Override
    public Long createBoard(
            UUID authorUuid,
            BoardType boardType,
            RequestDto requestDto
    ) {
        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl(), boardType);
        Board savedBoard = boardRepository.save(board);

        RentalPost rentalPost = new RentalPost(savedBoard.getId(), requestDto.getPrice(), requestDto.getDeposit());
        rentalRepository.save(rentalPost);

        BoardDocument document = BoardDocument.builder()
                .id(savedBoard.getId())
                .authorUuid(savedBoard.getAuthorUuid())
                .title(savedBoard.getTitle())
                .content(savedBoard.getContent())
                .imageUrl(savedBoard.getImageUrl())
                .createdAt(savedBoard.getCreatedAt())
                .boardType(savedBoard.getBoardType())
                .build();

        boardDocumentRepository.save(document);

        return savedBoard.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getAllBoards(BoardType boardType) {
        List<Board> boards = boardRepository.findAllByBoardType(boardType);

        return boards.stream()
                .map(board-> {
                    RentalPost rentalPost = rentalRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
                    return RentalDtoMapper.toRentalListDto(board, rentalPost);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RentalDetailDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        RentalPost rentalPost = rentalRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return RentalDtoMapper.toRentalDetailDto(board, rentalPost, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        RentalPost rentalPost = rentalRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
        rentalPost.updatePriceAndDeposit(requestDto.getPrice(), requestDto.getDeposit());

        boardRepository.save(board);
        rentalRepository.save(rentalPost);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        rentalRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}
