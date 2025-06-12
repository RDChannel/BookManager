package rd.book.management.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import rd.book.management.manager.dto.BookDTO;
import rd.book.management.manager.entity.Book;
import rd.book.management.manager.repository.BookRepository;
import rd.book.management.manager.service.BookService;
import rd.book.management.manager.utils.UtilISBN;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @Mock
    private BookRepository bookRepository;

	@Mock
    private UtilISBN utilISBN;

    @InjectMocks
    private BookService bookService;

	private Book testBook;
    private BookDTO bookDTO;

	@BeforeEach
    void beforeEach() {//Create initial data for each test
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Roan");
        testBook.setAuthor("Roan");
        testBook.setIsbn("1234567890123");

        bookDTO = new BookDTO();
        bookDTO.setTitle("Roan");
        bookDTO.setAuthor("Roan");
    }

	@Test
    void testGetBooks() {
		Pageable pageable = PageRequest.of(0, 100); //code that tests if get books list is retrieved
    	Page<Book> bookPage = new PageImpl<>(List.of(testBook));
		when(bookRepository.findAll(pageable)).thenReturn(bookPage);

		Page<Book> pageResponse = bookService.getBooks(0, 100);
		assertEquals(1, pageResponse.getContent().size());
		assertEquals(testBook.getId(), pageResponse.getContent().get(0).getId());
		assertEquals(testBook.getAuthor(), pageResponse.getContent().get(0).getAuthor());
		assertEquals(testBook.getTitle(), pageResponse.getContent().get(0).getTitle());
		assertEquals(testBook.getIsbn(), pageResponse.getContent().get(0).getIsbn());
    }

	@Test
    void testDeleteBooks() {
		when(bookRepository.findAll()).thenReturn(List.of(testBook)); //code that tests if delete all was triggered from bookService
		ResponseEntity<String> response = bookService.deleteBooks();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("All Books Removed From System", response.getBody());
		verify(bookRepository).deleteAll();
    }

	@Test
    void testGetBookById() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));//code that tests if book can be found by id
		Book book = bookService.findBookById(1L);
		assertEquals(testBook.getId(), book.getId());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getIsbn(), book.getIsbn());
    }

	@Test
    void testGetBookByAuthorOrTitle() {
		Pageable pageable = PageRequest.of(0, 100);//code that tests a search service to find books by author or title
    	Page<Book> bookPage = new PageImpl<>(List.of(testBook));
		Page<Book> emptyPage = new PageImpl<>(List.of());
		when(bookRepository.findByTitleOrAuthorContaining("Roan","",pageable)).thenReturn(bookPage);
		when(bookRepository.findByTitleOrAuthorContaining("Ro","",pageable)).thenReturn(bookPage);
		when(bookRepository.findByTitleOrAuthorContaining("","Roan",pageable)).thenReturn(bookPage);
		when(bookRepository.findByTitleOrAuthorContaining("","Ro",pageable)).thenReturn(bookPage);
		when(bookRepository.findByTitleOrAuthorContaining("","",pageable)).thenReturn(emptyPage);
		
		Page<Book> pageResponse = bookService.searchBookByTitleOrAuthor("Roan","",0, 100);//code that tests if it finds books by title
		assertEquals(1, pageResponse.getContent().size());
		assertEquals(testBook.getId(), pageResponse.getContent().get(0).getId());
		assertEquals(testBook.getAuthor(), pageResponse.getContent().get(0).getAuthor());
		assertEquals(testBook.getTitle(), pageResponse.getContent().get(0).getTitle());
		assertEquals(testBook.getIsbn(), pageResponse.getContent().get(0).getIsbn());

		pageResponse = bookService.searchBookByTitleOrAuthor("Ro","",0, 100);//code that tests if it finds books by partial title
		assertEquals(1, pageResponse.getContent().size());
		assertEquals(testBook.getId(), pageResponse.getContent().get(0).getId());
		assertEquals(testBook.getAuthor(), pageResponse.getContent().get(0).getAuthor());
		assertEquals(testBook.getTitle(), pageResponse.getContent().get(0).getTitle());
		assertEquals(testBook.getIsbn(), pageResponse.getContent().get(0).getIsbn());

		pageResponse = bookService.searchBookByTitleOrAuthor("","Roan",0, 100);//code that tests if it finds books by author
		assertEquals(1, pageResponse.getContent().size());
		assertEquals(testBook.getId(), pageResponse.getContent().get(0).getId());
		assertEquals(testBook.getAuthor(), pageResponse.getContent().get(0).getAuthor());
		assertEquals(testBook.getTitle(), pageResponse.getContent().get(0).getTitle());
		assertEquals(testBook.getIsbn(), pageResponse.getContent().get(0).getIsbn());

		pageResponse = bookService.searchBookByTitleOrAuthor("","Ro",0, 100);//code that tests if it finds books by partial author
		assertEquals(1, pageResponse.getContent().size());
		assertEquals(testBook.getId(), pageResponse.getContent().get(0).getId());
		assertEquals(testBook.getAuthor(), pageResponse.getContent().get(0).getAuthor());
		assertEquals(testBook.getTitle(), pageResponse.getContent().get(0).getTitle());
		assertEquals(testBook.getIsbn(), pageResponse.getContent().get(0).getIsbn());

		pageResponse = bookService.searchBookByTitleOrAuthor("","",0, 100);//code that tests that it shouldnt find any books
		assertEquals(0, pageResponse.getContent().size());
    }


	@Test
    void testDeleteBookById() {
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook)); //code that tests if delete by id was triggered from bookService
		Book book = bookService.deleteBookById(1L);
		assertEquals(testBook.getId(), book.getId());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getIsbn(), book.getIsbn());
		verify(bookRepository).delete(testBook);
    }


	@Test
    void testGetBookByISBN() {
		when(bookRepository.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook));//code that tests if book can be found by ISBN
		Book book = bookService.findBookByISBN(testBook.getIsbn());
		assertEquals(testBook.getId(), book.getId());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getIsbn(), book.getIsbn());
    }

	@Test
    void testDeleteBookByISBN() {
		when(bookRepository.findByIsbn(testBook.getIsbn())).thenReturn(Optional.of(testBook)); //code that tests if delete by ISBN was triggered from bookService
		Book book = bookService.deleteBookByISBN(testBook.getIsbn());
		assertEquals(testBook.getId(), book.getId());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getIsbn(), book.getIsbn());
		verify(bookRepository).delete(testBook);
    }

	@Test
    void testCreateBook() {
		when(utilISBN.Generate()).thenReturn("1234567890123");         //code that tests if book can be created
		when(bookRepository.save(any(Book.class))).thenReturn(testBook);
		when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(),bookDTO.getAuthor())).thenReturn(Optional.empty());
		when(bookRepository.findByIsbn(testBook.getIsbn())).thenReturn(Optional.empty());
		Book book = bookService.createBook(bookDTO);
		assertEquals(testBook.getId(), book.getId());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getIsbn(), book.getIsbn());
    }


	@Test
    void testCreateBookExists() {        				//code that tests if book can not be created because an existing one was found
		when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(),bookDTO.getAuthor())).thenReturn(Optional.of(testBook));
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> bookService.createBook(bookDTO)
		);
		assertEquals("Book Already Exists, ISBN: " + testBook.getIsbn(), exception.getMessage());
		verify(bookRepository, never()).save(any());
    }


   @Test
    void testUpdateBook() {      //code that tests if book can be updated
		when(bookRepository.save(any(Book.class))).thenReturn(testBook);
		when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
		when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(),bookDTO.getAuthor())).thenReturn(Optional.empty());
		Book book = bookService.updateBook(1L,bookDTO);
		assertEquals(testBook.getId(), book.getId());
		assertEquals(testBook.getAuthor(), book.getAuthor());
		assertEquals(testBook.getTitle(), book.getTitle());
		assertEquals(testBook.getIsbn(), book.getIsbn());
    }


	@Test
    void testUpdateBookExists() {        				//code that tests if book can not be updated because an existing one was found
		when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(),bookDTO.getAuthor())).thenReturn(Optional.of(testBook));
		IllegalArgumentException exception = assertThrows(
			IllegalArgumentException.class,
			() -> bookService.updateBook(1L,bookDTO)
		);
		assertEquals("Book already contains input data!", exception.getMessage());
		verify(bookRepository, never()).save(any());
    }
}
