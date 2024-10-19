import java.util.*;

public class Main {
    // Book class
    static class Book {
        private String title;
        private String author;
        private int year;

        public Book(String title, String author, int year) {
            this.title = title;
            this.author = author;
            this.year = year;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getYear() {
            return year;
        }

        @Override
        public String toString() {
            return title + " by " + author + " (" + year + ")";
        }
    }

    // BookStore class with Generics
    static class BookStore<T extends Book> {
        private List<T> books = new ArrayList<>();
        private List<Member> members = new ArrayList<>();
        private SortStrategy sortStrategy;

        public void addBook(T book) {
            books.add(book);
            notifyMembers(book);
        }

        public void removeBook(T book) {
            books.remove(book);
        }

        public T findBookByTitle(String title) {
            for (T book : books) {
                if (book.getTitle().equalsIgnoreCase(title)) {
                    return book;
                }
            }
            return null;
        }

        public void setSortStrategy(SortStrategy sortStrategy) {
            this.sortStrategy = sortStrategy;
        }

        public void sortBooks() {
            if (sortStrategy != null) {
                sortStrategy.sort(books);
            }
        }

        public void addMember(Member member) {
            members.add(member);
        }

        private void notifyMembers(T book) {
            for (Member member : members) {
                member.update(book);
            }
        }

        public Iterator<T> iterator() {
            return new BookStoreIterator<>(books);
        }
    }

    // Iterator Pattern
    static class BookStoreIterator<T> implements Iterator<T> {
        private List<T> books;
        private int position = 0;

        public BookStoreIterator(List<T> books) {
            this.books = books;
        }

        @Override
        public boolean hasNext() {
            return position < books.size();
        }

        @Override
        public T next() {
            return books.get(position++);
        }
    }

    // Strategy Pattern
    interface SortStrategy {
        <T extends Book> void sort(List<T> books);
    }

    static class SortByTitle implements SortStrategy {
        @Override
        public <T extends Book> void sort(List<T> books) {
            books.sort(Comparator.comparing(Book::getTitle));
        }
    }

    static class SortByAuthor implements SortStrategy {
        @Override
        public <T extends Book> void sort(List<T> books) {
            books.sort(Comparator.comparing(Book::getAuthor));
        }
    }

    static class SortByYear implements SortStrategy {
        @Override
        public <T extends Book> void sort(List<T> books) {
            books.sort(Comparator.comparingInt(Book::getYear));
        }
    }

    // Observer Pattern
    interface Observer {
        void update(Book book);
    }

    static class Member implements Observer {
        private String name;

        public Member(String name) {
            this.name = name;
        }

        @Override
        public void update(Book book) {
            System.out.println("Hello " + name + ", new book added: " + book);
        }
    }

    // Main class to demonstrate functionality
    public static void main(String[] args) {
        BookStore<Book> store = new BookStore<>();
        store.addMember(new Member("Alice"));
        store.addMember(new Member("Bob"));

        Book book1 = new Book("Effective Java", "Joshua Bloch", 2008);
        Book book2 = new Book("Clean Code", "Robert C. Martin", 2008);
        Book book3 = new Book("Design Patterns", "Erich Gamma", 1994);

        store.addBook(book1);
        store.addBook(book2);
        store.addBook(book3);

        store.setSortStrategy(new SortByTitle());
        store.sortBooks();

        Iterator<Book> iterator = store.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
