package com.casestudy.controller;

import com.casestudy.dto.credit.CreditRequestDto;
import com.casestudy.dto.credit.CreditResponseDto;
import com.casestudy.model.Credit;
import com.casestudy.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;
    @PostMapping("/create")
    public ResponseEntity<CreditResponseDto> createCredit(@RequestBody CreditRequestDto creditRequestDto) {
        CreditResponseDto responseDto = creditService.createCredit(creditRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<CreditResponseDto>> listCredits(@PathVariable Integer userId) {
        List<CreditResponseDto> responseDto = creditService.listCredits(userId);
        return ResponseEntity.ok(responseDto);
    }
    @GetMapping("/list/filter")
    public ResponseEntity<List<CreditResponseDto>> listFilteredAndPaginatedCredits(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<CreditResponseDto> creditResponseDtos = creditService.listFilteredAndPaginatedCredits(status, date);
        return ResponseEntity.ok(creditResponseDtos);
    }


}
