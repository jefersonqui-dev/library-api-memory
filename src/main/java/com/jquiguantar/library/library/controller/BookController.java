package com.jquiguantar.library.library.controller;

import com.jquiguantar.library.library.dto.BookDto;
import com.jquiguantar.library.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Gestión de Libros", description = "API para gestionar la biblioteca de libros")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Retorna una lista de todos los libros en la biblioteca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libros encontrados exitosamente"),
            @ApiResponse(responseCode = "204", description = "No hay libros disponibles")
    })
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID", description = "Retorna un libro específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<BookDto> getBookById(
            @Parameter(description = "ID del libro") @PathVariable Long id) {
        Optional<BookDto> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo libro", description = "Crea un nuevo libro en la biblioteca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del libro inválidos")
    })
    public ResponseEntity<BookDto> createBook(
            @Parameter(description = "Datos del libro a crear") @Valid @RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro existente", description = "Actualiza un libro existente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del libro inválidos"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<BookDto> updateBook(
            @Parameter(description = "ID del libro") @PathVariable Long id,
            @Parameter(description = "Datos actualizados del libro") @Valid @RequestBody BookDto bookDto) {
        Optional<BookDto> updatedBook = bookService.updateBook(id, bookDto);
        return updatedBook.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", description = "Elimina un libro de la biblioteca por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID del libro") @PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/author")
    @Operation(summary = "Buscar libros por autor", description = "Busca libros que contengan el nombre del autor especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "204", description = "No se encontraron libros del autor")
    })
    public ResponseEntity<List<BookDto>> findByAuthor(
            @Parameter(description = "Nombre del autor") @RequestParam String author) {
        List<BookDto> books = bookService.findByAuthor(author);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/title")
    @Operation(summary = "Buscar libros por título", description = "Busca libros que contengan el título especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "204", description = "No se encontraron libros con ese título")
    })
    public ResponseEntity<List<BookDto>> findByTitle(
            @Parameter(description = "Título del libro") @RequestParam String title) {
        List<BookDto> books = bookService.findByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/available")
    @Operation(summary = "Obtener libros disponibles", description = "Retorna solo los libros que están disponibles para préstamo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libros disponibles encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay libros disponibles")
    })
    public ResponseEntity<List<BookDto>> getAvailableBooks() {
        List<BookDto> books = bookService.getAvailableBooks();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Búsqueda general", description = "Busca libros cuyo título o autor contengan el texto proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "204", description = "No se encontraron libros")
    })
    public ResponseEntity<List<BookDto>> searchBooks(
            @Parameter(description = "Texto de búsqueda") @RequestParam String q) {
        List<BookDto> books = bookService.findByTitleOrAuthor(q);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @PostMapping("/{id}/prestar")
    @Operation(summary = "Prestar libro", description = "Marca un libro específico como prestado (no disponible)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro prestado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El libro no está disponible para préstamo"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<BookDto> lendBook(
            @Parameter(description = "ID del libro") @PathVariable Long id) {
        try {
            Optional<BookDto> lentBook = bookService.lendBook(id);
            return lentBook.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/devolver")
    @Operation(summary = "Devolver libro", description = "Marca un libro específico como devuelto (disponible)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro devuelto exitosamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<BookDto> returnBook(
            @Parameter(description = "ID del libro") @PathVariable Long id) {
        Optional<BookDto> returnedBook = bookService.returnBook(id);
        return returnedBook.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
