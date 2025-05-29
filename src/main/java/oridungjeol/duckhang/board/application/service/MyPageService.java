package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.application.mapper.BoardDtoMapper;
import oridungjeol.duckhang.board.application.port.in.MyPageUseCase;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListDto;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyPageService implements MyPageUseCase {
    private final BoardRepository boardRepository;
    private final UserJpaRepository userJpaRepository;
    @Override
    public List<BoardListResponseDto> getAllUserBoards(UUID userId) {
        List<Board> boards = boardRepository.findAllByAuthorUuid(userId); // 모든 타입

        return boards.stream()
                .map(board -> {
                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    return BoardDtoMapper.toBoardListDto(board, user);
                })
                .sorted(Comparator.comparing(BoardListResponseDto::createdAt).reversed())
                .toList();
    }
}
