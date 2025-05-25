package oridungjeol.duckhang.board.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;
import oridungjeol.duckhang.board.infrastructure.repository.BoardJpaRepository;
import oridungjeol.duckhang.board.support.enums.BoardType;
import oridungjeol.duckhang.board.support.mapper.BoardMapper;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardRepositoryAdapter implements BoardRepository {
    private final BoardJpaRepository boardJpaRepository;

    @Override
    public Board save(Board board) {
        BoardEntity entity;

        if (board.getId() != null) {
            entity = boardJpaRepository.findById(board.getId())
                    .map(e -> BoardMapper.toUpdatedEntity(e, board))
                    .orElse(BoardMapper.toEntity(board));
        } else {
            entity = BoardMapper.toEntity(board);
        }

        BoardEntity saved = boardJpaRepository.save(entity);
        return BoardMapper.toDomain(saved);
    }
    @Override
    public Optional<Board> findById(Long id) {
        return boardJpaRepository.findById(id)
                .map(BoardMapper::toDomain);
    }

    @Override
    public List<Board> findAllByBoardType(BoardType boardType) {
        return boardJpaRepository.findAllByBoardType(boardType).stream()
                .map(BoardMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        boardJpaRepository.deleteById(id);
    }
}