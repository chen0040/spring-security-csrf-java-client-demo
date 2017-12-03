# spring-boot-slingshot

slingshot project with spring boot and spring security and spring data jpa as well as elastic search using jest

# Setup

To use this project create a database named spring_boot_slingshot in your mysql database (make sure it is running at localhost:3306)

```sql
CREATE DATABASE spring_boot_slingshot CHARACTER SET utf8 COLLATE utf8_unicode_ci;
```

Note that the default username and password for the mysql is configured to 

* username: root
* password: chen0469

If your mysql or mariadb does not use these configuration, please change the settings in src/resources/config/application-default.properties

The application will generate two accounts in the database on startup if they don't exist:

ADMIN:

* username: admin
* password: admin

DEMO:

* username: demo
* password: demo

For the spring security configuration, the CSRF is enabled.

# Usage

### Build the applications

Run the "./make.ps1" (windows environment) and "./make.sh" (unix environment). which will compile and stores the built
jars in the "bin" folder.

* spring-boot-application: the spring boot application that has csrf-enabled spring security configuration
* desktop-client: a simple swing desktop application that login the spring boot application using web api










