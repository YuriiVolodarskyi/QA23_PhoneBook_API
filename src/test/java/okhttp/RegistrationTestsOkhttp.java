package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class RegistrationTestsOkhttp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void RegistrationSuccess() throws IOException {
        int i = (int) (System.currentTimeMillis() / 1000) % 3600;
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("alimych" + i + "0@gmail.com")
                .password("Yv030665!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);
        AuthResponseDTO responseDTO = gson.fromJson(response.body().string(), AuthResponseDTO.class);
        System.out.println(responseDTO.getToken());
    }

    @Test
    public void RegistrationWrongEmail() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("alimych0gmail.com")
                .password("Yv030665!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        Assert.assertEquals(errorDTO.getPath(), "/v1/user/registration/usernamepassword");
    }

    @Test
    public void RegistrationWrongPassword() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("alimych1@gmail.com")
                .password("v030665")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 400);
        //Assert.assertEquals(errorDTO.getMessage(), "???");
        Assert.assertEquals(errorDTO.getPath(), "/v1/user/registration/usernamepassword");
    }

    @Test
    public void RegistrationWrongDuplicateUser() throws IOException {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("alimych65@gmail.com")
                .password("Yv030665!")
                .build();
        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 409);
        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getStatus(), 409);
        Assert.assertEquals(errorDTO.getMessage(), "User already exists");
        Assert.assertEquals(errorDTO.getPath(), "/v1/user/registration/usernamepassword");
    }
}
