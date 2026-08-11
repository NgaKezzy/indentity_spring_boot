<div align="center">

# Identity Service API

REST API quản lý người dùng, vai trò, quyền hạn và xác thực JWT, được xây dựng với Spring Boot và MySQL.

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D?logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)

</div>

## Giới thiệu

Project cung cấp các API phục vụ quản lý danh tính, bao gồm:

- Đăng nhập, đăng xuất, làm mới và kiểm tra token.
- Quản lý người dùng.
- Quản lý vai trò và quyền hạn.
- Xác thực và phân quyền bằng JWT Bearer Token.
- Tài liệu API tương tác bằng Swagger UI.
- Xuất OpenAPI để sử dụng trong Postman.

## Công nghệ sử dụng

| Công nghệ | Mục đích |
| --- | --- |
| Java 17 | Ngôn ngữ lập trình |
| Spring Boot 4 | Xây dựng ứng dụng và REST API |
| Spring Security | Xác thực và phân quyền |
| Spring Data JPA | Truy cập dữ liệu |
| MySQL | Cơ sở dữ liệu |
| JWT / OAuth2 Resource Server | Bảo vệ API |
| Springdoc OpenAPI | Swagger UI và OpenAPI JSON |
| MapStruct & Lombok | Ánh xạ và rút gọn mã nguồn |
| Maven | Quản lý dependency và build |
| JUnit, JaCoCo & Testcontainers | Kiểm thử và báo cáo độ bao phủ |
| Spotless | Chuẩn hóa định dạng mã nguồn |

## Yêu cầu hệ thống

- JDK 17 trở lên.
- MySQL đang hoạt động.
- Không bắt buộc cài Maven vì project đã có Maven Wrapper.

## Cài đặt và chạy ứng dụng

### 1. Cấu hình cơ sở dữ liệu

Cập nhật thông tin kết nối trong `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/<database_name>?allowPublicKeyRetrieval=true&useSSL=false
    username: <mysql_username>
    password: <mysql_password>
```

Đảm bảo database đã tồn tại trước khi khởi động ứng dụng.

### 2. Khởi động ứng dụng

Trên macOS/Linux:

```bash
./mvnw spring-boot:run
```

Trên Windows:

```powershell
mvnw.cmd spring-boot:run
```

Ứng dụng chạy mặc định tại:

```text
http://localhost:8080/indentity
```

> **Lưu ý:** `indentity` là context path hiện đang được khai báo trong `application.yaml`.

## Xem và thử API bằng Swagger

Sau khi ứng dụng khởi động thành công, mở:

**Swagger UI:** http://localhost:8080/indentity/swagger-ui.html

**OpenAPI JSON:** http://localhost:8080/indentity/v3/api-docs

Để gọi thử một API công khai, mở endpoint cần kiểm tra, nhấn **Try it out**, nhập dữ liệu rồi nhấn **Execute**.

### Gọi API yêu cầu JWT

1. Gọi `POST /auth/login` để lấy access token.
2. Nhấn **Authorize** ở đầu trang Swagger UI.
3. Nhập access token vào trường `bearerAuth` và nhấn **Authorize**.
4. Mở endpoint cần kiểm tra, nhấn **Try it out** rồi **Execute**.

## Import collection vào Postman

Ứng dụng phải đang chạy để Postman truy cập tài liệu OpenAPI.

### Import trực tiếp bằng URL

1. Mở Postman và chọn **Import**.
2. Chọn **Link**.
3. Nhập URL sau:

   ```text
   http://localhost:8080/indentity/v3/api-docs
   ```

4. Nhấn **Import** để Postman tự động tạo collection.

### Import bằng file JSON

Tải tài liệu OpenAPI về máy:

```bash
curl http://localhost:8080/indentity/v3/api-docs \
  -o identity-api.json
```

Trong Postman, chọn **Import** → **Files** rồi chọn file `identity-api.json`.

### Cấu hình JWT trong Postman

1. Gọi API đăng nhập để lấy access token.
2. Mở collection và chọn tab **Authorization**.
3. Chọn **Bearer Token** rồi nhập access token.
4. Đặt các request thành **Inherit auth from parent** để dùng chung token.

## Các nhóm API chính

| Đường dẫn | Chức năng |
| --- | --- |
| `/auth` | Đăng nhập, đăng xuất, introspect và refresh token |
| `/users` | Quản lý người dùng |
| `/roles` | Quản lý vai trò |
| `/permissions` | Quản lý quyền hạn |

Tất cả đường dẫn API đều nằm sau context path `/indentity`. Chi tiết request và response được cập nhật tự động tại Swagger UI.

## Kiểm thử và chất lượng mã nguồn

Chạy toàn bộ test:

```bash
./mvnw test
```

Chạy quy trình kiểm tra đầy đủ, bao gồm test, báo cáo JaCoCo và kiểm tra định dạng:

```bash
./mvnw verify
```

Tự động định dạng source Java và `pom.xml`:

```bash
./mvnw spotless:apply
```

Chỉ kiểm tra định dạng mà không thay đổi file:

```bash
./mvnw spotless:check
```

Nếu Spotless báo lỗi, chạy `./mvnw spotless:apply`, kiểm tra lại thay đổi rồi chạy lại `./mvnw verify`.
