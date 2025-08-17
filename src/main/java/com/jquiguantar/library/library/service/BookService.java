package com.jquiguantar.library.library.service;

import com.jquiguantar.library.library.dto.BookDto;
import com.jquiguantar.library.library.entity.Book;
import com.jquiguantar.library.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    // Obtener todos los libros
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener libro por ID
    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDto);
    }

    // Crear nuevo libro
    public BookDto createBook(BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    // Actualizar libro existente
    public Optional<BookDto> updateBook(Long id, BookDto bookDto) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookDto.getTitle());
                    existingBook.setAuthor(bookDto.getAuthor());
                    existingBook.setDescription(bookDto.getDescription());
                    existingBook.setPublicationYear(bookDto.getPublicationYear());
                    existingBook.setIsbn(bookDto.getIsbn());
                    existingBook.setGenre(bookDto.getGenre());
                    existingBook.setAvailable(bookDto.getAvailable());
                    return convertToDto(bookRepository.save(existingBook));
                });
    }

    // Eliminar libro
    public boolean deleteBook(Long id) {
        return bookRepository.deleteById(id);
    }

    // Buscar por autor
    public List<BookDto> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Buscar por título
    public List<BookDto> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Búsqueda general (título o autor)
    public List<BookDto> findByTitleOrAuthor(String searchText) {
        return bookRepository.findByTitleOrAuthorContainingIgnoreCase(searchText)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Prestar libro
    public Optional<BookDto> lendBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    if (book.getAvailable()) {
                        book.setAvailable(false);
                        return convertToDto(bookRepository.save(book));
                    } else {
                        throw new IllegalStateException("El libro no está disponible para préstamo");
                    }
                });
    }

    // Devolver libro
    public Optional<BookDto> returnBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setAvailable(true);
                    return convertToDto(bookRepository.save(book));
                });
    }

    // Obtener libros disponibles
    public List<BookDto> getAvailableBooks() {
        return bookRepository.findByAvailableTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Métodos de conversión privados
    private BookDto convertToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setIsbn(book.getIsbn());
        dto.setGenre(book.getGenre());
        dto.setAvailable(book.getAvailable());
        return dto;
    }

    private Book convertToEntity(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setPublicationYear(dto.getPublicationYear());
        book.setIsbn(dto.getIsbn());
        book.setGenre(dto.getGenre());
        book.setAvailable(dto.getAvailable());
        return book;
    }
}
