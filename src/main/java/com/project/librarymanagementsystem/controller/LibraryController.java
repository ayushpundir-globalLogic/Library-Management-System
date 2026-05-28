package com.project.librarymanagementsystem.controller;

import com.project.librarymanagementsystem.dto.BookDTO;
import com.project.librarymanagementsystem.dto.BorrowerDTO;
import com.project.librarymanagementsystem.dto.LoanResponseDTO;
import com.project.librarymanagementsystem.service.LibraryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryServices libraryServices;

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(libraryServices.getAllBooks());
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        BookDTO savedBook = libraryServices.addBook(bookDTO);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping("/borrowers")
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        return ResponseEntity.ok(libraryServices.getAllBorrowers());
    }

    @PostMapping("/loans/checkout")
    public ResponseEntity<LoanResponseDTO> checkOutBook(
            @RequestParam Long bookId,
            @RequestParam Long borrowerId) {
        return ResponseEntity.ok(libraryServices.checkOutBook(bookId, borrowerId));
    }

    @GetMapping("/loans/overdue")
    public ResponseEntity<List<LoanResponseDTO>> checkOverdueLoans() {
        return ResponseEntity.ok(libraryServices.checkAndFlagOverdueLoans());
    }
}
