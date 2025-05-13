package oridungjeol.duckhang.map.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import oridungjeol.duckhang.map.application.dto.LatLng;
import oridungjeol.duckhang.map.application.service.MapService;

@RequiredArgsConstructor
@Controller
public class MapController {
    private final MapService mapService;

    @MessageMapping("map.latlng")
    public String sendLatLng(LatLng latlng) {
        return mapService.sendLatLng(latlng);
    }
}
