# indentity_spring_boot

## Chuẩn hóa code với Spotless

Tự động format toàn bộ source Java và `pom.xml`:

```bash
./mvnw spotless:apply
```

Kiểm tra format mà không thay đổi file:

```bash
./mvnw spotless:check
```

Chạy quy trình kiểm tra đầy đủ, bao gồm test, báo cáo JaCoCo và Spotless check:

```bash
./mvnw verify
```

Nếu kiểm tra format thất bại, hãy chạy `./mvnw spotless:apply`, xem lại các thay đổi và chạy lại lệnh `verify`.
