package com.jquiguantar.library.library.repository;

import com.jquiguantar.library.library.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class BookRepository {

    // Colección en memoria para almacenar los libros
    private final Map<Long, Book> books = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Constructor para inicializar con datos de ejemplo
    public BookRepository() {
        initializeSampleData();
    }

    // Inicializar con datos de ejemplo
    private void initializeSampleData() {
        Book book1 = new Book(idGenerator.getAndIncrement(), "Don Quijote de la Mancha",
                "Miguel de Cervantes",
                "Obra maestra de la literatura española que narra las aventuras de un hidalgo que enloquece por la lectura de libros de caballerías",
                1605, "978-84-376-0494-7", "Novela", true);

        Book book2 = new Book(idGenerator.getAndIncrement(), "Cien años de soledad",
                "Gabriel García Márquez",
                "Novela que cuenta la historia de la familia Buendía a lo largo de siete generaciones en el pueblo ficticio de Macondo",
                1967, "978-84-397-2071-7", "Realismo mágico", true);

        Book book3 = new Book(idGenerator.getAndIncrement(), "El Señor de los Anillos",
                "J.R.R. Tolkien",
                "Trilogía épica de fantasía que narra la búsqueda del Anillo Único para destruirlo en el Monte del Destino",
                1954, "978-84-450-7139-9", "Fantasía épica", true);

        Book book4 = new Book(idGenerator.getAndIncrement(), "1984",
                "George Orwell",
                "Novela distópica que describe una sociedad totalitaria bajo la vigilancia constante del Gran Hermano",
                1949, "978-84-397-2071-7", "Ciencia ficción", true);

        Book book5 = new Book(idGenerator.getAndIncrement(), "El Principito",
                "Antoine de Saint-Exupéry",
                "Cuento poético que trata temas como el amor, la amistad y el sentido de la vida a través de la historia de un pequeño príncipe",
                1943, "978-84-397-2071-7", "Literatura infantil", true);

        books.put(book1.getId(), book1);
        books.put(book2.getId(), book2);
        books.put(book3.getId(), book3);
        books.put(book4.getId(), book4);
        books.put(book5.getId(), book5);
    }

    // Obtener todos los libros
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    // Buscar por ID
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    // Guardar libro (crear o actualizar)
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(idGenerator.getAndIncrement());
        }
        books.put(book.getId(), book);
        return book;
    }

    // Eliminar por ID
    public boolean deleteById(Long id) {
        return books.remove(id) != null;
    }

    // Verificar si existe por ID
    public boolean existsById(Long id) {
        return books.containsKey(id);
    }

    // Buscar por autor
    public List<Book> findByAuthorContainingIgnoreCase(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Buscar por título
    public List<Book> findByTitleContainingIgnoreCase(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Buscar por ISBN
    public Optional<Book> findByIsbn(String isbn) {
        return books.values().stream()
                .filter(book -> Objects.equals(book.getIsbn(), isbn))
                .findFirst();
    }

    // Buscar libros disponibles
    public List<Book> findByAvailableTrue() {
        return books.values().stream()
                .filter(Book::getAvailable)
                .collect(Collectors.toList());
    }

    // Buscar por año de publicación
    public List<Book> findByPublicationYear(Integer year) {
        return books.values().stream()
                .filter(book -> Objects.equals(book.getPublicationYear(), year))
                .collect(Collectors.toList());
    }

    // Buscar por rango de años
    public List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear) {
        return books.values().stream()
                .filter(book -> book.getPublicationYear() >= startYear && book.getPublicationYear() <= endYear)
                .collect(Collectors.toList());
    }

    // Búsqueda general (título o autor)
    public List<Book> findByTitleOrAuthorContainingIgnoreCase(String searchText) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Obtener el siguiente ID disponible
    public Long getNextId() {
        return idGenerator.get();
    }
}
