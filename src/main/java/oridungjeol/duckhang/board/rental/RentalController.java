package oridungjeol.duckhang.board.rental;


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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 boarId에 해당하는 게시글이 없습니다."));
        return ResponseEntity.ok(rental);
    }
}
