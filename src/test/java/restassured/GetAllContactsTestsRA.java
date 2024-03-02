package restassured;

import com.jayway.restassured.RestAssured;
import dto.ContactDTO;
import dto.GetAllContactDTO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetAllContactsTestsRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYWxpbXljaDY1QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzA5NTcxMTQ4LCJpYXQiOjE3MDg5NzExNDh9.S89Ze-AmJnqPsmi-7byUdu-qmc3O4Dc1KClpeTb7E1s";

    @BeforeMethod
    public void PreCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }
    @Test
    public void getAllContactsSuccess(){
        GetAllContactDTO contactDTO = given()
                .header("Authorization", token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactDTO.class);
        List<ContactDTO> list = contactDTO.getContacts();
        for (ContactDTO contact: list){
            System.out.println(contact.getId());
            System.out.println(contact.getEmail());
            System.out.println("Size of list --> " + list.size());
        }


    }
}
