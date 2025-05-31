package oridungjeol.duckhang.chat.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FraudDataSet {
    private List<String> fraud;
    private List<String> similar;
    private List<String> normal;
}
