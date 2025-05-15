package oridungjeol.duckhang.map.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LatLng {
    private double latitude;
    private double longitude;
    private String user_id;
    private long room_id;
}
