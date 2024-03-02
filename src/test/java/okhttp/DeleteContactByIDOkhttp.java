package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIDOkhttp {
    String id;
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    @BeforeMethod
    public void PreCondition() throws IOException {
        int i = new Random().nextInt(1000) + 1000;
        //     (int) (System.currentTimeMillis() / 1000) % 3600;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Vasya")
                .lastName("Pupkin")
                .email("vasya" + i + "@mail.ru")
                .phone("565656566" + i)
                .address("Haifa")
                .description("Vasiliy!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(contactDTO), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        MessageDTO messageDTO = gson.fromJson(response.body().string(), MessageDTO.class);
        String message = messageDTO.getMessage();
        String[] all = message.split(": ");
        id = all[1];
        System.out.println(all[1]);

    }

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYWxpbXljaDY1QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzA5NTcxMTQ4LCJpYXQiOjE3MDg5NzExNDh9.S89Ze-AmJnqPsmi-7byUdu-qmc3O4Dc1KClpeTb7E1s";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
    public void deleteContactByIDSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        MessageDTO dto = gson.fromJson(response.body().string(), MessageDTO.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(), "Contact was deleted!");

    }

    @Test
    public void deleteContactByIDWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/fe48747e-52d7-4ede-8245-4e4d4f672fa3")
                .delete()
                .addHeader("Authorization", "iu7y67fg7uyfgv")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
       Assert.assertEquals(errorDTO.getError(), "Unauthorized");

    }
    @Test
    public void deleteContactByIDNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + 123)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(), "Contact with id: 123 not found in your contacts!");

    }
}
