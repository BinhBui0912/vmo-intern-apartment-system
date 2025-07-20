# OOP – Object-Oriented Programming
---
## 1. Định nghĩa OOP
**OOP (Object-Oriented Programming)** – Lập trình hướng đối tượng –  là một phương pháp lập trình dựa trên khái niệm về lớp và đối tượng. OOP tập trung vào các đối tượng thao tác hơn là logic để thao tác chúng.

---
## 2. Đối tượng (Object) và Lớp (Class) trong OOP
### 2.1 Đối tượng (Object)
Đối tượng trong OOP bao gồm 2 thành phần chính:
- Thuộc tính (Attribute): là những thông tin, đặc điểm của đối tượng
- Phương thức (Method): là những hành vi mà đối tượng có thể thực hiện
### 2.2 Lớp (Class)
Lớp là sự trừu tượng hóa của đối tượng. Những đối tượng có những đặc tính tương tự nhau sẽ được tập hợp thành một lớp. Lớp cũng sẽ bao gồm 2 thông tin là thuộc tính và phương thức.
Một đối tượng sẽ được xem là một thực thể của lớp.

---
## 3 OOP gồm 4 tính chất cơ bản
| Tính chất                | Ý nghĩa                                                                 | Ví dụ |
|--------------------------|------------------------------------------------------------------------|-------|
| **1. Đóng gói** (*Encapsulation*) | Ẩn dữ liệu bên trong đối tượng, chỉ cho phép truy cập thông qua các phương thức | Dữ liệu nhân viên chỉ được truy cập qua `getName()` / `setName()` |
| **2. Kế thừa** (*Inheritance*)    | Lớp con kế thừa thuộc tính và hành vi từ lớp cha                       | `class Dog extends Animal` |
| **3. Đa hình** (*Polymorphism*)   | Liên quan đến Overloading và Overriding. Một phương thức có thể thực hiện theo nhiều cách khác nhau            | `speak()` của `Dog` khác `speak()` của `Cat` |
| **4. Trừu tượng** (*Abstraction*) | Ẩn chi tiết phức tạp, chỉ lộ ra phần cần thiết cho người dùng. Liên quan tới Abstract và Interface.          | Interface `Vehicle` có method `start()`, các lớp cụ thể tự triển khai |

---
## 4. Ví dụ cụ thể bằng Java

### 4.1. Lớp cha: `Animal.java`

```java
public class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public void speak() {
        System.out.println("This animal makes a sound");
    }

    public String getName() {
        return name;
    }
}
```
### 4.2. Lớp con kế thừa: `Dog.java`
```java
public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public void speak() {
        System.out.println(getName() + " says: Woof!");
    }
}
```
### 4.3. Lớp Main: `Main.java`
```java
public class Main {
    public static void main(String[] args) {
        Animal myAnimal = new Animal("Some animal");
        Dog myDog = new Dog("Buddy");

        myAnimal.speak(); // This animal makes a sound
        myDog.speak();    // Buddy says: Woof!
    }
}
```