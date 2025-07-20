# SOLID
---
- Nguyên tắc SOLID trong lập trình hướng đối tượng

- **SOLID** là 5 nguyên tắc giúp viết mã **dễ hiểu**, **bảo trì tốt**, **mở rộng dễ**:

---

## 1. S – Single Responsibility Principle (Nguyên lý trách nhiệm đơn)
- Một lớp chỉ nên có **một lý do để thay đổi**.

- Ví dụ:  
`UserService` chỉ xử lý logic người dùng, không kiêm gửi email.
---

## 2. O – Open/Closed Principle (Nguyên lý mở rộng/đóng)
- Mở để **mở rộng**, đóng để **sửa đổi**.
- Ví dụ:  
Thêm loại thanh toán mới bằng cách **tạo class mới**, không sửa class cũ.

---

## 3. L – Liskov Substitution Principle (Nguyên lý thay thế Liskov)
- Class con phải có thể thay thế class cha mà **không làm sai chức năng**.

- Ví dụ:  
`Bird` có `fly()` → `Penguin` kế thừa nhưng **không bay được** → vi phạm.

---

## 4. I – Interface Segregation Principle (Nguyên lý phân tách interface)
- Interface nên **nhỏ gọn**, không ép class phải implement phương thức **không dùng**.
- Ví dụ:  
Không nên gộp `print()`, `fax()` vào 1 interface nếu có máy in không fax được.

---

## 5. D – Dependency Inversion Principle (Nguyên lý đảo ngược phụ thuộc)
- Lớp cấp cao không phụ thuộc lớp cấp thấp, mà cả hai cùng phụ thuộc vào **abstraction**.
- Ví dụ:  
`OrderService` dùng `PaymentProcessor` (interface), không dùng trực tiếp `Paypal`, `VNPay`.

