package com.project.librarymanagementsystem.service;

import com.project.librarymanagementsystem.dto.BookDTO;
import com.project.librarymanagementsystem.dto.BorrowerDTO;
import com.project.librarymanagementsystem.dto.LoanResponseDTO;
import com.project.librarymanagementsystem.model.Book;
import com.project.librarymanagementsystem.model.Borrower;

import java.util.List;

public interface LibraryServices {
    public List<BookDTO> getAllBooks();
    public BookDTO addBook(BookDTO bookDTO);
    public List<BorrowerDTO> getAllBorrowers();
    public LoanResponseDTO checkOutBook(Long bookId, Long borrowerId);
    public List<LoanResponseDTO> checkAndFlagOverdueLoans();
}
