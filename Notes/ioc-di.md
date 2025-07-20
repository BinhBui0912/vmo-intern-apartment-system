# IoC và DI
---
## 1. IoC – Inversion of Control (Đảo ngược điều khiển)
### 1.1 Nguyên lý
**IoC** là nguyên lý thiết kế trong đó quyền điều khiển việc tạo và quản lý đối tượng được chuyển từ chính đối tượng đó sang một bên thứ ba (thường là framework như Spring).
### 1.2 Ví dụ
Không dùng IoC:
```java
ServiceA serviceA = new ServiceA(); // Tự khởi tạo
```
Có IoC:
```java
@Service
public class ServiceA {
    // Spring sẽ tạo và quản lý ServiceA 
}
```
## 2. DI – Dependency Injection (Tiêm phụ thuộc)
### 2.1 Khái niệm

- **Dependency Injection (DI)** là một kỹ thuật lập trình cho phép một lớp không tự tạo các đối tượng mà nó phụ thuộc vào, mà nhận chúng từ bên ngoài.  
- DI là một cách cụ thể để thực hiện nguyên lý IoC (Inversion of Control).

---

### 2.2 Các loại DI phổ biến trong Java (Spring)

| Loại DI                  | Mô tả                                       |
|---------------------------|---------------------------------------------|
| **Constructor Injection** | Tiêm phụ thuộc thông qua constructor        |
| **Setter Injection**      | Tiêm thông qua phương thức setter           |
| **Field Injection**       | Tiêm trực tiếp vào thuộc tính (dùng `@Autowired`) |

---

### 2.3 Ví dụ: 
```java
@Service
public class ServiceA {
    public void doSomething() {
        System.out.println("Service A logic");
    }
}
```
#### Constructor Injection trong Spring Boot:
```java
@RestController
public class MyController {

    private final ServiceA serviceA;

    // Constructor Injection
    public MyController(ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    @GetMapping("/run")
    public void run() {
        serviceA.doSomething();
    }
}
```
#### Setter Injection trong Spring Boot:
```java
@RestController
public class SetterInjectionController {

    private ServiceB serviceB;

    // Setter Injection
    @Autowired
    public void setServiceB(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    @GetMapping("/setter")
    public void handle() {
        serviceB.doSomething();
    }
}
```
#### Field Injection trong Spring Boot:
```java
@RestController
public class FieldInjectionController {

    @Autowired
    private ServiceC serviceC;

    @GetMapping("/field")
    public void handle() {
        serviceC.doSomething();
    }
}
```