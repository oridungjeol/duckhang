package oridungjeol.duckhang.board.support;

import org.springframework.core.convert.converter.Converter;
import oridungjeol.duckhang.board.domain.BoardType;

public class SpringToBoardTypeConverter implements Converter<String, BoardType> {

    @Override
    public BoardType convert(String source) {
        return BoardType.valueOf(source.toUpperCase());
    }
}
