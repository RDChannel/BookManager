package rd.book.management.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rd.book.management.manager.dto.BookDTO;
import rd.book.management.manager.entity.Book;
import rd.book.management.manager.service.BookService;

@RestController
@RequestMapping("/Book")
public class BookController { //Code Where all the different controllers for books are set up

    @Autowired
    private final BookService bookService;

    public BookController(BookService bookService){// inject book service for use
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getAllBook( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size){
        return bookService.getBooks(page, size);
    }

    @GetMapping("/ByTitleOrAuthor")
    public Page<Book> getByTitleOrAuthor(@RequestParam String title, @RequestParam String author, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size){
        return bookService.searchBookByTitleOrAuthor(title, author, page, size);
    }

    @GetMapping("/ByIsbn/{isbn}")
    public Book findBookByIsbn( @PathVariable String isbn){
        return bookService.findBookByISBN(isbn);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id){
        return bookService.findBookById(id);
    }

    @DeleteMapping("/ByIsbn/{isbn}")
    public Book deleteBookByIsbn( @PathVariable String isbn){
        return bookService.deleteBookByISBN(isbn);
    }

    @DeleteMapping("/{id}")
    public Book deleteBook(@PathVariable Long id){
        return bookService.deleteBookById(id);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBook(){
        return bookService.deleteBooks();
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Book updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable Long id) {
        return bookService.updateBook(id,bookDTO);
    }
}
