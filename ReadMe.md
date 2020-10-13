# Assignment 2

Assignment 2 is an extension of CS-102: Assignment 1 with added features. It maintains a tennis database containing tennis matches and tennis players. All data is stored sorted and the program allows user to manipulate data from a user interface. User can add, delete and even export data. 

#### Explanation of JCF Class Chosen for TennisMatchesContainer Implementation:

> In the assignment to store TennisMatches I have used LinkedList instead of Arraylist. This is because for I believe for my implementation Linked List is better. In my code I am frequently manipulating data in TennisMatchContainer such as adding data or deleting data. Also deleting one player could result in either deleting no matches or several matches therefore I used linked list instead of array list. 
### A comparison of Linked List vs Array List in terms of performance
|Operation|Linked List|Array List|
|--|--|--|
|Insertion|add(E element) is _O(1)_|add(int index, E element) is  _O(n)_ <br/>(with  _n/2_  steps on average)|
|Deletion|remove(int index) is _O(n)_<br/>(with _n/4_ steps on average)|remove(int index) is _O(n)_<br/>(with _n/2_ steps on average)|
|Get|get(int index) is _O(n)_<br/>(with _n/4_ steps on average)|get(int index) is _O(1)_|
> For the most of cases Linked list is faster when it comes to manipulating data therefore I have used linked list
