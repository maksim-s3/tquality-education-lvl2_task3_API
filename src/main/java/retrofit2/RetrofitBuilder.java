package retrofit2;

import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitBuilder {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private static Retrofit retrofit;
    public static Retrofit build(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit;
    }
}
