# 📚 BookWise - Digital Library Management System

A comprehensive Spring Boot-based digital library management system that allows users to browse, read, and manage books with role-based access control for readers and sellers.

## 🌟 Features

### 👥 User Management
- **Multi-role System**: Reader, Seller, and Super Admin roles
- **User Authentication**: Secure login with Spring Security
- **Profile Management**: Personal information, profile pictures, and preferences
- **Role-based Access**: Different interfaces for different user types

### 📖 Book Management
- **Book Upload**: Sellers can upload books with cover images and metadata
- **Book Browsing**: Readers can search and browse available books
- **Book Details**: Comprehensive book information display
- **PDF Viewer**: Integrated DFlip PDF viewer for reading books
- **Bookshelf**: Personal collection management for readers

### 🎨 User Interface
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Modern UI**: Bootstrap 5 with custom styling
- **Sidebar Navigation**: Role-based navigation menus
- **Search Functionality**: Real-time book search and filtering

### 🔧 Technical Features
- **Spring Boot**: Backend framework with RESTful APIs
- **Hibernate**: Object-relational mapping
- **AJAX**: Dynamic content loading without page refresh
- **File Upload**: Image and PDF file handling
- **Security**: CSRF protection and role-based authorization

## 🏗️ Architecture

### Backend Structure
```
src/main/java/com/bookWise/
├── controller/           # REST controllers
│   ├── HomeController.java
│   ├── BookWiseRestController.java
│   └── userProfile/
├── model/               # Entity classes
│   ├── BookWiseUser.java
│   ├── BookEncounter.java
│   └── BookRating.java
├── dao/                 # Data access layer
│   ├── BookWiseDAO.java
│   └── impl/
├── service/             # Business logic
│   ├── BookService.java
│   └── userService/
├── SecurityConfig/      # Security configuration
│   ├── BookWiseSecurityConfig.java
│   └── CustomAuthenticationSuccessHandler.java
└── util/               # Utility classes
    ├── FileUtils.java
    └── BookUtils.java
```

### Frontend Structure
```
src/main/webapp/
├── WEB-INF/views/
│   ├── Home.jsp              # Reader home page
│   ├── sellerHome.jsp        # Seller home page
│   ├── userProfile/          # User profile pages
│   ├── book-details/         # Book detail pages
│   ├── bookshelf/           # Bookshelf management
│   └── fragments/           # Reusable components
├── resources/
│   ├── script/              # JavaScript files
│   │   ├── commonComponents.js
│   │   ├── home/
│   │   ├── bookShelf/
│   │   └── userProfile/
│   ├── css/                 # Stylesheets
│   └── lib/dflip/          # PDF viewer library
└── images/                 # Static images
```

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Maven 3.6+
- MySQL 5.7+ or compatible database
- Web browser with JavaScript enabled

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd BookWise
   ```

2. **Configure database**
   - Create a MySQL database
   - Update `application.properties` with your database credentials
   - Run the SQL scripts in `ReleaseSQL/` directory

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open browser and navigate to `http://localhost:8080`
   - Login with default credentials or register new account

### Default Users
- **Reader**: `reader@bookwise.com` / `password`
- **Seller**: `seller@bookwise.com` / `password`
- **Admin**: `admin@bookwise.com` / `password`

## 📱 User Roles & Features

### 👤 Reader
- **Browse Books**: Search and view available books
- **Read Books**: Use integrated PDF viewer
- **Manage Bookshelf**: Add/remove books from personal collection
- **Rate Books**: Rate and review books
- **Profile Management**: Update personal information

### 🏪 Seller
- **Upload Books**: Add new books with metadata and cover images
- **Manage Inventory**: View and edit uploaded books
- **Book List**: Comprehensive list of all uploaded books
- **Profile Management**: Update seller information

### 🔧 Super Admin
- **User Management**: Manage all users and roles
- **System Monitoring**: View activity logs and statistics
- **Content Moderation**: Approve/reject book uploads

## 🔧 Configuration

### Database Configuration
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookwise
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### File Upload Configuration
```properties
# File upload settings
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
file.upload.path=/usr/local/bookWiseFile/
```

### Security Configuration
```properties
# Security settings
spring.security.user.name=admin
spring.security.user.password=admin
```

## 🛠️ Development

### Adding New Features

1. **Backend Changes**
   - Create entity classes in `model/`
   - Add DAO methods in `dao/`
   - Implement business logic in `service/`
   - Create REST controllers in `controller/`

2. **Frontend Changes**
   - Add JSP pages in `WEB-INF/views/`
   - Create JavaScript files in `resources/script/`
   - Add CSS styles in `resources/css/`

3. **Navigation Updates**
   - Update `commonComponents.js` for new pages
   - Modify sidebar navigation in respective JSP files

### Code Style Guidelines
- Use meaningful variable and function names
- Add comments for complex logic
- Follow Spring Boot conventions
- Use AJAX for dynamic content loading
- Implement proper error handling

## 📊 API Endpoints

### Authentication
- `POST /login` - User login
- `POST /logout` - User logout
- `GET /home` - Reader home page
- `GET /sellerHome` - Seller home page

### Book Management
- `GET /api/book/userHome/actions/getAllBooksUser` - Get all books
- `POST /api/book/actions/getSellerBooks` - Get seller's books
- `POST /api/book/upload` - Upload new book
- `GET /api/book/details/{id}` - Get book details

### User Profile
- `GET /api/user/profile/getUserProfileInfo` - Get user profile
- `POST /api/user/profile/saveUserProfileInfo` - Update user profile

### Bookshelf
- `GET /api/bookshelf/my-shelf` - Get user's bookshelf
- `POST /api/bookshelf/add` - Add book to bookshelf
- `DELETE /api/bookshelf/remove/{id}` - Remove book from bookshelf

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Manual Testing
1. Test user registration and login
2. Verify role-based access control
3. Test book upload and browsing
4. Verify PDF viewer functionality
5. Test responsive design on different devices

## 🚀 Deployment

### Production Deployment
1. **Build WAR file**
   ```bash
   mvn clean package
   ```

2. **Deploy to Tomcat**
   - Copy WAR file to `webapps/` directory
   - Configure database connection
   - Set up file upload directory

3. **Environment Configuration**
   - Update `application.properties` for production
   - Configure logging
   - Set up SSL certificate

### Docker Deployment
```dockerfile
FROM openjdk:8-jdk-alpine
COPY target/bookwise.war app.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.war"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation in `/docs` directory

## 🔄 Version History

- **v1.0.0** - Initial release with basic book management
- **v1.1.0** - Added PDF viewer and bookshelf functionality
- **v1.2.0** - Implemented role-based access control
- **v1.3.0** - Added responsive design and mobile support

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive UI components
- DFlip team for the PDF viewer library
- All contributors and testers

---

**BookWise** - Making digital reading accessible and enjoyable for everyone! 📚✨
