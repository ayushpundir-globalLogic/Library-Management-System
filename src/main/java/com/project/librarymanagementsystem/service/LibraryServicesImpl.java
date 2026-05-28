package com.project.librarymanagementsystem.service;

import com.project.librarymanagementsystem.dto.BookDTO;
import com.project.librarymanagementsystem.dto.BorrowerDTO;
import com.project.librarymanagementsystem.dto.LoanResponseDTO;
import com.project.librarymanagementsystem.exception.BookNotAvailableException;
import com.project.librarymanagementsystem.exception.ResourceNotFoundException;
import com.project.librarymanagementsystem.model.Book;
import com.project.librarymanagementsystem.model.Borrower;
import com.project.librarymanagementsystem.model.Loan;
import com.project.librarymanagementsystem.repository.BookRepository;
import com.project.librarymanagementsystem.repository.BorrowerRepository;
import com.project.librarymanagementsystem.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServicesImpl implements LibraryServices{

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final LoanRepository loanRepository;

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDTO addBook(BookDTO bookDTO) {
        if (bookDTO == null || bookDTO.getIsbn() == null || bookDTO.getIsbn().isBlank()) {
            throw new BookNotAvailableException("Cannot catalog a book without a valid ISBN.");
        }

        boolean isbnExists = bookRepository.findAll().stream()
                .anyMatch(b -> b.getIsbn().equalsIgnoreCase(bookDTO.getIsbn()));

        if (isbnExists) {
            throw new BookNotAvailableException("A catalog item with ISBN '" + bookDTO.getIsbn() + "' already exists!");
        }

        Book book = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .isbn(bookDTO.getIsbn())
                .isAvailable(true)
                .build();

        Book savedBook = bookRepository.save(book);
        return convertToBookDTO(savedBook);
    }

    @Override
    @Transactional
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(this::convertToBorrowerDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoanResponseDTO checkOutBook(Long bookId, Long borrowerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book data validation failed: No book found with ID " + bookId));

        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower validation failed: No member registered under ID " + borrowerId));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Transaction Denied: The book '" + book.getTitle() + "' is currently checked out!");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        Loan loan = Loan.builder()
                .book(book)
                .borrower(borrower)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        Loan savedLoan = loanRepository.save(loan);
        return convertToLoanResponseDTO(savedLoan);
    }

    @Override
    @Transactional
    public List<LoanResponseDTO> checkAndFlagOverdueLoans() {
        LocalDate today = LocalDate.now();
        List<Loan> overdueLoans = loanRepository.findByReturnDateIsNullAndDueDateBefore(today);

        for (Loan loan : overdueLoans) {
            loan.setOverdue(true);

            Borrower borrower = loan.getBorrower();
            borrower.setHasOverdueItems(true);

            borrowerRepository.save(borrower);
            loanRepository.save(loan);
        }

        return overdueLoans.stream()
                .map(this::convertToLoanResponseDTO)
                .collect(Collectors.toList());
    }

    private BookDTO convertToBookDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .isAvailable(book.isAvailable())
                .build();
    }

    private BorrowerDTO convertToBorrowerDTO(Borrower borrower) {
        return BorrowerDTO.builder()
                .id(borrower.getId())
                .name(borrower.getName())
                .email(borrower.getEmail())
                .membershipDate(borrower.getMembershipDate())
                .hasOverdueItems(borrower.isHasOverdueItems())
                .build();
    }

    private LoanResponseDTO convertToLoanResponseDTO(Loan loan) {
        return LoanResponseDTO.builder()
                .id(loan.getId())
                .bookId(loan.getBook().getId())
                .bookTitle(loan.getBook().getTitle())
                .borrowerId(loan.getBorrower().getId())
                .borrowerName(loan.getBorrower().getName())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .isOverdue(loan.isOverdue())
                .build();
    }
}
