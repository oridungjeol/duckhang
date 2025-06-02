package oridungjeol.duckhang.entity;

import java.util.List;

public class ChatDataset {
    private List<String> fraud;
    private List<String> similar;
    private List<String> normal;

    public List<String> getFraud() {
        return fraud;
    }

    public void setFraud(List<String> fraud) {
        this.fraud = fraud;
    }

    public List<String> getSimilar() {
        return similar;
    }

    public void setSimilar(List<String> similar) {
        this.similar = similar;
    }

    public List<String> getNormal() {
        return normal;
    }

    public void setNormal(List<String> normal) {
        this.normal = normal;
    }
}