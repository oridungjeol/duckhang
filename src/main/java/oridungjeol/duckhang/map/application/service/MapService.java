package oridungjeol.duckhang.map.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.map.application.dto.LatLng;

@RequiredArgsConstructor
@Service
public class MapService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public String sendLatLng(LatLng latlng) {
        String destination = "/topic/map." + latlng.getRoomId();
        simpMessagingTemplate.convertAndSend(destination, latlng);
        return destination;
    }
}
