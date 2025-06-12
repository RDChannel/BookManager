package rd.book.management.manager.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import rd.book.management.manager.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {//Code to set up the book repository
    Optional<Book> findByTitleAndAuthor(String title, String author);
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findAll(Pageable pageable);
    Page<Book> findByTitleOrAuthorContaining(String title, String author, Pageable pageable);
}
