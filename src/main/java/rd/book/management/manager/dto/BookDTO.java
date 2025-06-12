package rd.book.management.manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookDTO {//Created for api input payload
    @Size(max = 100, message = "Title can not be more than 100 characters!")
    @NotBlank(message = "Title cant be blank!")
    private String title;

    @Size(max = 50, message = "Author can not be more than 50 characters!")
    @NotBlank(message = "Author cant be blank!")
    private String author;
}
