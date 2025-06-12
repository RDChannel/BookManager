package rd.book.management.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import rd.book.management.manager.controller.BookController;
import rd.book.management.manager.dto.BookDTO;
import rd.book.management.manager.entity.Book;
import rd.book.management.manager.service.BookService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(BookController.class)
public class ControllerTests {
    
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BookService bookService;


    private Book testBook;
    private BookDTO bookDTO;

    @BeforeEach
    void setup() {//Initial Start Data
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
    void testGetAllBooks() throws Exception {//Test get all books api
        Page<Book> bookPage = new PageImpl<>(List.of(testBook));

        when(bookService.getBooks(0, 100)).thenReturn(bookPage);

        mockMvc.perform(get("/Book"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].title").value("Roan"))
            .andExpect(jsonPath("$.content[0].author").value("Roan"))
            .andExpect(jsonPath("$.content[0].isbn").value("1234567890123"));
    }

    @Test
    void testGetAllBooksByAuthorOrTitle() throws Exception {//Test get all books by searching using author or title api
        Page<Book> bookPage = new PageImpl<>(List.of(testBook));
        when(bookService.searchBookByTitleOrAuthor("Roan","Roan",0, 100)).thenReturn(bookPage);

        mockMvc.perform(get("/Book/ByTitleOrAuthor?page=0&size=100&title=Roan&author=Roan"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].title").value("Roan"))
            .andExpect(jsonPath("$.content[0].author").value("Roan"))
            .andExpect(jsonPath("$.content[0].isbn").value("1234567890123"));
    }

    @Test
    void testCreateBook() throws Exception {//Test create book api
        when(bookService.createBook(any(BookDTO.class))).thenReturn(testBook);

        mockMvc.perform(post("/Book")
                .contentType("application/json")
                .content("""
                    {
                        "title": "Roan",
                        "author": "Roan"
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Roan"))
            .andExpect(jsonPath("$.author").value("Roan"))
            .andExpect(jsonPath("$.isbn").value("1234567890123"));
    }

    @Test
    void testUpdateBook() throws Exception {//Test update book api
        when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(testBook);

        mockMvc.perform(put("/Book/1")
                .contentType("application/json")
                .content("""
                    {
                        "title": "Roan",
                        "author": "Roan"
                    }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Roan"))
            .andExpect(jsonPath("$.author").value("Roan"))
            .andExpect(jsonPath("$.isbn").value("1234567890123"));
    }

    @Test
    void testFindBookById() throws Exception {//Test get book by id api
        when(bookService.findBookById(1L)).thenReturn(testBook);

        mockMvc.perform(get("/Book/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testDeleteBookById() throws Exception {//Test delete book by id api
        when(bookService.deleteBookById(1L)).thenReturn(testBook);

        mockMvc.perform(delete("/Book/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testFindBookByIsbn() throws Exception {//Test get book by isbn api
        when(bookService.findBookByISBN("1234567890123")).thenReturn(testBook);

        mockMvc.perform(get("/Book/ByIsbn/1234567890123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isbn").value("1234567890123"));
    }

    @Test
    void testDeleteBookByIsbn() throws Exception {//Test delete book by isbn api
         when(bookService.deleteBookByISBN("1234567890123")).thenReturn(testBook);

        mockMvc.perform(delete("/Book/ByIsbn/1234567890123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isbn").value("1234567890123"));
    }

    @Test
    void testGetBookNotExists() throws Exception {//Test Not Found book by id api
        when(bookService.findBookById(1L)).thenThrow(new IllegalArgumentException("Not Found"));

        mockMvc.perform(get("/Book/1"))
            .andExpect(status().isNotFound()); 
    }

    @Test
    void testDeleteAllBooks() throws Exception {//Test delete all books api
        when(bookService.deleteBooks())
            .thenReturn(ResponseEntity.status(HttpStatus.OK).body("All Books Removed From System"));

        mockMvc.perform(delete("/Book"))
            .andExpect(status().isOk())
            .andExpect(content().string("All Books Removed From System"));
    }
}
