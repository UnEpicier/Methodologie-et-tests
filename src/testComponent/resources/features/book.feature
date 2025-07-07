Feature: the user can create and retrieve the books

  Scenario: user creates two books and retrieve both of them
    When the user creates the book "Les Misérables" written by "Victor Hugo"
    And the user creates the book "L'avare" written by "Molière"
    And the user gets all books
    Then the list should contains the following books in the same order
      | id | title          | author      | booked |
      | 2  | L'avare        | Molière     | false  |
      | 1  | Les Misérables | Victor Hugo | false  |


  Scenario: User books a book
    When the user creates the book "1984" written by "George Orwell"
    And the user books the book with id 1
    And the user gets all books
    Then the list should contains the following books in the same order
      | id | title | author        | booked |
      | 1  | 1984  | George Orwell | true   |

  Scenario: User tries to reserve a non-existing book
    When the user tries to book a non-existing book with id 999
    Then the booking request should fail with status code 404

  Scenario: User unbooks a book
    When the user creates the book "Dune" written by "Frank Herbert"
    And the user books the book with id 1
    And the user unbooks the book with id 1
    And the user gets all books
    Then the list should contains the following books in the same order
      | id | title | author        | booked |
      | 1  | Dune  | Frank Herbert | false  |