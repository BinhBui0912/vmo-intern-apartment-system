# Stateless vs Stateful
---
## 1. Stateless (Không trạng thái)

- Mỗi request là **độc lập**, **không phụ thuộc** vào request trước đó.
- Server **không lưu thông tin** gì về client giữa các lần request.
- Thường dùng trong các **REST API**, dễ **scale**, dễ **cache**.
- Client phải gửi đầy đủ thông tin mỗi lần gọi.

### 1.1 Ưu điểm:
- Dễ mở rộng (scalable)
- Dễ xử lý lỗi
- Không cần lưu session phía server

### 1.2 Nhược điểm:
- Mỗi lần request phải gửi đủ thông tin (gây thừa dữ liệu)

---
## 2. Stateful (Có trạng thái)
- Server lưu thông tin phiên làm việc (session) của client.

- Mỗi request có thể phụ thuộc vào các request trước.

- Thường dùng trong các hệ thống có quản lý phiên đăng nhập (session-based auth).
### 2.1 Ưu điểm:
- Không cần gửi lại đầy đủ thông tin mỗi lần
- Giao tiếp phức tạp dễ quản lý

### 2.2 Nhược điểm:
- Khó mở rộng server (phải chia sẻ session)
- Tốn tài nguyên lưu session