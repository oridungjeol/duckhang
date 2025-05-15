package oridungjeol.duckhang.board;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/rental")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RentalController {

    private final RentalRepository rentalRepository;

    @GetMapping("/{boardId}")
    public ResponseEntity<RentalEntity> getRental(@PathVariable int boardId) {
        RentalEntity rental = rentalRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "렌탈 정보를 찾을 수 없습니다."));
        return ResponseEntity.ok(rental);
    }
}
