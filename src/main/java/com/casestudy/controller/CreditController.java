package com.casestudy.controller;

import com.casestudy.dto.credit.CreditRequestDto;
import com.casestudy.dto.credit.CreditResponseDto;
import com.casestudy.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
