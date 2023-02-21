import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.List;

public class MyTelegramBot extends TelegramLongPollingBot
{
    public static final String BOT_TOKEN = "5990127953:AAFlN1WoURRvujsowCSOvb5Xf8GJfwGZ2cg";

    public static final String BOT_USERNAME = "NasaGrabberBot_bot";

    public static final String URI = "https://api.nasa.gov/planetary/apod?api_key=gq8lMd0ruQDDcILWu0JmtcrhlQBK3CVv1dRGOwEi";

    public static long chat_id;

    public MyTelegramBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername()
    {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken()
    {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage())
        {
            chat_id = update.getMessage().getChatId();
            switch (update.getMessage().getText())
            {
                case "/help":
                    sendMessage("Бот-граббер картинок от NASA! Получаем ссылки на картинки с сайта НАСА по запросу. " +
                            "Картинки на сайте NASA обновляются один раз в сутки. " +
                            "Для получения картинки введите команду /give");
                    break;
                case "/give":
                    try
                    {
                        sendMessage(Utils.getUrl(URI));
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException(e);
                    }
                    break;
                default:
                    sendMessage("Запрос не относится к доступным командам! Прочтите /help чтобы посмотреть список доступных команд.");
            }
        }
    }

    private void sendMessage(String messageText)
    {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(messageText);
        try
        {
            execute(message);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

}
