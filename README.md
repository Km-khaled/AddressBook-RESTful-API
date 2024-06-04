# Address Book RESTful API

This application is a RESTful API developed in Java with Jersey that allows you to manage an address book. It provides features to create, read, update, and delete address books and associated addresses. To access the files, you need to write the following link in your browser: `http://localhost:8080/tprest/webapi/carnets`. You must have MySQL running, either through XAMPP or another method.

## Features

- Create a new address book
- List all existing address books
- Delete an existing address book
- Add a new address to a specific address book
- List all addresses in a specific address book
- Retrieve details of a specific address
- Delete a specific address from an address book

## Technologies Used

- Java
- Jersey (JAX-RS)
- MySQL

## Prerequisites

- Java Development Kit (JDK) installed
- MySQL Server installed

## Configuration

1. Clone the GitHub repository to your local machine.
2. Configure the MySQL database connection settings in the `SingletonConnection.java` file.
3. Create the database and necessary tables by executing the provided SQL scripts.

## Execution

1. Compile the Java source code.
2. Deploy the application to a Jersey-compatible application server.
3. Access the RESTful resources using the base URL and appropriate paths.

## Usage Examples

- Create a new address book:
  `POST http://localhost:8080/tprest/webapi/carnets` with the address book data in the request body.
- List all address books:
  `GET http://localhost:8080/tprest/webapi/carnets`
- Delete an address book:
  `DELETE http://localhost:8080/tprest/webapi/carnets` with the address book name in the request body.
- Add a new address:
  `POST http://localhost:8080/tprest/webapi/carnets/{nom_carnet}/adresses` with the address data in the request body.
- List all addresses in an address book:
  `GET http://localhost:8080/tprest/webapi/carnets/{nom_carnet}/adresses`
- Retrieve details of an address:
  `GET http://localhost:8080/tprest/webapi/carnets/{nom_carnet}/adresses/{nom_personne}`
- Delete an address:
  `DELETE http://localhost:8080/tprest/webapi/carnets/{nom_carnet}/adresses` with the person's name in the request body.

## Contribution

Contributions are welcome! If you have any suggestions for improvements, bug fixes, or new features, feel free to submit a pull request.

## License

This project is licensed under the [MIT](LICENSE) license.
