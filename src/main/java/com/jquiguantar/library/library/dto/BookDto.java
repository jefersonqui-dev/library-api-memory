package com.jquiguantar.library.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 1, max = 255, message = "El autor debe tener entre 1 y 255 caracteres")
    private String author;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @NotNull(message = "El año de publicación es obligatorio")
    private Integer publicationYear;

    private String isbn;

    @Size(max = 100, message = "El género no puede exceder 100 caracteres")
    private String genre;

    private Boolean available = true;
}
