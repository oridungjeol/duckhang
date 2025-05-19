package oridungjeol.duckhang.map.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import oridungjeol.duckhang.map.application.dto.LatLng;
import oridungjeol.duckhang.map.application.service.MapService;

@RequiredArgsConstructor
@Controller
public class MapController {
    private final MapService mapService;

    @MessageMapping("map.latlng/{room_id}")
    public String sendLatLng(@DestinationVariable("room_id") long room_id, LatLng latlng) {
        return mapService.sendLatLng(latlng);
    }
}
