package oridungjeol.duckhang.board.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;
import oridungjeol.duckhang.board.infrastructure.repository.BoardJpaRepository;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.infrastructure.mapper.BoardEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardRepositoryAdapter implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;

    @Override
    public Board save(Board board) {
        BoardEntity entity;

        if (board.getId() != null) {
            entity = boardJpaRepository.findById(board.getId())
                    .map(e -> BoardEntityMapper.toUpdatedEntity(e, board))
                    .orElse(BoardEntityMapper.toEntity(board));
        } else {
            entity = BoardEntityMapper.toEntity(board);
        }

        BoardEntity saved = boardJpaRepository.save(entity);
        return BoardEntityMapper.toDomain(saved);
    }
    @Override
    public Optional<Board> findById(Long id) {
        return boardJpaRepository.findById(id)
                .map(BoardEntityMapper::toDomain);
    }

    @Override
    public List<Board> findAllByBoardType(BoardType boardType) {
        return boardJpaRepository.findAllByBoardType(boardType).stream()
                .map(BoardEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        boardJpaRepository.deleteById(id);
    }

    @Override
    public List<Board> findAllByAuthorUuid(UUID authorUuid) {
        return boardJpaRepository.findAllByAuthorUuid(authorUuid).stream()
                .map(BoardEntityMapper::toDomain)
                .toList();
    }
}