# REST API
---
## 1. REST API là gì?
**REST API** (Representational State Transfer – Application Programming Interface) là một kiểu kiến trúc được dùng để xây dựng các dịch vụ web, cho phép các hệ thống giao tiếp với nhau qua giao thức HTTP theo cách đơn giản, nhẹ và dễ mở rộng.

## 2. Đặc điểm của REST API

REST (Representational State Transfer) là một kiểu kiến trúc được sử dụng phổ biến để xây dựng các dịch vụ web. Dưới đây là một số đặc điểm chính của REST API:

- **Stateless** (Không trạng thái): Mỗi request từ client đến server phải chứa đầy đủ thông tin để server xử lý. Server không lưu trạng thái của client giữa các request.
- **Client-Server**: Client và server tách biệt rõ ràng. Client chỉ quan tâm đến giao diện người dùng, còn server xử lý logic và dữ liệu.
- **Cacheable**: REST cho phép cache dữ liệu để tăng hiệu năng.
- **Uniform Interface**: Giao tiếp giữa client và server tuân theo một quy chuẩn nhất định (thường là HTTP).
- **Layered System**: Kiến trúc REST có thể chia thành nhiều tầng (layer), mỗi tầng có thể xử lý một vai trò riêng biệt.
- **Resource-Based**: Dữ liệu được biểu diễn dưới dạng "resource" (ví dụ: user, product...), truy cập qua URL.

---

## 3. Các phương thức HTTP phổ biến trong REST API

| Phương thức | Mô tả | Ví dụ |
|------------|------|-------|
| **GET**    | Lấy thông tin từ server (an toàn, không thay đổi dữ liệu) | `GET /users` |
| **POST**   | Tạo mới một tài nguyên | `POST /users` |
| **PUT**    | Cập nhật toàn bộ tài nguyên | `PUT /users/1` |
| **PATCH**  | Cập nhật một phần tài nguyên | `PATCH /users/1` |
| **DELETE** | Xóa một tài nguyên | `DELETE /users/1` |

---

## 4. Ví dụ REST API đơn giản bằng Spring Boot
```java
@RestController
@RequestMapping("/api/books")
public class BookController {

    // Danh sách giả lập lưu trữ sách
    private final Map<Long, String> books = new HashMap<>();

    // GET: Lấy tất cả sách
    @GetMapping
    public Map<Long, String> getAllBooks() {
        return books;
    }

    // POST: Thêm sách mới
    @PostMapping
    public String addBook(@RequestParam Long id, @RequestParam String title) {
        books.put(id, title);
        return "Đã thêm sách: " + title;
    }

    // PUT: Cập nhật sách
    @PutMapping("/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam String title) {
        if (books.containsKey(id)) {
            books.put(id, title);
            return "Đã cập nhật sách ID " + id;
        }
        return "Không tìm thấy sách ID " + id;
    }

    // DELETE: Xoá sách
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        books.remove(id);
        return "Đã xoá sách ID " + id;
    }
}
```

