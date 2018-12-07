import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class Library {
	private String name;
	private TreeSet<Book> registeredBook;
	private HashSet<Borrower> registeredBorrower;

	public Library(String name) {
		this.name = name;
		registeredBook = new TreeSet<Book>(new Comp());
		registeredBorrower = new HashSet<Borrower>();
	}

	// �̿��� ���
	public void registerOneBorrower(String studentName, int studentNumber) {
		if (checkDuplicate(studentName, studentNumber)) {
			Borrower borrower = new Borrower(studentName, studentNumber);
			registeredBorrower.add(borrower);
			System.out.println(
					"Student name :" + studentName + ", Student number :" + studentNumber + " has been registered.");
		} else {
			System.out.println("Registration failed due to duplicate name and catalogue number.");
		}
	}

	// �̿��� ��Ͻ� �̸�, �й� �ߺ�Ȯ��
	public boolean checkDuplicate(String studentName, int studentNumber) {
		Iterator<Borrower> iterator = registeredBorrower.iterator();
		Borrower borrower;
		while (iterator.hasNext()) {
			borrower = iterator.next();
			if (borrower.getStudentName().equals(studentName) || borrower.getStudentNumber() == studentNumber)
				return false;
		}
		return true;
	}

	// å ���
	public void registerOneBook(String title, String author, int catalogueNumber) {
		if (checkDuplicate(catalogueNumber)) {
			Book book = new Book(title, author, catalogueNumber);
			registeredBook.add(book);
			System.out.println("Book's name :" + title + ", Book's author :" + author + ", Book's catalogue number :"
					+ catalogueNumber + " has been registered.");
		} else {
			System.out.println("Registertion failed due to duplicate book's catalogue number.");
		}
	}

	// å ��Ͻ� �з���ȣ �ߺ�Ȯ��
	public boolean checkDuplicate(int catalogueNumber) {
		for (Book book : registeredBook) {
			if (book.getCatalogueNumber() == catalogueNumber) {
				return false;
			}
		}
		return true;
	}

	// ���Ⱑ���� å ����
	public void displayBooksForLoan() {
		Iterator<Book> iter = registeredBook.iterator();
		while (iter.hasNext() == true) {
			Book book = (Book) iter.next();
			if (book.getBorrower() == null) {
				System.out.println("Book's title : " + book.getTitle() + ", Book's author : " + book.getAuthor()
						+ ", Book's catalogue number : " + book.getCatalogueNumber()
						+ "\n------------------------------");
			}
		}
	}

	// �������� å ����
	public void displayBooksOnLoan() {
		Iterator<Book> iter = registeredBook.iterator();
		while (iter.hasNext() == true) {
			Book book = (Book) iter.next();
			if (book.getBorrower() != null)
				System.out.println("Book's title : " + book.getTitle() + ", Book's author : " + book.getAuthor()
						+ ", Book's catalogue number : " + book.getCatalogueNumber()
						+ "\n------------------------------");
		}
	}

	// å ã��
	public Book findBook(int catalogueNumber) {
		Book target = null;
		for (Book book : registeredBook) {
			if (book.getCatalogueNumber() == catalogueNumber) {
				target = book;
				break;
			}
		}
		if (target.getBorrower() != null) {
			return target;
		} else {
			return target;
		}

	}

	// �̿��� ã��
	public Borrower findBorrower(int studentNumber) {
		Borrower target = null;
		for (Borrower borrower : registeredBorrower) {
			if (borrower.getStudentNumber() == studentNumber) {
				target = borrower;
				break;
			}
		}
		return target;
	}

	// å ����
	public void lendOneBook(int catalogueNumber, int studentNumber) {
		Book book = findBook(catalogueNumber);
		Borrower borrower = findBorrower(studentNumber);
		if (book != null) {
			if (borrower != null) {
				book.attachBorrower(borrower);
				borrower.attachBook(book);
				System.out.println(borrower.getStudentName() + " lent out " + book.getTitle());
			}
		}
	}

	// å �ݳ�
	public void returnOneBook(int catalogueNumber) {
		Book book = findBook(catalogueNumber);
		Borrower borrower = book.getBorrower();
		book.detachBorrower();
		borrower.detachBook(catalogueNumber);
		System.out.println(borrower.getStudentName() + " returned " + book.getTitle());
	}

	// �������� ����Ǿ��ִ� �̿���������� å�������� ObjectOutputStream���� �о����.
	public void loadFile() {
		try {
			FileInputStream file = new FileInputStream("borrower.tmp");
			ObjectInputStream in = new ObjectInputStream(file);
			registeredBorrower = (HashSet<Borrower>) in.readObject();
			file = new FileInputStream("book.tmp");
			in = new ObjectInputStream(file);
			registeredBook = (TreeSet<Book>) in.readObject();
		} catch (Exception e) {
		}
	}

	// ���� �������� ����Ǿ��ִ� �̿���������� å�������� ObjectInputStream���� ������.
	public void saveFile() {
		try {
			FileOutputStream file = new FileOutputStream("borrower.tmp");
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(registeredBorrower);
			file = new FileOutputStream("book.tmp");
			out = new ObjectOutputStream(file);
			out.writeObject(registeredBook);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �̿��� ����
	public void deleteOneBorrower(String studentName, int studentNumber) {
		for(Borrower borrower : registeredBorrower) {
			if(borrower.getStudentName().equals(studentName) && borrower.getStudentNumber() == studentNumber) {
				if(borrower.getBorrowBook().size() != 0) {
					System.out.println("Return books first.");
				} else{
					registeredBorrower.remove(borrower);
					System.out.println(studentName + " has been deleted.");
				}
			}
		}
	}
	
	// å ����
	public void deleteOneBook(int catalogueNumber) {
		for(Book book : registeredBook) {
			if(book.getCatalogueNumber() == catalogueNumber) {
				if(book.getBookBorrower().size() != 0) {
					System.out.println("Can't delete books on loan.");
				} else{
					registeredBook.remove(book);
					System.out.println(catalogueNumber + " has been deleted.");
				}
			}
		}
	}
}
