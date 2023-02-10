package retrofit2;

import configuration.Configuration;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitBuilder {
    private static Retrofit retrofit;

    public static Retrofit build() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getStartUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit;
    }
}
