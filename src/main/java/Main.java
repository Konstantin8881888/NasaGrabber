import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main
{
    //Сылка на запрос
    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=gq8lMd0ruQDDcILWu0JmtcrhlQBK3CVv1dRGOwEi";
    //Сущность, которая будет преобразовывать ответ в наш объект
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException
    {

        //Настраиваем  HTTP клиент, отправляющий запросы
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        //Отправляем запрос и получаем ответ
        CloseableHttpResponse response = httpClient.execute(new HttpGet(URI));

        //Преобразуем ответ в Java-объект из Джейсона
        NasaObject nasaObject = mapper.readValue(response.getEntity().getContent(), NasaObject.class);

        // Отправляем запрос и получаем ответ с картинкой
        CloseableHttpResponse pictureResponse = httpClient.execute(new HttpGet(nasaObject.getUrl()));

        //Формируем название для файла
        String[] arr = nasaObject.getUrl().split("/");
        String file = arr[arr.length -1];
        System.out.println(file);

        //Проверяем что наш ответ не null
        HttpEntity entity = pictureResponse.getEntity();
        if (entity != null)
        {
            //сохраняем в файл
            FileOutputStream fos = new FileOutputStream(file);
            entity.writeTo(fos);
            fos.close();
        }
        else
        {
            System.out.println("Объект пуст!");
        }

    }
}

