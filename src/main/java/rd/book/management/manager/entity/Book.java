package rd.book.management.manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Book {//Book entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Chosen to work with mssql server
    private Long id;

    @Column(name="Title", length=100, nullable=false, unique=false)
    private String title;

    @Column(name="Author", length=50, nullable=false, unique=false)
    private String author;

    @Column(name="isbn", length=13, nullable=false, unique=true)
    @Size(min = 13, max = 13, message = "Incorrect ISBN Format")
    private String isbn;

}
