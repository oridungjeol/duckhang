package oridungjeol.duckhang.map.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.map.application.dto.LatLng;

@RequiredArgsConstructor
@Service
public class MapService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public String sendLatLng(LatLng latlng) {
        String destination = "/topic/map.latlng/" + latlng.getRoom_id();
        simpMessagingTemplate.convertAndSend(destination, latlng);
        return destination;
    }
}
