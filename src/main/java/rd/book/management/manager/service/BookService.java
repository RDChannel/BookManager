package rd.book.management.manager.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rd.book.management.manager.dto.BookDTO;
import rd.book.management.manager.entity.Book;
import rd.book.management.manager.repository.BookRepository;
import rd.book.management.manager.utils.UtilISBN;

@Service
@Transactional
public class BookService {// Code That Manages The Input data from the controllers and returns appropriate responses

    private final BookRepository bookRepository; //Injected book reposistory to allow data manipulation to the database data

    private final UtilISBN utilISBN; // Injected to use

    public BookService(BookRepository bookRepository,UtilISBN utilISBN){//Dependency Injection
        this.utilISBN = utilISBN;
        this.bookRepository = bookRepository;
    }

    public Page<Book> getBooks(int page, int size) {//Code to get all books by pagenation
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    public ResponseEntity<String> deleteBooks() {//Code to delete all books
        if(!bookRepository.findAll().isEmpty()){
            bookRepository.deleteAll();
            return ResponseEntity.status(HttpStatus.OK).body("All Books Removed From System");
        }else{
            throw new IllegalArgumentException("No books on system to delete!");
        }
        
    }

    public Book findBookById(Long id){// code to find a book by its id
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            return bookOptional.get();
        }else{
            throw new IllegalArgumentException("Not Found");
        }
    }

    public Page<Book> searchBookByTitleOrAuthor(String title, String author, int page, int size){//code to search for books with pagenation based on the author or the title
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByTitleOrAuthorContaining(title, author, pageable);
    }

    public Book deleteBookById(Long id){
        Optional<Book> bookOptional = bookRepository.findById(id);
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            bookRepository.delete(book);
            return book;
        }else{
            throw new IllegalArgumentException("Not Found");
        }
    }

    public Book findBookByISBN(String isbn){//code used to find book by its isbn number
        if(isbn.length() == 13){
            Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
            if(bookOptional.isPresent()){
                return bookOptional.get();
            }else{
                throw new IllegalArgumentException("Not Found");
            }
        }else{
            throw new IllegalArgumentException("ISBN incorrect format!");
        }
    }

    public Book deleteBookByISBN(String isbn){//code used to delete a book by its isbn number
        if(isbn.length() == 13){
            Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
            if(bookOptional.isPresent()){
                Book book = bookOptional.get();
                bookRepository.delete(book);
                return book;
            }else{
                throw new IllegalArgumentException("Not Found");
            }
        }else{
            throw new IllegalArgumentException("ISBN incorrect format!");
        }
    }

    public Book createBook(BookDTO bookDTO){// code to create a book record in the data
        
        Optional<Book> found = bookRepository.findByTitleAndAuthor(bookDTO.getTitle(),bookDTO.getAuthor());
        if(found.isPresent()){
            throw new IllegalArgumentException("Book Already Exists, ISBN: " + found.get().getIsbn());
        }else{
            Book book = new Book();
            book.setAuthor(bookDTO.getAuthor());
            book.setTitle(bookDTO.getTitle());
            int loops = 0;
            boolean notfound = true;
            Optional<Book> foundISBN;
            while(loops < 10 && notfound){// loop if the isbn isnt unique
                loops ++;
                book.setIsbn(utilISBN.Generate());// code used to generate the isbn
                foundISBN = bookRepository.findByIsbn(book.getIsbn());
                if(!foundISBN.isPresent()){
                    notfound = false;
                }
            }
            return bookRepository.save(book);
        }
    }


    public Book updateBook(Long id, BookDTO bookDTO){//code used to update book data
        String dtoAuthor = bookDTO.getAuthor();
        String dtoTitle = bookDTO.getTitle();
        Optional<Book> found = bookRepository.findByTitleAndAuthor(dtoTitle,dtoAuthor);//code used to check for duplicate api calls or not duplicating books
        if(found.isPresent()){
            if(found.get().getId() != id){
                throw new IllegalArgumentException("Book already exists, ISBN: " + found.get().getIsbn());
            }else{
                throw new IllegalArgumentException("Book already contains input data!");
            }
        }else{
            Optional<Book> bookOptional = bookRepository.findById(id);
            if(bookOptional.isPresent()){
                Book book = bookOptional.get();
                book.setAuthor(dtoAuthor);
                book.setTitle(dtoTitle);
                return bookRepository.save(book);
            }else{
                throw new IllegalArgumentException("Not Found!");
            }
        }
    }

}
