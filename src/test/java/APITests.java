import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class APITests {


    @Test
    void specificBookAPI(){

        Response response = RestAssured.get("https://simple-books-api.glitch.me/books/2");
        System.out.println(response.getBody().asString());
        String expectedBookName = "Just as I Am";
        String actualBookName = response.getBody().jsonPath().getString("name");
        Assert.assertEquals(actualBookName, expectedBookName);

    }

    @Test
    void createOrder(){

        String baseUrl = "https://simple-books-api.glitch.me";
        String token = "3a7131bd81efe2cc48ff3fe8b2a5c2e9b0cf1dc8ad84dddfbc1d666f4272ec1a";
        String requestBody = "{\"bookId\": 1,\"customerName\": \"Omar\"}";

        Response response = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/orders/")
                .then()
                .statusCode(201)
                .extract().response();
        System.out.println(response.asString());

        Response response2 = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response2.asString());

    }

    @Test
    void patchOrder(){

        String baseUrl = "https://simple-books-api.glitch.me";
        String token = "3a7131bd81efe2cc48ff3fe8b2a5c2e9b0cf1dc8ad84dddfbc1d666f4272ec1a";
        String orderId = "AWrnSlQxtCNRDVKZ8raxZ";
        String requestBody = "{\"customerName\": \"Senger\"}";

        Response response = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch("/orders/"+ orderId)
                .then()
                .extract().response();

        Response response2 = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("/orders/"+ orderId)
                .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response2.asString());

        String actualCustomerName = response2.getBody().jsonPath().getString("customerName");
        Assert.assertEquals(actualCustomerName, "Senger");

    }

    @Test
    void deleteOrder(){

        String baseUrl = "https://simple-books-api.glitch.me";
        String token = "3a7131bd81efe2cc48ff3fe8b2a5c2e9b0cf1dc8ad84dddfbc1d666f4272ec1a";
        String orderId = "AWrnSlQxtCNRDVKZ8raxZ";

        Response response = given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .delete("/orders/"+ orderId)
                .then()
                .extract().response();

        String error = response.getBody().jsonPath().getString("error");
        Assert.assertEquals(error, "No order with id AWrnSlQxtCNRDVKZ8raxZ.");


    }


}
