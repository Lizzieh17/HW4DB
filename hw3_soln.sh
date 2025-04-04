# user=lal013
# password=ooveiz0M

mysql <<EOFMYSQL
use MYUSERNAME;
show tables;

DROP TABLE IF EXISTS Bookstore;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Copy;
DROP TABLE IF EXISTS Purchase;

#Create Tables:
CREATE TABLE Bookstore(
bookstoreID INT PRIMARY KEY, 
bookstoreName CHAR(25) NOT NULL, 
state CHAR(2) NOT NULL, 
city CHAR(15) NOT NULL
);

CREATE TABLE Book(
  bookID INT PRIMARY KEY,
  bookName CHAR(25) NOT NULL,
  author CHAR(25) NOT NULL,
  publicationDate DATE DEFAULT NULL,
  type ENUM('fic', 'non') DEFAULT NULL
);

CREATE TABLE Copy(
  copyID INT PRIMARY KEY KEY,
  bookstoreID INT,
  bookID INT,
  price DEC(4, 2) CHECK (price BETWEEN 5 AND 50),
  FOREIGN KEY (bookstoreID) REFERENCES Bookstore(bookstoreID) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (bookID) REFERENCES Book(bookID) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Purchase(
  purchaseId INT PRIMARY KEY,
  copyID INT,
  date DATE CHECK (' 2025 -01 -01 ' <= date),
  time TIME,
  FOREIGN KEY (copyID) REFERENCES Copy(copyID) ON DELETE CASCADE ON UPDATE CASCADE
);

#Add data:

#Bookstore:
INSERT INTO
    Bookstore (bookstoreID, bookstoreName, state, city)
VALUES
    (0, 'Barnes and Noble', 'MO', 'Kansas City'),
    (5, 'Dickson Street Bookshop', 'AR', 'Fayetteville'),
    (11, 'Pearl''s Books', 'AR', 'Fayetteville');

#Book:
INSERT INTO Book (bookID, bookName, author, publicationDate, type)
VALUES
    (9, 'Brave New World', 'Aldous Huxley', '1932-02-04', 'fic'),
    (10, 'To Kill a Mockingbird', 'Harper Lee', '1960-07-11', 'fic'),
    (13, 'Godel, Escher, Bach', 'Douglas Hofstadter', '1979-01-01', 'non'),
    (21, 'The Brothers Karamazov', 'Fyodor Dostoevsky', '1880-11-01', 'fic'),
    (15, 'The Hiding Place', 'Corrie Ten Boom', '1971-01-01', 'non'),
    (16, 'The Grapes of Wrath', 'John Steinbeck', '1939-04-14', 'fic'),
    (37, 'Watchmen', 'Alan Moore', '1986-05-13', 'fic'),
    (4, 'Life of Pi', 'Yann Martel', '2001-09-11', 'fic'),
    (29, 'Unbroken', 'Laura Hillenbrand', '2010-11-16', 'non'),
    (42, 'The Return of The King', 'J. R. R. Tolkien', '1955-10-20', 'fic');

#Copy:
INSERT INTO Copy (copyID, bookstoreID, bookID, price)
VALUES
    (0, 0, 42, 25.00),
    (1, 0, 13, 35.00),
    (2, 0, 37, 18.75),
    (3, 0, 10, 12.00),
    (4, 0, 29, 15.00),
    (5, 5, 16, 10.00),
    (6, 5, 42, 15.00),
    (7, 5, 4, 8.25),
    (8, 5, 21, 21.00),
    (9, 11, 10, 15.00),
    (10, 11, 9, 12.00),
    (11, 11, 15, 10.00);

#Purchase: 
INSERT INTO Purchase (purchaseId, copyID, date, time)
VALUES
    (0, 6, '2025-01-15', '10:32'),
    (1, 8, '2025-01-18', '08:45'),
    (2, 11, '2025-01-04', '14:37'),
    (3, 4, '2025-01-29', '18:00'),
    (4, 5, '2025-02-01', '12:18'),
    (5, 1, '2025-02-03', '21:30'),
    (6, 10, '2025-02-09', '16:18'),
    (7, 7, '2025-02-14', '23:00'),
    (8, 9, '2025-02-21', '20:05');

Tasks:

#1:
SHOW TABLES;

#2:
DESC Bookstore;
DESC Book;
DESC Copy;
DESC Purchase;

#3:
SHOW CREATE TABLE Bookstore;
SHOW CREATE TABLE Book;
SHOW CREATE TABLE Copy;
SHOW CREATE TABLE Purchase;

#4:
SELECT COLUMN_NAME, CONSTRAINT_NAME, REFERENCED_COLUMN_NAME, REFERENCED_TABLE_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE 
WHERE REFERENCED_COLUMN_NAME IS NOT NULL AND CONSTRAINT_SCHEMA = 'lal013';

#5:
SELECT * FROM Bookstore;
SELECT * FROM Book;
SELECT * FROM Copy;
SELECT * FROM Purchase;

#6:
SELECT Bookstore.bookstoreName, Bookstore.city 
FROM Bookstore
JOIN Copy ON Bookstore.bookstoreID = Copy.bookstoreID
JOIN Book ON Copy.bookID = Book.bookID
WHERE Book.bookName = 'To Kill a Mockingbird';

#7:
SELECT 
	Book.bookName, 
	Bookstore.bookstoreName, 
	Bookstore.city, 
	Bookstore.state, 
	Copy.price
FROM Copy
JOIN Book ON Copy.bookID = Book.bookID
JOIN Bookstore ON Copy.bookstoreID = Bookstore.bookstoreID
WHERE Bookstore.state = 'AR'
ORDER BY Copy.price;

#8:
SELECT
	Bookstore.bookstoreName,
	Bookstore.city,
	Bookstore.state,
	COUNT(Copy.copyID) AS numberOfItems,
	AVG(Copy.price) AS averagePrice
FROM Copy
JOIN Bookstore ON Copy.bookstoreID = Bookstore.bookstoreID
GROUP BY Bookstore.bookstoreName, Bookstore.city, Bookstore.state;

#9:
SELECT
	COUNT(Purchase.purchaseID) AS numberOfPurchases,
	AVG(Copy.price) AS averageBasePrice,
	SUM(Copy.price) AS totalBasePrice,
	SUM(Copy.price) * 1.15 AS totalPriceWithTax
FROM Purchase
JOIN Copy ON Purchase.copyID = Copy.copyID
JOIN Bookstore ON Copy.bookstoreID = Bookstore.bookstoreID
WHERE Bookstore.bookstoreName = 'Pearl''s Books';

# 10:
SELECT
	COUNT(Purchase.purchaseID) AS numberOfPurchases,
	AVG(Copy.price) AS averageBasePrice,
	SUM(Copy.price) AS totalBasePrice,
	SUM(Copy.price) * 1.15 AS totalPriceWithTax
FROM Purchase
JOIN Copy ON Purchase.copyID = Copy.copyID
WHERE Purchase.date <= '2025-01-31' AND Purchase.date >= '2025-01-01';

#11:
UPDATE Bookstore
SET Bookstore.bookstoreID = 1
WHERE Bookstore.bookstoreID = 0;
SELECT * FROM Bookstore;
SELECT * FROM Copy;

#12:
DELETE FROM Book
WHERE Book.bookID = 9;
SELECT * FROM Book;
SELECT * FROM Copy;
SELECT * FROM Purchase;


EOFMYSQL