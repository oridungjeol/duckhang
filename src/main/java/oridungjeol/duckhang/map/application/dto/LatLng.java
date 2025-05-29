package oridungjeol.duckhang.map.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatLng {
    private double latitude;
    private double longitude;
    private String user_id;
    private long room_id;
}
