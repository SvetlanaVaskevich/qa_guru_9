package bk.vaskevich.demoqa;

import bk.vaskevich.helpers.Attach;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class DemodaTest {

    @BeforeAll
    public static void init(){
        SelenideLogger.addListener("AllureSelenide",new AllureSelenide());
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        //enableVNC: true
        //enableVideo: false
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC","true");
        capabilities.setCapability("enableVideo","false");
        Configuration.browserCapabilities = capabilities;
        Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
    }

    @Test
    public void FillFormTest() {
        step("Открываем страницу регистрации",()->{
            open("/automation-practice-form");
            $(".practice-form-wrapper").shouldHave(text("Student Registration Form"));
        });
        step("Заполняем страницу данными",()->{
            $("#firstName").setValue("Svetlana");
            $("#lastName").setValue("Ivanova");
            $("#userEmail").setValue("mymail@bk.ru");
            $(byText("Female")).click();
            $("#userNumber").setValue("8999456123");
        });
        step("Заполняем дату рождения",()->{
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").click();
            $(".react-datepicker__month-select").selectOption("December");
            $(".react-datepicker__year-select").selectOption("1987");
            $(".react-datepicker__day.react-datepicker__day--023").click();
        });
        step("Заолняем данные о хобби",()->{
            $("#subjectsInput").setValue("English").pressEnter();
            $(byText("Sports")).click();
        });
        //$("#uploadPicture").uploadFromClasspath("sport1.png");
        step("Заполняем данные об адресе",()->{
            $("#currentAddress").setValue("Russia,Ekaterinburg").scrollTo();
            $("#state").click();
            $("#stateCity-wrapper").$(byText("NCR")).click();
            $("#city").click();
            $("#stateCity-wrapper").$(byText("Noida")).click();
            $("#submit").click();
        });
        step("Проверка таблицы данных",()->{
            $(".table-responsive").shouldHave(text("Svetlana Ivanova"),text("mymail@bk.ru"),
                    text("Female"),text("23 December,1987"),text("English"),text("Sports"),text(""),
                    text("Russia,Ekaterinburg"),text("NCR Noida"));
        });
    }

    @AfterEach
    public void addAttachment(){
        Attach.screenshotAs("Screnchot demoqa");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }

    @AfterAll
    public static void close(){
        closeWebDriver();
    }
}
